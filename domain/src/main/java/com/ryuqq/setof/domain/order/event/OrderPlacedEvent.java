package com.ryuqq.setof.domain.order.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.common.vo.LegacyUserId;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import java.time.Instant;

/**
 * 주문 확정 이벤트.
 *
 * <p>결제 완료 후 주문이 확정(PLACED)되었을 때 발행됩니다.
 *
 * @param orderId 주문 ID
 * @param memberId 회원 ID
 * @param legacyUserId 레거시 사용자 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record OrderPlacedEvent(
        LegacyOrderId orderId, MemberId memberId, LegacyUserId legacyUserId, Instant occurredAt)
        implements DomainEvent {

    public static OrderPlacedEvent of(
            LegacyOrderId orderId, MemberId memberId, LegacyUserId legacyUserId, Instant now) {
        return new OrderPlacedEvent(orderId, memberId, legacyUserId, now);
    }

    public Long orderIdValue() {
        return orderId.value();
    }

    public String memberIdValue() {
        return memberId != null ? memberId.value() : null;
    }

    public long legacyUserIdValue() {
        return legacyUserId.value();
    }
}
