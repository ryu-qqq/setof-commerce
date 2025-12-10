package com.ryuqq.setof.domain.member.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InvalidWithdrawalInfoException")
class InvalidWithdrawalInfoExceptionTest {

    @Test
    @DisplayName("사유로 예외 생성")
    void shouldCreateExceptionWithReason() {
        InvalidWithdrawalInfoException exception =
                new InvalidWithdrawalInfoException("탈퇴 사유는 필수입니다.");
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("필드와 사유로 예외 생성")
    void shouldCreateExceptionWithFieldAndReason() {
        InvalidWithdrawalInfoException exception =
                new InvalidWithdrawalInfoException("withdrawnAt", "탈퇴 일시는 필수입니다.");
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("withdrawnAt"));
    }
}
