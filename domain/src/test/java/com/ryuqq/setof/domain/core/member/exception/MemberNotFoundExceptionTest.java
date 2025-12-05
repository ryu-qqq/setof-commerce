package com.ryuqq.setof.domain.core.member.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MemberNotFoundException")
class MemberNotFoundExceptionTest {

    @Test
    @DisplayName("기본 생성자로 예외 생성")
    void shouldCreateExceptionWithDefaultMessage() {
        MemberNotFoundException exception = new MemberNotFoundException();
        assertEquals("회원을 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("커스텀 메시지로 예외 생성")
    void shouldCreateExceptionWithCustomMessage() {
        MemberNotFoundException exception = new MemberNotFoundException("회원 ID: 12345를 찾을 수 없습니다.");
        assertEquals("회원 ID: 12345를 찾을 수 없습니다.", exception.getMessage());
    }
}
