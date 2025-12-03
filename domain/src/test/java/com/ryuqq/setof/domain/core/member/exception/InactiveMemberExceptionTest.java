package com.ryuqq.setof.domain.core.member.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InactiveMemberException")
class InactiveMemberExceptionTest {

    @Test
    @DisplayName("기본 생성자로 예외 생성")
    void shouldCreateExceptionWithDefaultMessage() {
        InactiveMemberException exception = new InactiveMemberException();
        assertEquals("휴면 또는 정지된 회원입니다. 고객센터에 문의하세요.", exception.getMessage());
    }

    @Test
    @DisplayName("커스텀 메시지로 예외 생성")
    void shouldCreateExceptionWithCustomMessage() {
        InactiveMemberException exception = new InactiveMemberException("계정이 정지되었습니다. 사유: 비정상 활동");
        assertEquals("계정이 정지되었습니다. 사유: 비정상 활동", exception.getMessage());
    }
}
