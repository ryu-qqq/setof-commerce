package com.ryuqq.setof.domain.discount.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.discount.id.IssuedCouponId;
import java.time.Instant;

/**
 * 쿠폰 사용 이벤트.
 *
 * <p>유저가 쿠폰을 사용하여 주문했을 때 발행됩니다.
 *
 * @param issuedCouponId 발급된 쿠폰 ID
 * @param userId 사용 유저 ID
 * @param orderId 주문 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record CouponUsedEvent(
        IssuedCouponId issuedCouponId, long userId, long orderId, Instant occurredAt)
        implements DomainEvent {

    public static CouponUsedEvent of(
            IssuedCouponId issuedCouponId, long userId, long orderId, Instant now) {
        return new CouponUsedEvent(issuedCouponId, userId, orderId, now);
    }
}
