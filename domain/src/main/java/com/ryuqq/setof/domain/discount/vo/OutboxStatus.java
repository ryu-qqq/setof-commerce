package com.ryuqq.setof.domain.discount.vo;

/**
 * 할인 아웃박스 상태.
 *
 * <p>상태 전이:
 *
 * <pre>
 * PENDING → PUBLISHED → COMPLETED
 *                     → FAILED (3회 재시도 후)
 * PENDING → FAILED (SQS 발행 자체 실패 시)
 * </pre>
 */
public enum OutboxStatus {

    /** 대기 중 (스케줄러가 처리 예정) */
    PENDING,

    /** SQS 발행 완료 (Consumer 처리 대기) */
    PUBLISHED,

    /** 처리 완료 */
    COMPLETED,

    /** 처리 실패 */
    FAILED
}
