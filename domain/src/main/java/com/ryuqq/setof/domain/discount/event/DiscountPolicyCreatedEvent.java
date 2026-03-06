package com.ryuqq.setof.domain.discount.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.discount.id.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.vo.ApplicationType;
import com.ryuqq.setof.domain.discount.vo.StackingGroup;
import java.time.Instant;

/**
 * 할인 정책 생성 이벤트.
 *
 * <p>새 할인 정책이 생성되었을 때 발행됩니다.
 *
 * @param discountPolicyId 할인 정책 ID
 * @param applicationType 적용 방식
 * @param stackingGroup 스태킹 그룹
 * @param occurredAt 이벤트 발생 시각
 */
public record DiscountPolicyCreatedEvent(
        DiscountPolicyId discountPolicyId,
        ApplicationType applicationType,
        StackingGroup stackingGroup,
        Instant occurredAt)
        implements DomainEvent {

    public static DiscountPolicyCreatedEvent of(
            DiscountPolicyId discountPolicyId,
            ApplicationType applicationType,
            StackingGroup stackingGroup,
            Instant now) {
        return new DiscountPolicyCreatedEvent(
                discountPolicyId, applicationType, stackingGroup, now);
    }

    public Long discountPolicyIdValue() {
        return discountPolicyId.value();
    }
}
