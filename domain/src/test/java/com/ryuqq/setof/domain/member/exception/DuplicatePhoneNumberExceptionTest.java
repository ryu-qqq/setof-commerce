package com.ryuqq.setof.domain.member.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DuplicatePhoneNumberException")
class DuplicatePhoneNumberExceptionTest {

    @Test
    @DisplayName("핸드폰 번호로 예외 생성 - 마스킹된 번호 포함")
    void shouldCreateExceptionWithMaskedPhoneNumber() {
        DuplicatePhoneNumberException exception = new DuplicatePhoneNumberException("01012345678");
        assertNotNull(exception.getMessage());
        // 마스킹된 형태 검증: 010****5678
        assertTrue(exception.getMessage().contains("010****5678"));
    }
}
