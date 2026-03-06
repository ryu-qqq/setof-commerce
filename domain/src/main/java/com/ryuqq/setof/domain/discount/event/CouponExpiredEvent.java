package com.ryuqq.setof.domain.discount.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.discount.id.CouponId;
import com.ryuqq.setof.domain.discount.id.IssuedCouponId;
import java.time.Instant;

/**
 * 쿠폰 만료 이벤트.
 *
 * <p>발급된 쿠폰이 만료 처리되었을 때 발행됩니다.
 *
 * @param issuedCouponId 발급된 쿠폰 ID
 * @param couponId 쿠폰 템플릿 ID
 * @param userId 유저 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record CouponExpiredEvent(
        IssuedCouponId issuedCouponId, CouponId couponId, long userId, Instant occurredAt)
        implements DomainEvent {

    public static CouponExpiredEvent of(
            IssuedCouponId issuedCouponId, CouponId couponId, long userId, Instant now) {
        return new CouponExpiredEvent(issuedCouponId, couponId, userId, now);
    }
}
