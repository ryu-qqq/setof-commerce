package com.ryuqq.setof.batch.legacy.shipment;

import com.ryuqq.setof.batch.config.BatchProperties;
import com.ryuqq.setof.batch.legacy.shipment.client.ShipmentTrackerClient;
import com.ryuqq.setof.batch.legacy.shipment.dto.DeliveryCompletedShipment;
import com.ryuqq.setof.batch.legacy.shipment.dto.ShipmentInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 배송 추적 배치 Job 설정
 *
 * <p>배송중인 주문의 실제 배송 상태를 조회하여 배송완료로 업데이트합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
public class TrackingShipmentJobConfig {

    private static final Logger log = LoggerFactory.getLogger(TrackingShipmentJobConfig.class);
    private static final String JOB_NAME = "trackingShipmentJob";
    private static final String STEP_NAME = "trackingShipmentStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource legacyDataSource;
    private final JdbcTemplate legacyJdbcTemplate;
    private final ShipmentTrackerClient shipmentTrackerClient;
    private final BatchProperties batchProperties;

    public TrackingShipmentJobConfig(
            JobRepository jobRepository,
            @Qualifier("legacyTransactionManager") PlatformTransactionManager transactionManager,
            @Qualifier("legacyDataSource") DataSource legacyDataSource,
            @Qualifier("legacyJdbcTemplate") JdbcTemplate legacyJdbcTemplate,
            ShipmentTrackerClient shipmentTrackerClient,
            BatchProperties batchProperties) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.legacyDataSource = legacyDataSource;
        this.legacyJdbcTemplate = legacyJdbcTemplate;
        this.shipmentTrackerClient = shipmentTrackerClient;
        this.batchProperties = batchProperties;
    }

    @Bean
    public Job trackingShipmentJob() {
        return new JobBuilder(JOB_NAME, jobRepository).start(trackingShipmentStep()).build();
    }

    @Bean
    public Step trackingShipmentStep() {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<ShipmentInfo, DeliveryCompletedShipment>chunk(
                        batchProperties.getChunkSize(), transactionManager)
                .reader(shipmentItemReader())
                .processor(shipmentItemProcessor())
                .writer(shipmentItemWriter())
                .faultTolerant()
                .skipLimit(batchProperties.getSkipLimit())
                .skip(Exception.class)
                .retryLimit(batchProperties.getRetryLimit())
                .retry(Exception.class)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<ShipmentInfo> shipmentItemReader() {
        JdbcPagingItemReader<ShipmentInfo> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(legacyDataSource);
        reader.setPageSize(batchProperties.getChunkSize());
        reader.setRowMapper(shipmentRowMapper());
        reader.setQueryProvider(shipmentQueryProvider());

        return reader;
    }

    private MySqlPagingQueryProvider shipmentQueryProvider() {
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("s.order_id, s.invoice_no, s.company_code");
        queryProvider.setFromClause("shipment s INNER JOIN orders o ON s.order_id = o.order_id");
        queryProvider.setWhereClause(
                "o.order_status = 'DELIVERY_PROCESSING' "
                        + "AND s.shipment_type = 'PARCEL_SERVICE' "
                        + "AND s.delivery_status = 'DELIVERY_PROCESSING'");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("s.order_id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return queryProvider;
    }

    private RowMapper<ShipmentInfo> shipmentRowMapper() {
        return new RowMapper<>() {
            @Override
            public ShipmentInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ShipmentInfo(
                        rs.getLong("order_id"),
                        rs.getString("invoice_no"),
                        rs.getString("company_code"));
            }
        };
    }

    @Bean
    public ItemProcessor<ShipmentInfo, DeliveryCompletedShipment> shipmentItemProcessor() {
        return shipmentInfo -> {
            log.debug(
                    "Processing shipment: orderId={}, invoiceNo={}",
                    shipmentInfo.orderId(),
                    shipmentInfo.invoiceNo());

            return shipmentTrackerClient
                    .trackShipment(shipmentInfo)
                    .map(
                            completedDate ->
                                    new DeliveryCompletedShipment(
                                            shipmentInfo.orderId(), completedDate))
                    .orElse(null); // null 반환 시 Writer에서 제외됨
        };
    }

    @Bean
    public ItemWriter<DeliveryCompletedShipment> shipmentItemWriter() {
        return chunk -> {
            var items = chunk.getItems();
            if (items.isEmpty()) {
                return;
            }

            log.info("Updating {} delivery completed shipments", items.size());

            for (DeliveryCompletedShipment item : items) {
                updateShipmentDeliveryCompleted(item);
                updateOrderDeliveryCompleted(item);
                insertOrderHistory(item);
            }

            log.info("Successfully updated {} shipments to DELIVERY_COMPLETED", items.size());
        };
    }

    private void updateShipmentDeliveryCompleted(DeliveryCompletedShipment item) {
        String sql =
                """
                UPDATE shipment
                SET delivery_status = 'DELIVERY_COMPLETED',
                    delivery_date = ?,
                    update_operator = 'BATCH',
                    update_date = NOW()
                WHERE order_id = ?
                """;

        legacyJdbcTemplate.update(
                sql, Timestamp.valueOf(item.deliveryCompletedDate()), item.orderId());
    }

    private void updateOrderDeliveryCompleted(DeliveryCompletedShipment item) {
        String sql =
                """
                UPDATE orders
                SET order_status = 'DELIVERY_COMPLETED',
                    update_operator = 'BATCH',
                    update_date = NOW()
                WHERE order_id = ?
                """;

        legacyJdbcTemplate.update(sql, item.orderId());
    }

    private void insertOrderHistory(DeliveryCompletedShipment item) {
        String sql =
                """
INSERT INTO orders_history (ORDER_ID, ORDER_STATUS, INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE, UPDATE_DATE)
VALUES (?, 'DELIVERY_COMPLETED', 'BATCH', 'BATCH', NOW(), NOW())
""";

        legacyJdbcTemplate.update(sql, item.orderId());
    }
}
