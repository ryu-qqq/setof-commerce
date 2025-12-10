package com.ryuqq.setof.domain.member.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AlreadyKakaoMemberException")
class AlreadyKakaoMemberExceptionTest {

    @Test
    @DisplayName("memberId로 예외 생성")
    void shouldCreateExceptionWithMemberId() {
        UUID memberId = UUID.randomUUID();
        AlreadyKakaoMemberException exception = new AlreadyKakaoMemberException(memberId);
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains(memberId.toString()));
    }

    @Test
    @DisplayName("이메일로 예외 생성")
    void shouldCreateExceptionWithEmail() {
        AlreadyKakaoMemberException exception = new AlreadyKakaoMemberException("test@example.com");
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("test@example.com"));
    }
}
