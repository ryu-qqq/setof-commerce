package com.ryuqq.setof.domain.mileage.vo;

/** 마일리지 적립 유형. 어떤 행위로 적립되었는지를 나타냅니다. */
public enum MileageIssueType {

    /** 회원가입 적립. */
    JOIN,

    /** 구매확정 적립. */
    ORDER,

    /** 리뷰 작성 적립. */
    REVIEW,

    /** 관리자 수동 지급. */
    ADMIN,

    /** 이벤트/프로모션 적립. */
    EVENT
}
