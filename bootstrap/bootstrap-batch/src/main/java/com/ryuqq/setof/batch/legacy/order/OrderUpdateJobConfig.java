package com.ryuqq.setof.batch.legacy.order;

import com.ryuqq.setof.batch.config.BatchProperties;
import com.ryuqq.setof.batch.legacy.order.dto.OrderUpdateResult;
import com.ryuqq.setof.batch.legacy.order.dto.UpdateOrder;
import com.ryuqq.setof.batch.legacy.order.enums.OrderStatus;
import com.ryuqq.setof.batch.legacy.order.strategy.CompletedOrderStrategy;
import com.ryuqq.setof.batch.legacy.order.strategy.OrderUpdateStrategy;
import com.ryuqq.setof.batch.legacy.order.strategy.RejectedOrderStrategy;
import com.ryuqq.setof.batch.legacy.order.strategy.SettlementProcessingStrategy;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 주문 상태 업데이트 배치 Job 설정 (DB 직접 업데이트)
 *
 * <p>Rejected, Completed, Settlement 주문 처리
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
public class OrderUpdateJobConfig {

    private static final Logger log = LoggerFactory.getLogger(OrderUpdateJobConfig.class);

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource legacyDataSource;
    private final JdbcTemplate legacyJdbcTemplate;
    private final BatchProperties batchProperties;

    private final RejectedOrderStrategy rejectedOrderStrategy;
    private final CompletedOrderStrategy completedOrderStrategy;
    private final SettlementProcessingStrategy settlementProcessingStrategy;

