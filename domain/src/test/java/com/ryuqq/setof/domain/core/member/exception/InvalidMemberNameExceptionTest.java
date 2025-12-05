package com.ryuqq.setof.domain.core.member.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InvalidMemberNameException")
class InvalidMemberNameExceptionTest {

    @Test
    @DisplayName("기본 생성자로 예외 생성")
    void shouldCreateExceptionWithDefaultMessage() {
        InvalidMemberNameException exception = new InvalidMemberNameException();
        assertEquals("회원 이름이 올바르지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("값과 사유로 예외 생성")
    void shouldCreateExceptionWithValueAndReason() {
        InvalidMemberNameException exception =
                new InvalidMemberNameException("홍", "이름은 2~5자여야 합니다.");
        assertTrue(exception.getMessage().contains("홍"));
        assertTrue(exception.getMessage().contains("이름은 2~5자여야 합니다."));
    }
}
