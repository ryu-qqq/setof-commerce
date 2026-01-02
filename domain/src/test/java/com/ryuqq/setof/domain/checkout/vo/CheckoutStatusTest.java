package com.ryuqq.setof.domain.checkout.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CheckoutStatus VO")
class CheckoutStatusTest {

    @Test
    @DisplayName("PENDING 상태는 결제 처리 시작이 가능하다")
    void pendingCanStartProcessing() {
        assertTrue(CheckoutStatus.PENDING.canStartProcessing());
    }

    @Test
    @DisplayName("PROCESSING 상태는 결제 처리 시작이 불가능하다")
    void processingCannotStartProcessing() {
        assertFalse(CheckoutStatus.PROCESSING.canStartProcessing());
    }

    @Test
    @DisplayName("PROCESSING 상태는 결제 완료가 가능하다")
    void processingCanComplete() {
        assertTrue(CheckoutStatus.PROCESSING.canComplete());
    }

    @Test
    @DisplayName("PENDING 상태는 결제 완료가 불가능하다")
    void pendingCannotComplete() {
        assertFalse(CheckoutStatus.PENDING.canComplete());
    }

    @Test
    @DisplayName("PENDING 상태는 만료가 가능하다")
    void pendingCanExpire() {
        assertTrue(CheckoutStatus.PENDING.canExpire());
    }

    @Test
    @DisplayName("PROCESSING 상태도 만료가 가능하다")
    void processingCanExpire() {
        assertTrue(CheckoutStatus.PROCESSING.canExpire());
    }

    @Test
    @DisplayName("COMPLETED 상태는 만료가 불가능하다")
    void completedCannotExpire() {
        assertFalse(CheckoutStatus.COMPLETED.canExpire());
    }

    @Test
    @DisplayName("PENDING은 Final 상태가 아니다")
    void pendingIsNotFinal() {
        assertFalse(CheckoutStatus.PENDING.isFinal());
    }

    @Test
    @DisplayName("COMPLETED는 Final 상태다")
    void completedIsFinal() {
        assertTrue(CheckoutStatus.COMPLETED.isFinal());
    }

    @Test
    @DisplayName("EXPIRED는 Final 상태다")
    void expiredIsFinal() {
        assertTrue(CheckoutStatus.EXPIRED.isFinal());
    }

    @Test
    @DisplayName("defaultStatus는 PENDING을 반환한다")
    void defaultStatusIsPending() {
        assertEquals(CheckoutStatus.PENDING, CheckoutStatus.defaultStatus());
    }
}
