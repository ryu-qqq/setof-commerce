package com.ryuqq.setof.domain.notification.vo;

/**
 * 알림 발송 트리거 이벤트 유형.
 *
 * <p>Consumer가 이 값을 보고 어떤 템플릿으로 메시지를 조립할지 결정합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum NotificationEventType {

    // ========== 주문 ==========

    /** 주문 수락 (셀러에게) */
    ORDER_ACCEPTED,

    /** 주문 완료 (고객에게) */
    ORDER_COMPLETED,

    /** 배송 시작 (고객에게) */
    DELIVERY_STARTED,

    // ========== 취소 ==========

    /** 취소 요청 (고객에게) */
    CANCEL_REQUESTED,

    /** 취소 요청 (셀러에게) */
    CANCEL_REQUESTED_TO_SELLER,

    /** 자동 취소 (24시간 미처리) */
    CANCEL_AUTO,

    /** 취소 완료 */
    CANCEL_COMPLETED,

    // ========== 반품 ==========

    /** 반품 요청 (고객에게) */
    REFUND_REQUESTED,

    /** 반품 요청 (셀러에게) */
    REFUND_REQUESTED_TO_SELLER,

    /** 반품 거절 (고객에게) */
    REFUND_REJECTED,

    /** 반품 수락 (고객에게) */
    REFUND_ACCEPTED,

    // ========== QNA ==========

    /** 상품 문의 등록 (고객 확인) */
    QNA_PRODUCT_CREATED,

    /** 상품 문의 등록 (셀러에게) */
    QNA_PRODUCT_CREATED_TO_SELLER,

    /** 주문 문의 등록 (고객 확인) */
    QNA_ORDER_CREATED,

    /** 주문 문의 등록 (셀러에게) */
    QNA_ORDER_CREATED_TO_SELLER,

    // ========== 회원 ==========

    /** 회원 가입 환영 */
    MEMBER_JOINED,

    // ========== 마일리지 ==========

    /** 마일리지 만료 임박 */
    MILEAGE_EXPIRING_SOON
}
