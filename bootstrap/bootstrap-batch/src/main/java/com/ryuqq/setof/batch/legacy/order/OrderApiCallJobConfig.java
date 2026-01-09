package com.ryuqq.setof.batch.legacy.order;

import com.ryuqq.setof.batch.config.BatchProperties;
import com.ryuqq.setof.batch.legacy.order.client.OrderApiClient;
import com.ryuqq.setof.batch.legacy.order.strategy.CancelRequestStrategy;
import com.ryuqq.setof.batch.legacy.order.strategy.PaymentFailStrategy;
import com.ryuqq.setof.batch.legacy.order.strategy.VBankFailStrategy;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 주문 API 호출 배치 Job 설정
 *
 * <p>CancelRequest, PaymentFail, VBankFail 주문 처리 (외부 API 호출)
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
public class OrderApiCallJobConfig {

    private static final Logger log = LoggerFactory.getLogger(OrderApiCallJobConfig.class);

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource legacyDataSource;
    private final BatchProperties batchProperties;
    private final OrderApiClient orderApiClient;

    private final CancelRequestStrategy cancelRequestStrategy;
    private final PaymentFailStrategy paymentFailStrategy;
    private final VBankFailStrategy vBankFailStrategy;

    public OrderApiCallJobConfig(
            JobRepository jobRepository,
            @Qualifier("legacyTransactionManager") PlatformTransactionManager transactionManager,
            @Qualifier("legacyDataSource") DataSource legacyDataSource,
            BatchProperties batchProperties,
            OrderApiClient orderApiClient,
            CancelRequestStrategy cancelRequestStrategy,
            PaymentFailStrategy paymentFailStrategy,
            VBankFailStrategy vBankFailStrategy) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.legacyDataSource = legacyDataSource;
        this.batchProperties = batchProperties;
        this.orderApiClient = orderApiClient;
        this.cancelRequestStrategy = cancelRequestStrategy;
        this.paymentFailStrategy = paymentFailStrategy;
        this.vBankFailStrategy = vBankFailStrategy;
    }

    // ========================================
    // Cancel Request Orders Job
    // ========================================

    @Bean
    public Job updateCancelRequestOrdersJob() {
        return new JobBuilder("updateCancelRequestOrdersJob", jobRepository)
                .start(updateCancelRequestOrdersStep())
                .build();
    }

    @Bean
    public Step updateCancelRequestOrdersStep() {
        return new StepBuilder("updateCancelRequestOrdersStep", jobRepository)
                .<Long, Long>chunk(batchProperties.getChunkSize(), transactionManager)
                .reader(cancelRequestOrderIdReader(null, null))
                .writer(cancelRequestOrderWriter())
                .faultTolerant()
                .skipLimit(batchProperties.getSkipLimit())
                .skip(Exception.class)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<Long> cancelRequestOrderIdReader(
            @Value("#{jobParameters['startTime']}") String startTimeStr,
            @Value("#{jobParameters['endTime']}") String endTimeStr) {

        LocalDateTime startTime =
                startTimeStr != null
                        ? LocalDateTime.parse(startTimeStr)
                        : cancelRequestStrategy.getStartTime();
        LocalDateTime endTime =
                endTimeStr != null
                        ? LocalDateTime.parse(endTimeStr)
                        : cancelRequestStrategy.getEndTime();

        JdbcPagingItemReader<Long> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(legacyDataSource);
        reader.setPageSize(batchProperties.getChunkSize());
        reader.setRowMapper((rs, rowNum) -> rs.getLong("order_id"));

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("o.order_id");
        queryProvider.setFromClause("orders o");
        queryProvider.setWhereClause(
                String.format(
                        "o.update_date >= '%s' AND o.update_date <= '%s' AND o.order_status ="
                                + " 'CANCEL_REQUEST'",
                        Timestamp.valueOf(startTime), Timestamp.valueOf(endTime)));

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("o.order_id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setQueryProvider(queryProvider);
        return reader;
    }

    @Bean
    public ItemWriter<Long> cancelRequestOrderWriter() {
        return chunk -> {
            List<Long> orderIds = (List<Long>) chunk.getItems();
            if (orderIds.isEmpty()) {
                return;
            }

            log.info("Processing {} cancel request orders via API", orderIds.size());
            orderApiClient.processCancelOrders(orderIds);
        };
    }

    // ========================================
    // Payment Fail Orders Job
    // ========================================

    @Bean
    public Job updatePaymentFailOrdersJob() {
        return new JobBuilder("updatePaymentFailOrdersJob", jobRepository)
                .start(updatePaymentFailOrdersStep())
                .build();
    }

    @Bean
    public Step updatePaymentFailOrdersStep() {
        return new StepBuilder("updatePaymentFailOrdersStep", jobRepository)
                .<Long, Long>chunk(batchProperties.getChunkSize(), transactionManager)
                .reader(paymentFailOrderIdReader(null, null))
                .writer(paymentFailOrderWriter())
                .faultTolerant()
                .skipLimit(batchProperties.getSkipLimit())
                .skip(Exception.class)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<Long> paymentFailOrderIdReader(
            @Value("#{jobParameters['startTime']}") String startTimeStr,
            @Value("#{jobParameters['endTime']}") String endTimeStr) {

        LocalDateTime startTime =
                startTimeStr != null
                        ? LocalDateTime.parse(startTimeStr)
                        : paymentFailStrategy.getStartTime();
        LocalDateTime endTime =
                endTimeStr != null
                        ? LocalDateTime.parse(endTimeStr)
                        : paymentFailStrategy.getEndTime();

        JdbcPagingItemReader<Long> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(legacyDataSource);
        reader.setPageSize(batchProperties.getChunkSize());
        reader.setRowMapper((rs, rowNum) -> rs.getLong("order_id"));

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("o.order_id");
        queryProvider.setFromClause(
                "orders o INNER JOIN payment_bill pb ON o.payment_id = pb.payment_id");
        queryProvider.setWhereClause(
                String.format(
                        "o.order_status = 'ORDER_PROCESSING' "
                                + "AND pb.payment_method_id NOT IN (4, 5) "
                                + "AND o.update_date >= '%s' AND o.update_date <= '%s'",
                        Timestamp.valueOf(startTime), Timestamp.valueOf(endTime)));

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("o.order_id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setQueryProvider(queryProvider);
        return reader;
    }

    @Bean
    public ItemWriter<Long> paymentFailOrderWriter() {
        return chunk -> {
            List<Long> orderIds = (List<Long>) chunk.getItems();
            if (orderIds.isEmpty()) {
                return;
            }

            log.info("Processing {} payment fail orders via API", orderIds.size());
            orderApiClient.processPaymentFailOrders(orderIds);
        };
    }

    // ========================================
    // VBank Fail Orders Job
    // ========================================

    @Bean
    public Job updateVBankFailOrdersJob() {
        return new JobBuilder("updateVBankFailOrdersJob", jobRepository)
                .start(updateVBankFailOrdersStep())
                .build();
    }

    @Bean
    public Step updateVBankFailOrdersStep() {
        return new StepBuilder("updateVBankFailOrdersStep", jobRepository)
                .<Long, Long>chunk(batchProperties.getChunkSize(), transactionManager)
                .reader(vbankFailOrderIdReader(null, null))
                .writer(vbankFailOrderWriter())
                .faultTolerant()
                .skipLimit(batchProperties.getSkipLimit())
                .skip(Exception.class)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<Long> vbankFailOrderIdReader(
            @Value("#{jobParameters['startTime']}") String startTimeStr,
            @Value("#{jobParameters['endTime']}") String endTimeStr) {

        LocalDateTime startTime =
                startTimeStr != null
                        ? LocalDateTime.parse(startTimeStr)
                        : vBankFailStrategy.getStartTime();
        LocalDateTime endTime =
                endTimeStr != null
                        ? LocalDateTime.parse(endTimeStr)
                        : vBankFailStrategy.getEndTime();

        JdbcPagingItemReader<Long> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(legacyDataSource);
        reader.setPageSize(batchProperties.getChunkSize());
        reader.setRowMapper((rs, rowNum) -> rs.getLong("order_id"));

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("o.order_id");
        queryProvider.setFromClause(
                "orders o INNER JOIN vbank_account va ON o.payment_id = va.payment_id");
        queryProvider.setWhereClause(
                String.format(
                        "o.order_status = 'ORDER_PROCESSING' "
                                + "AND va.vbank_due_date > '%s' AND va.vbank_due_date < '%s'",
                        Timestamp.valueOf(startTime), Timestamp.valueOf(endTime)));

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("o.order_id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setQueryProvider(queryProvider);
        return reader;
    }

    @Bean
    public ItemWriter<Long> vbankFailOrderWriter() {
        return chunk -> {
            List<Long> orderIds = (List<Long>) chunk.getItems();
            if (orderIds.isEmpty()) {
                return;
            }

            log.info("Processing {} vbank fail orders via API", orderIds.size());
            orderApiClient.processPaymentFailOrders(orderIds);
        };
    }
}
