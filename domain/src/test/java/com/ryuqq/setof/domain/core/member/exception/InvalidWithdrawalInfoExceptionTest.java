package com.ryuqq.setof.domain.core.member.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InvalidWithdrawalInfoException")
class InvalidWithdrawalInfoExceptionTest {

    @Test
    @DisplayName("기본 생성자로 예외 생성")
    void shouldCreateExceptionWithDefaultMessage() {
        InvalidWithdrawalInfoException exception = new InvalidWithdrawalInfoException();
        assertEquals("탈퇴 정보가 올바르지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("커스텀 메시지로 예외 생성")
    void shouldCreateExceptionWithCustomMessage() {
        InvalidWithdrawalInfoException exception =
                new InvalidWithdrawalInfoException("탈퇴 사유는 필수입니다.");
        assertEquals("탈퇴 사유는 필수입니다.", exception.getMessage());
    }
}
