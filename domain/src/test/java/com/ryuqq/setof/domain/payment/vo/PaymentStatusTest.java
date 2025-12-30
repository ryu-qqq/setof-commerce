package com.ryuqq.setof.domain.payment.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PaymentStatus VO")
class PaymentStatusTest {

    @Test
    @DisplayName("PENDING 상태에서 승인이 가능하다")
    void pendingCanApprove() {
        assertTrue(PaymentStatus.PENDING.canApprove());
    }

    @Test
    @DisplayName("APPROVED 상태에서 승인이 불가능하다")
    void approvedCannotApprove() {
        assertFalse(PaymentStatus.APPROVED.canApprove());
    }

    @Test
    @DisplayName("APPROVED 상태에서 환불이 가능하다")
    void approvedCanRefund() {
        assertTrue(PaymentStatus.APPROVED.canRefund());
    }

    @Test
    @DisplayName("PARTIAL_REFUNDED 상태에서 추가 환불이 가능하다")
    void partialRefundedCanRefund() {
        assertTrue(PaymentStatus.PARTIAL_REFUNDED.canRefund());
    }

    @Test
    @DisplayName("PENDING 상태에서 환불이 불가능하다")
    void pendingCannotRefund() {
        assertFalse(PaymentStatus.PENDING.canRefund());
    }

    @Test
    @DisplayName("PENDING 상태에서 취소가 가능하다")
    void pendingCanCancel() {
        assertTrue(PaymentStatus.PENDING.canCancel());
    }

    @Test
    @DisplayName("APPROVED 상태에서 취소가 가능하다")
    void approvedCanCancel() {
        assertTrue(PaymentStatus.APPROVED.canCancel());
    }

    @Test
    @DisplayName("PARTIAL_REFUNDED 상태에서 취소가 불가능하다")
    void partialRefundedCannotCancel() {
        assertFalse(PaymentStatus.PARTIAL_REFUNDED.canCancel());
    }

    @Test
    @DisplayName("FULLY_REFUNDED는 Final 상태다")
    void fullyRefundedIsFinal() {
        assertTrue(PaymentStatus.FULLY_REFUNDED.isFinal());
    }

    @Test
    @DisplayName("FAILED는 Final 상태다")
    void failedIsFinal() {
        assertTrue(PaymentStatus.FAILED.isFinal());
    }

    @Test
    @DisplayName("CANCELLED는 Final 상태다")
    void cancelledIsFinal() {
        assertTrue(PaymentStatus.CANCELLED.isFinal());
    }

    @Test
    @DisplayName("PENDING은 Final 상태가 아니다")
    void pendingIsNotFinal() {
        assertFalse(PaymentStatus.PENDING.isFinal());
    }

    @Test
    @DisplayName("APPROVED는 성공 상태다")
    void approvedIsSuccess() {
        assertTrue(PaymentStatus.APPROVED.isSuccess());
    }

    @Test
    @DisplayName("PARTIAL_REFUNDED는 성공 상태다")
    void partialRefundedIsSuccess() {
        assertTrue(PaymentStatus.PARTIAL_REFUNDED.isSuccess());
    }

    @Test
    @DisplayName("PENDING은 성공 상태가 아니다")
    void pendingIsNotSuccess() {
        assertFalse(PaymentStatus.PENDING.isSuccess());
    }

    @Test
    @DisplayName("defaultStatus는 PENDING을 반환한다")
    void defaultStatusIsPending() {
        assertEquals(PaymentStatus.PENDING, PaymentStatus.defaultStatus());
    }
}
