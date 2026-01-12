package com.ryuqq.setof.batch.legacy.notification;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.batch.legacy.notification.client.NhnCloudAlimTalkClient;
import com.ryuqq.setof.batch.legacy.notification.dto.AlimTalkSendResult;
import com.ryuqq.setof.batch.legacy.notification.dto.MessageQueueItem;
import com.ryuqq.setof.batch.legacy.notification.enums.AlimTalkTemplateCode;
import com.ryuqq.setof.batch.legacy.notification.enums.MessageStatus;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
 * 알림톡 발송 배치 Job 설정
 *
 * <p>PENDING 상태의 메시지를 조회하여 NHN Cloud API로 발송
 *
 * <p>스케줄: 5분마다
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
public class AlimTalkNotifyJobConfig {

    private static final Logger log = LoggerFactory.getLogger(AlimTalkNotifyJobConfig.class);

    private final DataSource legacyDataSource;
    private final NamedParameterJdbcTemplate legacyJdbcTemplate;
    private final NhnCloudAlimTalkClient alimTalkClient;
    private final ObjectMapper objectMapper;

    @Value("${batch.chunk-size:100}")
    private int chunkSize;

    @Value("${batch.skip-limit:10}")
    private int skipLimit;

    public AlimTalkNotifyJobConfig(
            @Qualifier("legacyDataSource") DataSource legacyDataSource,
            @Qualifier("legacyNamedJdbcTemplate") NamedParameterJdbcTemplate legacyJdbcTemplate,
            NhnCloudAlimTalkClient alimTalkClient,
            ObjectMapper objectMapper) {
        this.legacyDataSource = legacyDataSource;
        this.legacyJdbcTemplate = legacyJdbcTemplate;
        this.alimTalkClient = alimTalkClient;
        this.objectMapper = objectMapper;
    }

    @Bean
    public Job alimTalkNotifyJob(
            JobRepository jobRepository, @Qualifier("alimTalkNotifyStep") Step alimTalkNotifyStep) {
        return new JobBuilder("alimTalkNotifyJob", jobRepository).start(alimTalkNotifyStep).build();
    }

    @Bean
    public Step alimTalkNotifyStep(
            JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("alimTalkNotifyStep", jobRepository)
                .<MessageQueueItem, AlimTalkSendResult>chunk(chunkSize, transactionManager)
                .reader(pendingMessageReader())
                .processor(alimTalkSendProcessor())
                .writer(messageStatusWriter())
                .faultTolerant()
                .skipLimit(skipLimit)
                .skip(Exception.class)
                .build();
    }

    @Bean
    public JdbcPagingItemReader<MessageQueueItem> pendingMessageReader() {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("message_id", Order.ASCENDING);

        return new JdbcPagingItemReaderBuilder<MessageQueueItem>()
                .name("pendingMessageReader")
                .dataSource(legacyDataSource)
                .selectClause("SELECT message_id, TEMPLATE_CODE, PARAMETERS, STATUS, INSERT_DATE")
                .fromClause("FROM message_queue")
                .whereClause("WHERE STATUS = :status AND delete_yn = 'N'")
                .parameterValues(Map.of("status", MessageStatus.PENDING.getValue()))
                .sortKeys(sortKeys)
                .pageSize(chunkSize)
                .rowMapper(this::mapToMessageQueueItem)
                .build();
    }

    private MessageQueueItem mapToMessageQueueItem(ResultSet rs, int rowNum) throws SQLException {
        String parameters = rs.getString("PARAMETERS");
        String phoneNumber = extractPhoneNumber(parameters);
        String templateCode = rs.getString("TEMPLATE_CODE");

        Timestamp insertDate = rs.getTimestamp("INSERT_DATE");
        LocalDateTime createdAt =
                insertDate != null ? insertDate.toLocalDateTime() : LocalDateTime.now();

        return new MessageQueueItem(
                rs.getLong("message_id"),
                phoneNumber,
                AlimTalkTemplateCode.valueOf(templateCode),
                parameters,
                MessageStatus.fromValue(rs.getString("STATUS")),
                createdAt);
    }

    private String extractPhoneNumber(String parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return null;
        }
        try {
            JsonNode node = objectMapper.readTree(parameters);
            JsonNode recipientNo = node.get("recipientNo");
            return recipientNo != null ? recipientNo.asText() : null;
        } catch (Exception e) {
            log.warn("Failed to parse PARAMETERS JSON: {}", parameters, e);
            return null;
        }
    }

    @Bean
    public ItemProcessor<MessageQueueItem, AlimTalkSendResult> alimTalkSendProcessor() {
        return item -> {
            log.debug("Processing message: messageId={}", item.messageId());
            return alimTalkClient.sendAlimTalk(item);
        };
    }

    @Bean
    public ItemWriter<AlimTalkSendResult> messageStatusWriter() {
        return results -> {
            String sql =
                    """
                    UPDATE message_queue
                    SET STATUS = :status,
                        UPDATE_DATE = :updateDate
                    WHERE message_id = :messageId
                    """;

            for (AlimTalkSendResult result : results) {
                Map<String, Object> params = new HashMap<>();
                params.put("messageId", result.messageId());
                params.put("status", result.status().getValue());
                params.put("updateDate", LocalDateTime.now());

                legacyJdbcTemplate.update(sql, params);

                if (result.isSuccess()) {
                    log.info("Message sent successfully: messageId={}", result.messageId());
                } else {
                    log.warn(
                            "Message send failed: messageId={}, code={}, message={}",
                            result.messageId(),
                            result.responseCode(),
                            result.responseMessage());
                }
            }
        };
    }
}