    public OrderUpdateJobConfig(
            JobRepository jobRepository,
            @Qualifier("legacyTransactionManager") PlatformTransactionManager transactionManager,
            @Qualifier("legacyDataSource") DataSource legacyDataSource,
            @Qualifier("legacyJdbcTemplate") JdbcTemplate legacyJdbcTemplate,
            BatchProperties batchProperties,
            RejectedOrderStrategy rejectedOrderStrategy,
            CompletedOrderStrategy completedOrderStrategy,
            SettlementProcessingStrategy settlementProcessingStrategy) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.legacyDataSource = legacyDataSource;
        this.legacyJdbcTemplate = legacyJdbcTemplate;
        this.batchProperties = batchProperties;
        this.rejectedOrderStrategy = rejectedOrderStrategy;
        this.completedOrderStrategy = completedOrderStrategy;
        this.settlementProcessingStrategy = settlementProcessingStrategy;
    }

    // ========================================
    // Rejected Orders Job
    // ========================================

    @Bean
    public Job updateRejectedOrdersJob() {
        return new JobBuilder("updateRejectedOrdersJob", jobRepository)
                .start(updateRejectedOrdersStep())
                .build();
    }

    @Bean
    public Step updateRejectedOrdersStep() {
        return new StepBuilder("updateRejectedOrdersStep", jobRepository)
                .<UpdateOrder, OrderUpdateResult>chunk(
                        batchProperties.getChunkSize(), transactionManager)
                .reader(rejectedOrderItemReader(null, null))
                .processor(orderStatusProcessor())
                .writer(orderStatusWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<UpdateOrder> rejectedOrderItemReader(
            @Value("#{jobParameters['startTime']}") String startTimeStr,
            @Value("#{jobParameters['endTime']}") String endTimeStr) {
        return createOrderItemReader(rejectedOrderStrategy, startTimeStr, endTimeStr);
    }

    // ========================================
    // Completed Orders Job
    // ========================================

    @Bean
    public Job updateCompletedOrdersJob() {
        return new JobBuilder("updateCompletedOrdersJob", jobRepository)
                .start(updateCompletedOrdersStep())
                .build();
    }

    @Bean
    public Step updateCompletedOrdersStep() {
        return new StepBuilder("updateCompletedOrdersStep", jobRepository)
                .<UpdateOrder, OrderUpdateResult>chunk(
                        batchProperties.getChunkSize(), transactionManager)
                .reader(completedOrderItemReader(null, null))
                .processor(orderStatusProcessor())
                .writer(orderStatusWriter())
                .faultTolerant()
                .skipLimit(batchProperties.getSkipLimit())
                .skip(Exception.class)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<UpdateOrder> completedOrderItemReader(
            @Value("#{jobParameters['startTime']}") String startTimeStr,
            @Value("#{jobParameters['endTime']}") String endTimeStr) {
        return createOrderItemReader(completedOrderStrategy, startTimeStr, endTimeStr);
    }

    // ========================================
    // Settlement Processing Orders Job
    // ========================================

    @Bean
    public Job updateSettlementOrdersJob() {
        return new JobBuilder("updateSettlementOrdersJob", jobRepository)
                .start(updateSettlementOrdersStep())
                .build();
    }

    @Bean
    public Step updateSettlementOrdersStep() {
        return new StepBuilder("updateSettlementOrdersStep", jobRepository)
                .<UpdateOrder, OrderUpdateResult>chunk(
                        batchProperties.getChunkSize(), transactionManager)
                .reader(settlementOrderItemReader(null, null))
                .processor(orderStatusProcessor())
                .writer(settlementOrderWriter())
                .faultTolerant()
                .skipLimit(batchProperties.getSkipLimit())
                .skip(Exception.class)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<UpdateOrder> settlementOrderItemReader(
            @Value("#{jobParameters['startTime']}") String startTimeStr,
            @Value("#{jobParameters['endTime']}") String endTimeStr) {
        return createOrderItemReader(settlementProcessingStrategy, startTimeStr, endTimeStr);
    }

    // ========================================
    // Common Components
    // ========================================

    private JdbcPagingItemReader<UpdateOrder> createOrderItemReader(
            OrderUpdateStrategy strategy, String startTimeStr, String endTimeStr) {

        LocalDateTime startTime =
                startTimeStr != null ? LocalDateTime.parse(startTimeStr) : strategy.getStartTime();
        LocalDateTime endTime =
                endTimeStr != null ? LocalDateTime.parse(endTimeStr) : strategy.getEndTime();

        List<String> statusNames = strategy.getTargetStatuses().stream().map(Enum::name).toList();

        String statusInClause =
                statusNames.stream().map(s -> "'" + s + "'").collect(Collectors.joining(","));

        JdbcPagingItemReader<UpdateOrder> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(legacyDataSource);
        reader.setPageSize(batchProperties.getChunkSize());
        reader.setRowMapper(
                (rs, rowNum) ->
                        new UpdateOrder(
                                rs.getLong("order_id"),
                                rs.getString("order_status"),
                                rs.getTimestamp("settlement_date") != null
                                        ? rs.getTimestamp("settlement_date").toLocalDateTime()
                                        : null));

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("o.order_id, o.order_status, o.settlement_date");
        queryProvider.setFromClause("orders o");
        queryProvider.setWhereClause(
                String.format(
                        "o.update_date >= '%s' AND o.update_date <= '%s' AND o.order_status IN"
                                + " (%s)",
                        Timestamp.valueOf(startTime), Timestamp.valueOf(endTime), statusInClause));

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("o.order_id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setQueryProvider(queryProvider);
        return reader;
    }

    @Bean
    public ItemProcessor<UpdateOrder, OrderUpdateResult> orderStatusProcessor() {
        Map<OrderStatus, OrderStatus> statusMapping = OrderStatus.getStatusMapping();

        return order -> {
            OrderStatus currentStatus = OrderStatus.valueOf(order.orderStatus());
            OrderStatus newStatus = statusMapping.get(currentStatus);

            if (newStatus == null) {
                log.warn("No status mapping found for: {}", currentStatus);
                return null;
            }

            log.debug(
                    "Processing order: {} from {} to {}",
                    order.orderId(),
                    currentStatus,
                    newStatus);
            return new OrderUpdateResult(order.orderId(), newStatus);
        };
    }

    @Bean
    public ItemWriter<OrderUpdateResult> orderStatusWriter() {
        return chunk -> {
            var items = chunk.getItems();
            if (items.isEmpty()) {
                return;
            }

            log.info("Updating {} orders", items.size());

            for (OrderUpdateResult item : items) {
                updateOrderStatus(item);
                insertOrderHistory(item);
            }

            log.info("Successfully updated {} orders", items.size());
        };
    }

    @Bean
    public ItemWriter<OrderUpdateResult> settlementOrderWriter() {
        return chunk -> {
            var items = chunk.getItems();
            if (items.isEmpty()) {
                return;
            }

            log.info("Updating {} settlement orders", items.size());

            for (OrderUpdateResult item : items) {
                updateOrderStatus(item);
                insertOrderHistory(item);
                updateSettlementDate(item);
            }

            log.info("Successfully updated {} settlement orders", items.size());
        };
    }

    private void updateOrderStatus(OrderUpdateResult item) {
        String sql =
                """
                UPDATE orders
                SET ORDER_STATUS = ?,
                    UPDATE_OPERATOR = 'BATCH',
                    UPDATE_DATE = NOW()
                WHERE order_id = ?
                """;

        legacyJdbcTemplate.update(sql, item.newStatus().name(), item.orderId());
    }

    private void insertOrderHistory(OrderUpdateResult item) {
        String sql =
                """
INSERT INTO orders_history (ORDER_ID, ORDER_STATUS, INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE, UPDATE_DATE)
VALUES (?, ?, 'BATCH', 'BATCH', NOW(), NOW())
""";

        legacyJdbcTemplate.update(sql, item.orderId(), item.newStatus().name());
    }

    private void updateSettlementDate(OrderUpdateResult item) {
        String sql =
                """
                UPDATE settlement
                SET update_operator = 'BATCH',
                    update_date = NOW(),
                    expected_settlement_date = CASE
                        WHEN DAYOFWEEK(DATE_ADD(NOW(), INTERVAL 14 DAY)) = 7
                            THEN DATE_ADD(NOW(), INTERVAL 13 DAY)
                        WHEN DAYOFWEEK(DATE_ADD(NOW(), INTERVAL 14 DAY)) = 1
                            THEN DATE_ADD(NOW(), INTERVAL 12 DAY)
                        ELSE DATE_ADD(NOW(), INTERVAL 14 DAY)
                    END
                WHERE order_id = ?
                """;

        legacyJdbcTemplate.update(sql, item.orderId());
    }
}
