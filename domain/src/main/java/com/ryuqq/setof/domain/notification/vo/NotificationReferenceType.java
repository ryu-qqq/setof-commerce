package com.ryuqq.setof.domain.notification.vo;

/**
 * 알림 참조 엔티티 유형.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum NotificationReferenceType {

    /** 주문 */
    ORDER,

    /** 취소 */
    CANCEL,

    /** 반품 */
    REFUND,

    /** QNA */
    QNA,

    /** 회원 */
    MEMBER,

    /** 마일리지 */
    MILEAGE
}
