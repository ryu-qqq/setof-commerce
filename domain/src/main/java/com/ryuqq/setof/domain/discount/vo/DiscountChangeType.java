package com.ryuqq.setof.domain.discount.vo;

/**
 * 할인 정책 변경 유형.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum DiscountChangeType {

    /** 정책 신규 생성 */
    CREATED,

    /** 정책 내용 수정 */
    UPDATED,

    /** 정책 활성화 */
    ACTIVATED,

    /** 정책 비활성화 */
    DEACTIVATED,

    /** 정책 삭제 (Soft Delete) */
    DELETED,

    /** 할인 대상 추가 */
    TARGET_ADDED,

    /** 할인 대상 제거 */
    TARGET_REMOVED
}
