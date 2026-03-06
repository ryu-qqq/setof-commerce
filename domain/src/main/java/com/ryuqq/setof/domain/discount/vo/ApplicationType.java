package com.ryuqq.setof.domain.discount.vo;

/** 할인 적용 방식. */
public enum ApplicationType {

    /** 즉시할인 (자동 적용) */
    INSTANT,

    /** 쿠폰 할인 (유저 행동 필요) */
    COUPON
}
