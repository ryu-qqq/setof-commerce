package com.ryuqq.setof.batch.legacy.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.batch.legacy.notification.dto.ScheduledCancelOrder;
import com.ryuqq.setof.batch.legacy.notification.dto.VBankExpiryOrder;
import com.ryuqq.setof.batch.legacy.notification.enums.AlimTalkTemplateCode;
import com.ryuqq.setof.batch.legacy.notification.enums.MessageStatus;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 알림톡 메시지 생성 배치 Job 설정
 *
 * <p>1. 자동 취소 알림 스케줄 Job (매시 실행)
 *
 * <p>- CANCEL_REQUEST 상태로 24-23시간 경과한 주문에 대해 알림 생성
 *
 * <p>2. 가상계좌 만료 알림 스케줄 Job (15분마다 실행)
 *
 * <p>- 가상계좌 결제 대기 중 1시간 내 만료 예정 주문에 대해 알림 생성
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
public class ScheduleAlimTalkJobConfig {

    private static final Logger log = LoggerFactory.getLogger(ScheduleAlimTalkJobConfig.class);
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final DataSource legacyDataSource;
    private final NamedParameterJdbcTemplate legacyJdbcTemplate;
    private final ObjectMapper objectMapper;

    @Value("${batch.chunk-size:100}")
    private int chunkSize;

    @Value("${batch.skip-limit:10}")
    private int skipLimit;

    public ScheduleAlimTalkJobConfig(
            @Qualifier("legacyDataSource") DataSource legacyDataSource,
            @Qualifier("legacyNamedJdbcTemplate") NamedParameterJdbcTemplate legacyJdbcTemplate,
            ObjectMapper objectMapper) {
        this.legacyDataSource = legacyDataSource;
        this.legacyJdbcTemplate = legacyJdbcTemplate;
        this.objectMapper = objectMapper;
    }

    // ========================================
    // 자동 취소 알림 스케줄 Job
    // ========================================

    @Bean
    public Job scheduleAutoCancelJob(
            JobRepository jobRepository,
            @Qualifier("scheduleAutoCancelStep") Step scheduleAutoCancelStep) {
        return new JobBuilder("scheduleAutoCancelJob", jobRepository)
                .start(scheduleAutoCancelStep)
                .build();
    }

    @Bean
    public Step scheduleAutoCancelStep(
            JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("scheduleAutoCancelStep", jobRepository)
                .<ScheduledCancelOrder, Map<String, Object>>chunk(chunkSize, transactionManager)
                .reader(cancelRequestOrderReader())
                .processor(cancelOrderMessageProcessor())
                .writer(messageQueueWriter())
                .faultTolerant()
                .skipLimit(skipLimit)
                .skip(Exception.class)
                .build();
    }

    @Bean
    public JdbcPagingItemReader<ScheduledCancelOrder> cancelRequestOrderReader() {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("order_id", Order.ASCENDING);

        // 24-23시간 경과한 CANCEL_REQUEST 주문 조회
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusHours(24);
        LocalDateTime endTime = now.minusHours(23);

        return new JdbcPagingItemReaderBuilder<ScheduledCancelOrder>()
                .name("cancelRequestOrderReader")
                .dataSource(legacyDataSource)
                .selectClause(
                        """
                        SELECT o.order_id, u.phone_number, o.order_number,
                               op.product_name, o.cancel_request_date
                        """)
                .fromClause(
                        """
                        FROM orders o
                        JOIN users u ON o.user_id = u.user_id
                        JOIN order_products op ON o.order_id = op.order_id
                        """)
                .whereClause(
                        """
                        WHERE o.order_status = 'CANCEL_REQUEST'
                          AND o.cancel_request_date BETWEEN :startTime AND :endTime
                          AND NOT EXISTS (
                              SELECT 1 FROM message_queue mq
                              WHERE mq.order_id = o.order_id
                                AND mq.template_code = :templateCode
                          )
                        """)
                .parameterValues(
                        Map.of(
                                "startTime", startTime,
                                "endTime", endTime,
                                "templateCode", AlimTalkTemplateCode.CANCEL_ORDER_AUTO.name()))
                .sortKeys(sortKeys)
                .pageSize(chunkSize)
                .rowMapper(this::mapToScheduledCancelOrder)
                .build();
    }

    private ScheduledCancelOrder mapToScheduledCancelOrder(ResultSet rs, int rowNum)
            throws SQLException {
        return new ScheduledCancelOrder(
                rs.getLong("order_id"),
                rs.getString("phone_number"),
                rs.getString("order_number"),
                rs.getString("product_name"),
                rs.getTimestamp("cancel_request_date").toLocalDateTime());
    }

    @Bean
    public ItemProcessor<ScheduledCancelOrder, Map<String, Object>> cancelOrderMessageProcessor() {
        return order -> {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("orderId", order.orderId());
            messageData.put("phoneNumber", order.phoneNumber());
            messageData.put("templateCode", AlimTalkTemplateCode.CANCEL_ORDER_AUTO.name());

            // 템플릿 변수 생성
            Map<String, String> templateVariables = new HashMap<>();
            templateVariables.put("orderNumber", order.orderNumber());
            templateVariables.put("productName", order.productName());
            templateVariables.put(
                    "cancelRequestDate", order.cancelRequestDate().format(DATE_FORMATTER));

            try {
                messageData.put(
                        "templateVariables", objectMapper.writeValueAsString(templateVariables));
            } catch (JsonProcessingException e) {
                log.error(
                        "Failed to serialize template variables for order: {}", order.orderId(), e);
                messageData.put("templateVariables", "{}");
            }

            log.debug("Created cancel order notification: orderId={}", order.orderId());
            return messageData;
        };
    }

