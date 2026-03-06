package com.ryuqq.setof.domain.discount.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.discount.id.DiscountPolicyId;
import java.time.Instant;

/**
 * 할인 정책 비활성화 이벤트.
 *
 * <p>할인 정책이 비활성화되었을 때 발행됩니다. 관련 캐시 무효화 등에 사용됩니다.
 *
 * @param discountPolicyId 할인 정책 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record DiscountPolicyDeactivatedEvent(DiscountPolicyId discountPolicyId, Instant occurredAt)
        implements DomainEvent {

    public static DiscountPolicyDeactivatedEvent of(
            DiscountPolicyId discountPolicyId, Instant now) {
        return new DiscountPolicyDeactivatedEvent(discountPolicyId, now);
    }

    public Long discountPolicyIdValue() {
        return discountPolicyId.value();
    }
}
