package com.ryuqq.setof.adapter.out.sqs.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.adapter.out.sqs.config.SqsClientProperties;
import com.ryuqq.setof.adapter.out.sqs.exception.SqsPublishException;
import com.ryuqq.setof.application.discount.dto.messaging.DiscountOutboxPayload;
import com.ryuqq.setof.application.discount.port.out.client.DiscountOutboxMessageClient;
import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.services.sqs.model.SqsException;

/**
 * 할인 아웃박스 SQS 발행 어댑터.
 *
 * <p>crawlinghub CrawlTaskSqsAdapter 패턴을 따릅니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(prefix = "sqs.queues", name = "discount-outbox")
public class DiscountOutboxSqsAdapter implements DiscountOutboxMessageClient {

    private static final Logger log = LoggerFactory.getLogger(DiscountOutboxSqsAdapter.class);

    private final SqsClient sqsClient;
    private final String queueUrl;
    private final ObjectMapper objectMapper;

    public DiscountOutboxSqsAdapter(
            SqsClient sqsClient, SqsClientProperties properties, ObjectMapper objectMapper) {
        this.sqsClient = sqsClient;
        this.queueUrl = properties.getQueues().getDiscountOutbox();
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(DiscountOutbox outbox) {
        String payload = buildPayload(outbox);
        String messageGroupId = "discount-outbox-" + outbox.targetKeyValue();
        String deduplicationId = "outbox-" + outbox.idValue();

        SendMessageRequest.Builder requestBuilder =
                SendMessageRequest.builder().queueUrl(queueUrl).messageBody(payload);

        if (isFifoQueue()) {
            requestBuilder.messageDeduplicationId(deduplicationId).messageGroupId(messageGroupId);
        }

        try {
            SendMessageResponse response = sqsClient.sendMessage(requestBuilder.build());

            log.debug(
                    "SQS 메시지 발행 성공: outboxId={}, messageId={}, target={}",
                    outbox.idValue(),
                    response.messageId(),
                    outbox.targetKeyValue());
        } catch (SqsException e) {
            log.error(
                    "SQS 메시지 발행 실패: outboxId={}, target={}, error={}",
                    outbox.idValue(),
                    outbox.targetKeyValue(),
                    e.getMessage());
            throw new SqsPublishException("SQS 메시지 발행 실패", e);
        }
    }

    private String buildPayload(DiscountOutbox outbox) {
        try {
            DiscountOutboxPayload payload = DiscountOutboxPayload.from(outbox);
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
