package com.ryuqq.setof.domain.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DomainException 테스트")
class DomainExceptionTest {

    /** 테스트용 ErrorCode 구현 */
    private enum TestErrorCode implements ErrorCode {
        TEST_ERROR("TEST-001", 400, "테스트 에러 메시지");

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

    /** 테스트용 DomainException 구현 */
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

        TestDomainException(ErrorCode errorCode, Throwable cause) {
            super(errorCode, cause);
        }
    }

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ErrorCode만으로 예외를 생성한다")
        void createWithErrorCodeOnly() {
            // when
            TestDomainException exception = new TestDomainException(TestErrorCode.TEST_ERROR);

            // then
            assertThat(exception.getMessage()).isEqualTo("테스트 에러 메시지");
            assertThat(exception.getErrorCode()).isEqualTo(TestErrorCode.TEST_ERROR);
            assertThat(exception.args()).isEmpty();
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            TestDomainException exception =
                    new TestDomainException(TestErrorCode.TEST_ERROR, "커스텀 메시지");

            // then
            assertThat(exception.getMessage()).isEqualTo("커스텀 메시지");
            assertThat(exception.getErrorCode()).isEqualTo(TestErrorCode.TEST_ERROR);
            assertThat(exception.args()).isEmpty();
        }

        @Test
        @DisplayName("ErrorCode, 메시지, 컨텍스트 정보로 예외를 생성한다")
        void createWithErrorCodeMessageAndArgs() {
            // given
            Map<String, Object> args = Map.of("key1", "value1", "key2", 123);

            // when
            TestDomainException exception =
                    new TestDomainException(TestErrorCode.TEST_ERROR, "커스텀 메시지", args);

            // then
            assertThat(exception.getMessage()).isEqualTo("커스텀 메시지");
            assertThat(exception.args()).containsEntry("key1", "value1").containsEntry("key2", 123);
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            TestDomainException exception =
                    new TestDomainException(TestErrorCode.TEST_ERROR, cause);

            // then
            assertThat(exception.getMessage()).isEqualTo("테스트 에러 메시지");
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.args()).isEmpty();
        }

        @Test
        @DisplayName("args가 null인 경우 빈 Map을 반환한다")
        void argsReturnsEmptyMapWhenNull() {
            // when
            TestDomainException exception =
                    new TestDomainException(TestErrorCode.TEST_ERROR, "메시지", null);

            // then
            assertThat(exception.args()).isEmpty();
        }
    }

    @Nested
    @DisplayName("편의 메서드 테스트")
    class ConvenienceMethodTest {

        @Test
        @DisplayName("code()는 에러 코드 문자열을 반환한다")
        void returnsCode() {
            // given
            TestDomainException exception = new TestDomainException(TestErrorCode.TEST_ERROR);

            // then
            assertThat(exception.code()).isEqualTo("TEST-001");
        }

        @Test
        @DisplayName("httpStatus()는 HTTP 상태 코드를 반환한다")
        void returnsHttpStatus() {
            // given
            TestDomainException exception = new TestDomainException(TestErrorCode.TEST_ERROR);

            // then
            assertThat(exception.httpStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("args()는 불변 Map을 반환한다")
        void argsReturnsImmutableMap() {
            // given
            Map<String, Object> args = Map.of("key", "value");
            TestDomainException exception =
                    new TestDomainException(TestErrorCode.TEST_ERROR, "메시지", args);

            // then
            Map<String, Object> returnedArgs = exception.args();
            assertThat(returnedArgs).containsEntry("key", "value");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("RuntimeException을 상속한다")
        void extendsRuntimeException() {
            // given
            TestDomainException exception = new TestDomainException(TestErrorCode.TEST_ERROR);

            // then
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }
    }
}
