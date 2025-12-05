package com.ryuqq.setof.domain.core.member.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RequiredConsentMissingException")
class RequiredConsentMissingExceptionTest {

    @Test
    @DisplayName("기본 생성자로 예외 생성")
    void shouldCreateExceptionWithDefaultMessage() {
        RequiredConsentMissingException exception = new RequiredConsentMissingException();
        assertEquals("필수 동의 항목이 누락되었습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("동의 타입으로 예외 생성")
    void shouldCreateExceptionWithConsentType() {
        RequiredConsentMissingException exception =
                new RequiredConsentMissingException("개인정보 수집/이용");
        assertTrue(exception.getMessage().contains("개인정보 수집/이용"));
    }
}
