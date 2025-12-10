package com.ryuqq.setof.domain.member.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InvalidSocialIdException")
class InvalidSocialIdExceptionTest {

    @Test
    @DisplayName("잘못된 값으로 예외 생성")
    void shouldCreateExceptionWithInvalidValue() {
        InvalidSocialIdException exception = new InvalidSocialIdException("invalid");
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("invalid"));
    }

    @Test
    @DisplayName("provider와 잘못된 값으로 예외 생성")
    void shouldCreateExceptionWithProviderAndInvalidValue() {
        InvalidSocialIdException exception = new InvalidSocialIdException("KAKAO", "invalid");
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("KAKAO"));
        assertTrue(exception.getMessage().contains("invalid"));
    }
}
