package com.ryuqq.setof.domain.notification.vo;

/**
 * 알림 아웃박스 상태.
 *
 * <p>상태 전이:
 *
 * <pre>
 * PENDING → PUBLISHED → COMPLETED
 *                     → FAILED (3회 재시도 후)
 * PENDING → FAILED (SQS 발행 자체 실패 시)
 * </pre>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum NotificationStatus {

    /** 대기 중 (스케줄러가 처리 예정) */
    PENDING,

    /** SQS 발행 완료 (Consumer 처리 대기) */
    PUBLISHED,

    /** 처리 완료 (NHN Cloud 발송 성공) */
    COMPLETED,

    /** 처리 실패 */
    FAILED
}
