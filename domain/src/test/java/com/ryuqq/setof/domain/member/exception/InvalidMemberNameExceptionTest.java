package com.ryuqq.setof.domain.member.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InvalidMemberNameException")
class InvalidMemberNameExceptionTest {

    @Test
    @DisplayName("값으로 예외 생성")
    void shouldCreateExceptionWithValue() {
        InvalidMemberNameException exception = new InvalidMemberNameException("홍");
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("홍"));
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
