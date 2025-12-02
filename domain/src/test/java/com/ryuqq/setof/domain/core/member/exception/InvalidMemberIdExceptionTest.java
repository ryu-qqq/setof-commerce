package com.ryuqq.setof.domain.core.member.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * InvalidMemberIdException 테스트
 */
@DisplayName("InvalidMemberIdException")
class InvalidMemberIdExceptionTest {

    @Test
    @DisplayName("기본 생성자 사용 시 기본 메시지 포함")
    void shouldHaveDefaultMessage() {
        // When
        InvalidMemberIdException exception = new InvalidMemberIdException();

        // Then
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("회원 ID"));
    }

    @Test
    @DisplayName("커스텀 메시지로 생성")
    void shouldHaveCustomMessage() {
        // Given
        String customMessage = "테스트 에러 메시지";

        // When
        InvalidMemberIdException exception = new InvalidMemberIdException(customMessage);

        // Then
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    @DisplayName("잘못된 값으로 생성 시 값 포함 메시지")
    void shouldHaveMessageWithInvalidValue() {
        // Given
        Long invalidValue = -5L;

        // When
        InvalidMemberIdException exception = new InvalidMemberIdException(invalidValue);

        // Then
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("-5"));
    }

    @Test
    @DisplayName("null 값으로 생성 시 null 포함 메시지")
    void shouldHaveMessageWithNullValue() {
        // Given
        Long nullValue = null;

        // When
        InvalidMemberIdException exception = new InvalidMemberIdException(nullValue);

        // Then
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("null"));
    }
}
