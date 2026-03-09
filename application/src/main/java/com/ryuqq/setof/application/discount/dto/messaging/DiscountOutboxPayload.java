package com.ryuqq.setof.application.discount.dto.messaging;

import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;

/**
 * SQS 메시지 페이로드.
 *
 * <p>Scheduler가 Outbox를 SQS로 발행할 때 사용하는 DTO입니다. Consumer가 수신하여 가격 재계산을 수행합니다.
 *
 * @param outboxId 아웃박스 ID
 * @param targetType 대상 유형 (BRAND, SELLER, CATEGORY, PRODUCT)
 * @param targetId 대상 ID
 */
public record DiscountOutboxPayload(long outboxId, String targetType, long targetId) {

    public static DiscountOutboxPayload from(DiscountOutbox outbox) {
        return new DiscountOutboxPayload(
                outbox.idValue(), outbox.targetType().name(), outbox.targetId());
    }
}
