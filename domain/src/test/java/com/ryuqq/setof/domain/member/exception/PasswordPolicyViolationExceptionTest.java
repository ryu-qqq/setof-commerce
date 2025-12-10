package com.ryuqq.setof.domain.member.exception;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * PasswordPolicyViolationException 테스트
 *
 * <p>JaCoCo 커버리지 충족을 위한 예외 클래스 테스트
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
    }

    @Test
    @DisplayName("위반된 규칙으로 예외 생성")
    void shouldCreateExceptionWithViolatedRule() {
        // Given
        String violatedRule = "비밀번호가 너무 짧습니다.";

        // When
        PasswordPolicyViolationException exception =
                new PasswordPolicyViolationException(violatedRule);

        // Then
        assertNotNull(exception.getMessage());
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
