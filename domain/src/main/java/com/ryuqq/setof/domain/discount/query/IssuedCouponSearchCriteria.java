package com.ryuqq.setof.domain.discount.query;

import com.ryuqq.setof.domain.discount.id.CouponId;
import com.ryuqq.setof.domain.discount.vo.CouponStatus;

/**
 * IssuedCoupon 검색 조건 Criteria.
 *
 * <p>발급된 쿠폰 조회 시 사용하는 검색 조건을 정의합니다. 페이징은 cursor 기반이므로 QueryContext를 사용하지 않습니다.
 *
 * @param userId 유저 ID (필수)
 * @param couponId 쿠폰 ID 필터 (nullable)
 * @param status 상태 필터 (nullable)
 * @param size 페이지 크기
 */
public record IssuedCouponSearchCriteria(
        long userId, CouponId couponId, CouponStatus status, int size) {

    public IssuedCouponSearchCriteria {
        if (userId <= 0) {
            throw new IllegalArgumentException("유저 ID는 0보다 커야 합니다");
        }
        if (size <= 0) {
            size = 20;
        }
    }

    public static IssuedCouponSearchCriteria of(long userId, CouponStatus status, int size) {
        return new IssuedCouponSearchCriteria(userId, null, status, size);
    }

    public static IssuedCouponSearchCriteria forUser(long userId) {
        return new IssuedCouponSearchCriteria(userId, null, null, 20);
    }

    /** 쿠폰 ID 값 반환 (편의 메서드) */
    public Long couponIdValue() {
        return couponId != null ? couponId.value() : null;
    }
}
