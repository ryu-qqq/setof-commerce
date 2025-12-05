package com.ryuqq.setof.domain.core.member.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DuplicatePhoneNumberException")
class DuplicatePhoneNumberExceptionTest {

    @Test
    @DisplayName("기본 생성자로 예외 생성")
    void shouldCreateExceptionWithDefaultMessage() {
        DuplicatePhoneNumberException exception = new DuplicatePhoneNumberException();
        assertEquals("이미 등록된 핸드폰 번호입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("커스텀 메시지로 예외 생성")
    void shouldCreateExceptionWithCustomMessage() {
        DuplicatePhoneNumberException exception =
                new DuplicatePhoneNumberException("01012345678은 이미 등록되어 있습니다.");
        assertEquals("01012345678은 이미 등록되어 있습니다.", exception.getMessage());
    }
}
