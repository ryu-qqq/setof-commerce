package com.ryuqq.setof.domain.discount.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.discount.id.CouponId;
import com.ryuqq.setof.domain.discount.id.IssuedCouponId;
import java.time.Instant;

/**
 * 쿠폰 발급 이벤트.
 *
 * <p>유저에게 쿠폰이 발급되었을 때 발행됩니다.
 *
 * @param issuedCouponId 발급된 쿠폰 ID
 * @param couponId 쿠폰 템플릿 ID
 * @param userId 발급 대상 유저 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record CouponIssuedEvent(
        IssuedCouponId issuedCouponId, CouponId couponId, long userId, Instant occurredAt)
        implements DomainEvent {

    public static CouponIssuedEvent of(
            IssuedCouponId issuedCouponId, CouponId couponId, long userId, Instant now) {
        return new CouponIssuedEvent(issuedCouponId, couponId, userId, now);
    }
}
