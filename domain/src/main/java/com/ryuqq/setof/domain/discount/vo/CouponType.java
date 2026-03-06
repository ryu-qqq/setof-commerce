package com.ryuqq.setof.domain.discount.vo;

/** 쿠폰 발급 방식. */
public enum CouponType {

    /** 유저 다운로드 */
    DOWNLOAD,

    /** 조건 충족 시 자동 발급 */
    AUTO_ISSUE,

    /** 코드 입력 */
    CODE
}
