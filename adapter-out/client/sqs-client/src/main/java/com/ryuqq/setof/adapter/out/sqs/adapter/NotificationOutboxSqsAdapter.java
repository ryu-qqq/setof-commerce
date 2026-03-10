package com.ryuqq.setof.adapter.out.sqs.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.adapter.out.sqs.config.SqsClientProperties;
import com.ryuqq.setof.adapter.out.sqs.exception.SqsPublishException;
import com.ryuqq.setof.application.notification.dto.messaging.NotificationOutboxPayload;
import com.ryuqq.setof.application.notification.port.out.client.NotificationOutboxMessageClient;
import com.ryuqq.setof.domain.notification.aggregate.NotificationOutbox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.services.sqs.model.SqsException;

/**
 * 알림 아웃박스 SQS 발행 어댑터.
 *
 * <p>DiscountOutboxSqsAdapter 패턴을 따릅니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(prefix = "sqs.queues", name = "notification-outbox")
public class NotificationOutboxSqsAdapter implements NotificationOutboxMessageClient {

    private static final Logger log = LoggerFactory.getLogger(NotificationOutboxSqsAdapter.class);

    private final SqsClient sqsClient;
    private final String queueUrl;
    private final ObjectMapper objectMapper;

    public NotificationOutboxSqsAdapter(
            SqsClient sqsClient, SqsClientProperties properties, ObjectMapper objectMapper) {
        this.sqsClient = sqsClient;
        this.queueUrl = properties.getQueues().getNotificationOutbox();
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(NotificationOutbox outbox) {
        String payload = buildPayload(outbox);
        String messageGroupId = "notification-outbox-" + outbox.referenceKey();
        String deduplicationId = "notification-outbox-" + outbox.idValue();

        SendMessageRequest.Builder requestBuilder =
                SendMessageRequest.builder().queueUrl(queueUrl).messageBody(payload);

        if (isFifoQueue()) {
            requestBuilder.messageDeduplicationId(deduplicationId).messageGroupId(messageGroupId);
        }

        try {
            SendMessageResponse response = sqsClient.sendMessage(requestBuilder.build());

            log.debug(
                    "알림 아웃박스 SQS 발행 성공: outboxId={}, messageId={}, referenceKey={}",
                    outbox.idValue(),
                    response.messageId(),
                    outbox.referenceKey());
        } catch (SqsException e) {
            log.error(
                    "알림 아웃박스 SQS 발행 실패: outboxId={}, referenceKey={}, error={}",
                    outbox.idValue(),
                    outbox.referenceKey(),
                    e.getMessage());
            throw new SqsPublishException("알림 아웃박스 SQS 발행 실패", e);
        }
    }

    private String buildPayload(NotificationOutbox outbox) {
        try {
            NotificationOutboxPayload payload = NotificationOutboxPayload.from(outbox);
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("페이로드 직렬화 실패: outboxId={}", outbox.idValue());
            throw new SqsPublishException("페이로드 직렬화 실패", e);
        }
    }

    private boolean isFifoQueue() {
        return queueUrl != null && queueUrl.endsWith(".fifo");
    }
}
