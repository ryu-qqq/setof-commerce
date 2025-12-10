package com.ryuqq.setof.domain.member.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InactiveMemberException")
class InactiveMemberExceptionTest {

    @Test
    @DisplayName("memberId로 예외 생성")
    void shouldCreateExceptionWithMemberId() {
        UUID memberId = UUID.randomUUID();
        InactiveMemberException exception = new InactiveMemberException(memberId);
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains(memberId.toString()));
    }

    @Test
    @DisplayName("memberId와 상태로 예외 생성")
    void shouldCreateExceptionWithMemberIdAndStatus() {
        UUID memberId = UUID.randomUUID();
        InactiveMemberException exception = new InactiveMemberException(memberId, "SUSPENDED");
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains(memberId.toString()));
        assertTrue(exception.getMessage().contains("SUSPENDED"));
    }
}
