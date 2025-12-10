package com.ryuqq.setof.domain.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("DomainException")
class DomainExceptionTest {

    // 테스트용 ErrorCode 구현체
    private enum TestErrorCode implements ErrorCode {
        TEST_ERROR("TEST-001", 400, "테스트 에러 메시지"),
        NOT_FOUND_ERROR("TEST-404", 404, "찾을 수 없습니다");

        private final String code;
        private final int httpStatus;
        private final String message;

        TestErrorCode(String code, int httpStatus, String message) {
            this.code = code;
            this.httpStatus = httpStatus;
            this.message = message;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public int getHttpStatus() {
            return httpStatus;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

    // 테스트용 DomainException 구현체
    private static class TestDomainException extends DomainException {
        TestDomainException(ErrorCode errorCode) {
            super(errorCode);
        }

        TestDomainException(ErrorCode errorCode, String message) {
            super(errorCode, message);
        }

        TestDomainException(ErrorCode errorCode, String message, Map<String, Object> args) {
            super(errorCode, message, args);
        }
    }

    @Nested
    @DisplayName("ErrorCode 기반 생성자 테스트")
    class ErrorCodeConstructorTest {

        @Test
        @DisplayName("ErrorCode로 예외 생성")
        void shouldCreateWithErrorCode() {
            // When
            TestDomainException exception = new TestDomainException(TestErrorCode.TEST_ERROR);

            // Then
            assertNotNull(exception);
            assertEquals("TEST-001", exception.code());
            assertEquals(400, exception.httpStatus());
            assertEquals("테스트 에러 메시지", exception.getMessage());
            assertTrue(exception.args().isEmpty());
        }

        @Test
        @DisplayName("ErrorCode 객체 반환")
        void shouldReturnErrorCodeObject() {
            // When
            TestDomainException exception = new TestDomainException(TestErrorCode.NOT_FOUND_ERROR);

            // Then
            assertEquals(TestErrorCode.NOT_FOUND_ERROR, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("ErrorCode + 커스텀 메시지 생성자 테스트")
    class ErrorCodeWithMessageConstructorTest {

        @Test
        @DisplayName("커스텀 메시지로 예외 생성")
        void shouldCreateWithCustomMessage() {
            // Given
            String customMessage = "커스텀 에러 메시지입니다";

            // When
            TestDomainException exception =
                    new TestDomainException(TestErrorCode.TEST_ERROR, customMessage);

            // Then
            assertEquals("TEST-001", exception.code());
            assertEquals(customMessage, exception.getMessage());
            assertTrue(exception.args().isEmpty());
        }
    }

    @Nested
    @DisplayName("ErrorCode + 커스텀 메시지 + args 생성자 테스트")
    class ErrorCodeWithMessageAndArgsConstructorTest {

        @Test
        @DisplayName("args 포함 예외 생성")
        void shouldCreateWithArgs() {
            // Given
            String customMessage = "주문을 찾을 수 없습니다";
            Map<String, Object> args = Map.of("orderId", 12345L, "memberId", 100L);

            // When
            TestDomainException exception =
                    new TestDomainException(TestErrorCode.NOT_FOUND_ERROR, customMessage, args);

            // Then
            assertEquals("TEST-404", exception.code());
            assertEquals(404, exception.httpStatus());
            assertEquals(customMessage, exception.getMessage());
            assertEquals(12345L, exception.args().get("orderId"));
            assertEquals(100L, exception.args().get("memberId"));
        }

        @Test
        @DisplayName("null args는 빈 Map으로 처리")
        void shouldHandleNullArgs() {
            // When
            TestDomainException exception =
                    new TestDomainException(TestErrorCode.TEST_ERROR, "메시지", null);

            // Then
            assertNotNull(exception.args());
            assertTrue(exception.args().isEmpty());
        }

        @Test
        @DisplayName("args는 불변 Map")
        void shouldReturnImmutableArgs() {
            // Given
            Map<String, Object> args = Map.of("key", "value");

            // When
            TestDomainException exception =
                    new TestDomainException(TestErrorCode.TEST_ERROR, "메시지", args);

            // Then
            assertNotNull(exception.args());
            assertEquals("value", exception.args().get("key"));
        }
    }

    @Nested
    @DisplayName("RuntimeException 상속 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("RuntimeException 클래스를 상속")
        void shouldInheritFromRuntimeException() {
            // Then - DomainException 클래스가 RuntimeException을 상속하는지 확인
            assertEquals(RuntimeException.class, DomainException.class.getSuperclass());
        }

        @Test
        @DisplayName("getMessage는 예외 메시지 반환")
        void shouldReturnMessageFromGetMessage() {
            // When
            TestDomainException exception = new TestDomainException(TestErrorCode.TEST_ERROR);

            // Then
            assertEquals("테스트 에러 메시지", exception.getMessage());
        }
    }
}