    // ========================================
    // 가상계좌 만료 알림 스케줄 Job
    // ========================================

    @Bean
    public Job scheduleVBankCancelJob(
            JobRepository jobRepository,
            @Qualifier("scheduleVBankCancelStep") Step scheduleVBankCancelStep) {
        return new JobBuilder("scheduleVBankCancelJob", jobRepository)
                .start(scheduleVBankCancelStep)
                .build();
    }

    @Bean
    public Step scheduleVBankCancelStep(
            JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("scheduleVBankCancelStep", jobRepository)
                .<VBankExpiryOrder, Map<String, Object>>chunk(chunkSize, transactionManager)
                .reader(vbankExpiryOrderReader())
                .processor(vbankExpiryMessageProcessor())
                .writer(messageQueueWriter())
                .faultTolerant()
                .skipLimit(skipLimit)
                .skip(Exception.class)
                .build();
    }

    @Bean
    public JdbcPagingItemReader<VBankExpiryOrder> vbankExpiryOrderReader() {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("order_id", Order.ASCENDING);

        // 1시간 내 만료 예정인 가상계좌 주문 조회
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryLimit = now.plusHours(1);

        return new JdbcPagingItemReaderBuilder<VBankExpiryOrder>()
                .name("vbankExpiryOrderReader")
                .dataSource(legacyDataSource)
                .selectClause(
                        """
                        SELECT o.order_id, u.phone_number, o.order_number,
                               p.bank_name, p.account_number, o.total_amount, p.expiry_date
                        """)
                .fromClause(
                        """
                        FROM orders o
                        JOIN users u ON o.user_id = u.user_id
                        JOIN payments p ON o.order_id = p.order_id
                        """)
                .whereClause(
                        """
                        WHERE o.order_status = 'ORDER_PROCESSING'
                          AND p.payment_method = 'VBANK'
                          AND p.payment_status = 'PENDING'
                          AND p.expiry_date BETWEEN :now AND :expiryLimit
                          AND NOT EXISTS (
                              SELECT 1 FROM message_queue mq
                              WHERE mq.order_id = o.order_id
                                AND mq.template_code = :templateCode
                                AND mq.created_at > :todayStart
                          )
                        """)
                .parameterValues(
                        Map.of(
                                "now",
                                now,
                                "expiryLimit",
                                expiryLimit,
                                "templateCode",
                                AlimTalkTemplateCode.CANCEL_NOTIFY.name(),
                                "todayStart",
                                now.toLocalDate().atStartOfDay()))
                .sortKeys(sortKeys)
                .pageSize(chunkSize)
                .rowMapper(this::mapToVBankExpiryOrder)
                .build();
    }

    private VBankExpiryOrder mapToVBankExpiryOrder(ResultSet rs, int rowNum) throws SQLException {
        return new VBankExpiryOrder(
                rs.getLong("order_id"),
                rs.getString("phone_number"),
                rs.getString("order_number"),
                rs.getString("bank_name"),
                rs.getString("account_number"),
                rs.getInt("total_amount"),
                rs.getTimestamp("expiry_date").toLocalDateTime());
    }

    @Bean
    public ItemProcessor<VBankExpiryOrder, Map<String, Object>> vbankExpiryMessageProcessor() {
        return order -> {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("orderId", order.orderId());
            messageData.put("phoneNumber", order.phoneNumber());
            messageData.put("templateCode", AlimTalkTemplateCode.CANCEL_NOTIFY.name());

            // 템플릿 변수 생성
            Map<String, String> templateVariables = new HashMap<>();
            templateVariables.put("orderNumber", order.orderNumber());
            templateVariables.put("bankName", order.bankName());
            templateVariables.put("accountNumber", order.accountNumber());
            templateVariables.put("totalAmount", String.format("%,d", order.totalAmount()));
            templateVariables.put("expiryDate", order.expiryDate().format(DATE_FORMATTER));

            try {
                messageData.put(
                        "templateVariables", objectMapper.writeValueAsString(templateVariables));
            } catch (JsonProcessingException e) {
                log.error(
                        "Failed to serialize template variables for order: {}", order.orderId(), e);
                messageData.put("templateVariables", "{}");
            }

            log.debug("Created VBank expiry notification: orderId={}", order.orderId());
            return messageData;
        };
    }

    // ========================================
    // 공통 Writer
    // ========================================

    @Bean
    public ItemWriter<Map<String, Object>> messageQueueWriter() {
        return messages -> {
            String sql =
                    """
INSERT INTO message_queue (order_id, phone_number, template_code, template_variables, status, created_at)
VALUES (:orderId, :phoneNumber, :templateCode, :templateVariables, :status, :createdAt)
""";

            for (Map<String, Object> message : messages) {
                Map<String, Object> params = new HashMap<>(message);
                params.put("status", MessageStatus.PENDING.name());
                params.put("createdAt", LocalDateTime.now());

                legacyJdbcTemplate.update(sql, params);
                log.info(
                        "Message queued: orderId={}, templateCode={}",
                        message.get("orderId"),
                        message.get("templateCode"));
            }
        };
    }
}
