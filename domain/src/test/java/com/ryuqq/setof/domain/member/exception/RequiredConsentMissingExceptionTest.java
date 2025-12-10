package com.ryuqq.setof.domain.member.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RequiredConsentMissingException")
class RequiredConsentMissingExceptionTest {

    @Test
    @DisplayName("동의 타입으로 예외 생성")
    void shouldCreateExceptionWithConsentType() {
        RequiredConsentMissingException exception =
                new RequiredConsentMissingException("개인정보 수집/이용");
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("개인정보 수집/이용"));
    }
}
