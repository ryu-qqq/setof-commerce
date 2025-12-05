package com.ryuqq.setof.domain.core.member.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** InvalidMemberIdException 테스트 */
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
    @DisplayName("잘못된 UUID로 생성 시 포맷팅된 메시지 반환")
    void shouldHaveFormattedMessageWithInvalidUuid() {
        // Given
        String invalidUuid = "테스트-에러-메시지";

        // When
        InvalidMemberIdException exception = new InvalidMemberIdException(invalidUuid);

        // Then
        assertTrue(exception.getMessage().contains(invalidUuid));
        assertTrue(exception.getMessage().contains("잘못된 회원 ID"));
    }

    @Test
    @DisplayName("잘못된 값으로 생성 시 값 포함 메시지")
    void shouldHaveMessageWithInvalidValue() {
        // Given
        String invalidValue = "not-a-valid-uuid";

        // When
        InvalidMemberIdException exception = new InvalidMemberIdException(invalidValue);

        // Then
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("not-a-valid-uuid"));
    }

    @Test
    @DisplayName("null 값으로 생성 시 null 포함 메시지")
    void shouldHaveMessageWithNullValue() {
        // Given
        String nullValue = null;

        // When
        InvalidMemberIdException exception = new InvalidMemberIdException(nullValue);

        // Then
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("null"));
    }
}
