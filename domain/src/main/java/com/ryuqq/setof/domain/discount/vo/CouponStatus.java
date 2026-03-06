package com.ryuqq.setof.domain.discount.vo;

/** 발급된 쿠폰 상태. */
public enum CouponStatus {

    /** 발급됨 (사용 가능) */
    ISSUED,

    /** 사용됨 */
    USED,

    /** 만료됨 */
    EXPIRED,

    /** 취소됨 */
    CANCELLED
}
