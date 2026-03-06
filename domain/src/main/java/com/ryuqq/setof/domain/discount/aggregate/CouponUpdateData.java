package com.ryuqq.setof.domain.discount.aggregate;

import com.ryuqq.setof.domain.discount.vo.CouponCode;
import com.ryuqq.setof.domain.discount.vo.CouponName;
import com.ryuqq.setof.domain.discount.vo.DiscountPeriod;
import com.ryuqq.setof.domain.discount.vo.IssuanceLimit;

/**
 * 쿠폰 수정 데이터 Value Object.
 *
 * <p>Coupon Aggregate의 update() 메서드에 전달할 수정 데이터를 묶는 역할을 합니다.
 */
public record CouponUpdateData(
        CouponName couponName,
        String description,
        CouponCode couponCode,
        IssuanceLimit issuanceLimit,
        DiscountPeriod usagePeriod) {

    public static CouponUpdateData of(
            CouponName couponName,
            String description,
            CouponCode couponCode,
            IssuanceLimit issuanceLimit,
            DiscountPeriod usagePeriod) {
        return new CouponUpdateData(
                couponName, description, couponCode, issuanceLimit, usagePeriod);
    }
}
