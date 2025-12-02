package com.ryuqq.setof.domain.core.member.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AlreadyWithdrawnMemberException")
class AlreadyWithdrawnMemberExceptionTest {

    @Test
    @DisplayName("기본 생성자로 예외 생성")
    void shouldCreateExceptionWithDefaultMessage() {
        AlreadyWithdrawnMemberException exception = new AlreadyWithdrawnMemberException();
        assertEquals("이미 탈퇴한 회원입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("커스텀 메시지로 예외 생성")
    void shouldCreateExceptionWithCustomMessage() {
        AlreadyWithdrawnMemberException exception = new AlreadyWithdrawnMemberException("회원 ID 123은 이미 탈퇴했습니다.");
        assertEquals("회원 ID 123은 이미 탈퇴했습니다.", exception.getMessage());
    }
}
