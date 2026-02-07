package com.ryuqq.setof.storage.legacy.payment.entity;

/**
 * LegacyPaymentStatus - 레거시 결제 상태 Enum.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum LegacyPaymentStatus {
    PAYMENT_PROCESSING,
    PAYMENT_COMPLETED,
    PAYMENT_FAILED,
    PAYMENT_PARTIAL_REFUNDED,
    PAYMENT_REFUNDED,
    PAYMENT_CANCELLED
}
