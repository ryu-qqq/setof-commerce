package com.ryuqq.setof.domain.member.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AlreadyWithdrawnMemberException")
class AlreadyWithdrawnMemberExceptionTest {

    @Test
    @DisplayName("memberId로 예외 생성")
    void shouldCreateExceptionWithMemberId() {
        UUID memberId = UUID.randomUUID();
        AlreadyWithdrawnMemberException exception = new AlreadyWithdrawnMemberException(memberId);
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains(memberId.toString()));
    }
}
