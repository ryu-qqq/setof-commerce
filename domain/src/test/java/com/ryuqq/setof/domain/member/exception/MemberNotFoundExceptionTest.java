package com.ryuqq.setof.domain.member.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MemberNotFoundException")
class MemberNotFoundExceptionTest {

    @Test
    @DisplayName("memberId로 예외 생성")
    void shouldCreateExceptionWithMemberId() {
        UUID memberId = UUID.randomUUID();
        MemberNotFoundException exception = new MemberNotFoundException(memberId);
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains(memberId.toString()));
    }

    @Test
    @DisplayName("이메일로 예외 생성")
    void shouldCreateExceptionWithEmail() {
        MemberNotFoundException exception = new MemberNotFoundException("test@example.com");
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("test@example.com"));
    }
}
