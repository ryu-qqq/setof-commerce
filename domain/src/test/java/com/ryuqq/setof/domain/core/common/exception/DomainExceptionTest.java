package com.ryuqq.setof.domain.core.common.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DomainException (Common)")
class DomainExceptionTest {

    @Test
    @DisplayName("코드와 메시지로 예외 생성")
    void shouldCreateWithCodeAndMessage() {
        // Given
        String code = "TEST-001";
        String message = "테스트 에러 메시지";

        // When
        DomainException exception = new TestDomainException(code, message);

        // Then
        assertEquals(code, exception.code());
        assertEquals(message, exception.getMessage());
        assertTrue(exception.args().isEmpty());
    }

    @Test
    @DisplayName("코드, 메시지, args로 예외 생성")
    void shouldCreateWithCodeMessageAndArgs() {
        // Given
        String code = "TEST-002";
        String message = "테스트 에러 메시지";
        Map<String, Object> args = Map.of("key1", "value1", "key2", 123);

        // When
        DomainException exception = new TestDomainException(code, message, args);

        // Then
        assertEquals(code, exception.code());
        assertEquals(message, exception.getMessage());
        assertEquals(2, exception.args().size());
        assertEquals("value1", exception.args().get("key1"));
        assertEquals(123, exception.args().get("key2"));
    }

    @Test
    @DisplayName("null args일 경우 빈 Map 반환")
    void shouldReturnEmptyMapWhenArgsIsNull() {
        // Given
        String code = "TEST-003";
        String message = "테스트 에러 메시지";

        // When
        DomainException exception = new TestDomainException(code, message, null);

        // Then
        assertNotNull(exception.args());
        assertTrue(exception.args().isEmpty());
    }

    /** 테스트용 구체 클래스 */
    private static class TestDomainException extends DomainException {
        TestDomainException(String code, String message) {
            super(code, message);
        }

        TestDomainException(String code, String message, Map<String, Object> args) {
            super(code, message, args);
        }
    }
}
