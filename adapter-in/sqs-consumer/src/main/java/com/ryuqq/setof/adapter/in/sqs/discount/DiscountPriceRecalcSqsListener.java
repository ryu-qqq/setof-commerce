package com.ryuqq.setof.adapter.in.sqs.discount;

import com.ryuqq.setof.application.discount.dto.messaging.DiscountOutboxPayload;
import com.ryuqq.setof.application.discount.port.in.command.RecalculateDiscountPriceUseCase;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Discount Outbox SQS 리스너.
 *
 * <p>SQS 큐에서 Discount Outbox 메시지를 수신하여 가격 재계산을 수행합니다.
 *
 * <p><strong>처리 흐름</strong>:
 *
 * <ol>
 *   <li>SQS 큐에서 메시지 수신
 *   <li>Payload에서 outboxId 추출 (ListenerMapper)
 *   <li>RecalculateDiscountPriceUseCase 호출 (Application Layer)
 * </ol>
 *
 * <p><strong>에러 처리</strong>:
 *
 * <ul>
 *   <li>모든 예외를 삼키고 ACK 처리합니다 (DLQ 미사용).
 *   <li>Outbox 상태 관리는 Application Layer에서 수행합니다.
 *   <li>PUBLISHED 상태에서 멈춘 Outbox는 RecoverStuckDiscountOutbox 스케줄러가 복구합니다.
 * </ul>
 *
 * <p><strong>멱등성</strong>: Application Layer에서 Outbox 상태 체크로 보장합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "aws.sqs.listener.discount-outbox-listener-enabled",
        havingValue = "true",
        matchIfMissing = true)
public class DiscountPriceRecalcSqsListener {

    private static final Logger log = LoggerFactory.getLogger(DiscountPriceRecalcSqsListener.class);

    private final DiscountOutboxListenerMapper mapper;
    private final RecalculateDiscountPriceUseCase recalculateDiscountPriceUseCase;

    public DiscountPriceRecalcSqsListener(
            DiscountOutboxListenerMapper mapper,
            RecalculateDiscountPriceUseCase recalculateDiscountPriceUseCase) {
        this.mapper = mapper;
        this.recalculateDiscountPriceUseCase = recalculateDiscountPriceUseCase;
    }

    /**
     * Discount Outbox 메시지 수신 및 처리.
     *
     * <p>DLQ를 사용하지 않으므로 모든 예외를 삼키고 ACK 처리합니다. 실패한 Outbox는 PUBLISHED 상태로 남아있다가
     * RecoverStuckDiscountOutbox 스케줄러가 PENDING으로 복구하여 재시도합니다.
     *
     * @param payload Discount Outbox 페이로드
     */
    @SqsListener("${aws.sqs.listener.discount-outbox-queue-url}")
    public void handleMessage(@Payload DiscountOutboxPayload payload) {
        long outboxId = mapper.toOutboxId(payload);

        log.debug(
                "Discount Outbox 메시지 수신: outboxId={}, targetType={}, targetId={}",
                outboxId,
                payload.targetType(),
                payload.targetId());

        try {
            recalculateDiscountPriceUseCase.execute(outboxId);
            log.info(
                    "Discount 가격 재계산 완료: outboxId={}, targetType={}, targetId={}",
                    outboxId,
                    payload.targetType(),
                    payload.targetId());
        } catch (Exception e) {
            log.error(
                    "Discount 가격 재계산 실패: outboxId={}, targetType={}, targetId={}, error={}",
                    outboxId,
                    payload.targetType(),
                    payload.targetId(),
                    e.getMessage(),
                    e);
            // DLQ 미사용 - 예외를 삼키고 ACK 처리
            // PUBLISHED 상태의 Outbox는 RecoverStuckDiscountOutbox 스케줄러가 복구
        }
    }
}
