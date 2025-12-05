package com.ryuqq.setof.domain.core.member.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("KakaoMemberCannotChangePasswordException")
class KakaoMemberCannotChangePasswordExceptionTest {

    @Test
    @DisplayName("기본 생성자로 예외 생성")
    void shouldCreateExceptionWithDefaultMessage() {
        KakaoMemberCannotChangePasswordException exception =
                new KakaoMemberCannotChangePasswordException();
        assertEquals("카카오 회원은 비밀번호를 변경할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("커스텀 메시지로 예외 생성")
    void shouldCreateExceptionWithCustomMessage() {
        KakaoMemberCannotChangePasswordException exception =
                new KakaoMemberCannotChangePasswordException("소셜 로그인 회원은 비밀번호가 없습니다.");
        assertEquals("소셜 로그인 회원은 비밀번호가 없습니다.", exception.getMessage());
    }
}
