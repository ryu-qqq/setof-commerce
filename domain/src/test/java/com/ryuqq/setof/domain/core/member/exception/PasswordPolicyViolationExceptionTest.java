package com.ryuqq.setof.domain.core.member.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PasswordPolicyViolationException 테스트
 *
 * <p>JaCoCo 커버리지 충족을 위한 예외 클래스 테스트</p>
 */
@DisplayName("PasswordPolicyViolationException")
class PasswordPolicyViolationExceptionTest {

    @Test
    @DisplayName("기본 생성자로 예외 생성 시 기본 메시지 반환")
    void shouldCreateExceptionWithDefaultMessage() {
        // When
        PasswordPolicyViolationException exception = new PasswordPolicyViolationException();

        // Then
        assertNotNull(exception.getMessage());
        assertEquals(
            "비밀번호는 8자 이상이며, 영문 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.",
            exception.getMessage()
        );
    }

    @Test
    @DisplayName("커스텀 메시지로 예외 생성")
    void shouldCreateExceptionWithCustomMessage() {
        // Given
        String customMessage = "비밀번호가 너무 짧습니다.";

        // When
        PasswordPolicyViolationException exception = new PasswordPolicyViolationException(customMessage);

        // Then
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    @DisplayName("RuntimeException 상속 확인")
    void shouldExtendRuntimeException() {
        // When
        PasswordPolicyViolationException exception = new PasswordPolicyViolationException();

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }
}
