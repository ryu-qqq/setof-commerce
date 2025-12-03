package com.ryuqq.setof.domain.core.member.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InvalidSocialIdException")
class InvalidSocialIdExceptionTest {

    @Test
    @DisplayName("기본 생성자로 예외 생성")
    void shouldCreateExceptionWithDefaultMessage() {
        InvalidSocialIdException exception = new InvalidSocialIdException();
        assertEquals("소셜 ID가 올바르지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("커스텀 메시지로 예외 생성")
    void shouldCreateExceptionWithCustomMessage() {
        InvalidSocialIdException exception = new InvalidSocialIdException("커스텀 메시지");
        assertEquals("커스텀 메시지", exception.getMessage());
    }
}
