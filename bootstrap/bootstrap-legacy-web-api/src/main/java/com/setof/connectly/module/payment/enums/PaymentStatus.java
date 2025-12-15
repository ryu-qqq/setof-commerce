package com.setof.connectly.module.payment.enums;

public enum PaymentStatus {
    PAYMENT_PROCESSING,
    PAYMENT_COMPLETED,
    PAYMENT_FAILED,
    PAYMENT_PARTIAL_REFUNDED,
    PAYMENT_REFUNDED,
    PAYMENT_CANCELLED;

    public boolean isCompleted() {
        return this.equals(PAYMENT_COMPLETED);
    }

    public boolean isPartialCanceled() {
        return this.equals(PAYMENT_PARTIAL_REFUNDED);
    }

    public boolean isCanceled() {
        return this.equals(PAYMENT_CANCELLED);
    }

    public boolean isProcessing() {
        return this.equals(PAYMENT_PROCESSING);
    }
}
