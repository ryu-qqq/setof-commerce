package com.ryuqq.setof.batch.legacy.notification;

import com.ryuqq.setof.batch.legacy.notification.client.NhnCloudAlimTalkClient;
import com.ryuqq.setof.batch.legacy.notification.dto.AlimTalkSendResult;
import com.ryuqq.setof.batch.legacy.notification.dto.MessageQueueItem;
import com.ryuqq.setof.batch.legacy.notification.enums.AlimTalkTemplateCode;
import com.ryuqq.setof.batch.legacy.notification.enums.MessageStatus;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Value("${batch.chunk-size:100}")
    private int chunkSize;

    @Value("${batch.skip-limit:10}")
    private int skipLimit;

    public AlimTalkNotifyJobConfig(
            @Qualifier("legacyDataSource") DataSource legacyDataSource,
            @Qualifier("legacyNamedJdbcTemplate") NamedParameterJdbcTemplate legacyJdbcTemplate,
            NhnCloudAlimTalkClient alimTalkClient) {
        this.legacyDataSource = legacyDataSource;
        this.legacyJdbcTemplate = legacyJdbcTemplate;
        this.alimTalkClient = alimTalkClient;
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
                .selectClause(
                        "SELECT message_id, order_id, phone_number, template_code,"
                                + " template_variables, status, created_at")
                .fromClause("FROM message_queue")
                .whereClause("WHERE status = :status")
                .parameterValues(Map.of("status", MessageStatus.PENDING.name()))
                .sortKeys(sortKeys)
                .pageSize(chunkSize)
                .rowMapper(this::mapToMessageQueueItem)
                .build();
    }

    private MessageQueueItem mapToMessageQueueItem(ResultSet rs, int rowNum) throws SQLException {
        return new MessageQueueItem(
                rs.getLong("message_id"),
                rs.getLong("order_id"),
                rs.getString("phone_number"),
                AlimTalkTemplateCode.valueOf(rs.getString("template_code")),
                rs.getString("template_variables"),
                MessageStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("created_at").toLocalDateTime());
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
                    SET status = :status,
                        response_code = :responseCode,
                        response_message = :responseMessage,
                        sent_at = :sentAt
                    WHERE message_id = :messageId
                    """;

            for (AlimTalkSendResult result : results) {
                Map<String, Object> params = new HashMap<>();
                params.put("messageId", result.messageId());
                params.put("status", result.status().name());
                params.put("responseCode", result.responseCode());
                params.put("responseMessage", result.responseMessage());
                params.put("sentAt", LocalDateTime.now());

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
