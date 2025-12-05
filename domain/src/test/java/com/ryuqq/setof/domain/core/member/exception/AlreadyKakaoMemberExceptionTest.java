package com.ryuqq.setof.domain.core.member.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AlreadyKakaoMemberException")
class AlreadyKakaoMemberExceptionTest {

    @Test
    @DisplayName("기본 생성자로 예외 생성")
    void shouldCreateExceptionWithDefaultMessage() {
        AlreadyKakaoMemberException exception = new AlreadyKakaoMemberException();
        assertEquals("이미 카카오로 연동된 회원입니다. 카카오 회원은 카카오 로그인을 이용해주세요.", exception.getMessage());
    }

    @Test
    @DisplayName("커스텀 메시지로 예외 생성")
    void shouldCreateExceptionWithCustomMessage() {
        AlreadyKakaoMemberException exception =
                new AlreadyKakaoMemberException("해당 회원은 이미 카카오 연동 완료.");
        assertEquals("해당 회원은 이미 카카오 연동 완료.", exception.getMessage());
    }
}
