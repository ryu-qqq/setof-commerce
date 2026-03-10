package com.ryuqq.setof.domain.mileage.vo;

/** 마일리지 적립 건 상태. */
public enum MileageEntryStatus {

    /** 활성 (사용 가능). */
    ACTIVE,

    /** 전액 사용 완료. */
    EXHAUSTED,

    /** 만료됨. */
    EXPIRED,

    /** 회수됨 (주문 취소로 인한 적립분 회수). */
    REVOKED
}
