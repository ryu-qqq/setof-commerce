package com.ryuqq.setof.application.auth;

import com.ryuqq.setof.application.auth.dto.command.LoginCommand;
import com.ryuqq.setof.application.auth.dto.command.LogoutCommand;

/**
 * Auth Application Command 테스트 Fixtures.
 *
 * <p>로그인/로그아웃 Command 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public final class AuthCommandFixtures {

    private AuthCommandFixtures() {}

    public static final String DEFAULT_IDENTIFIER = "010-1234-5678";
    public static final String DEFAULT_PASSWORD = "password123";
    public static final String DEFAULT_USER_ID = "1";

    // ===== LoginCommand =====

    public static LoginCommand loginCommand() {
        return new LoginCommand(DEFAULT_IDENTIFIER, DEFAULT_PASSWORD);
    }

    public static LoginCommand loginCommand(String identifier, String password) {
        return new LoginCommand(identifier, password);
    }

    // ===== LogoutCommand =====

    public static LogoutCommand logoutCommand() {
        return new LogoutCommand(DEFAULT_USER_ID);
    }

    public static LogoutCommand logoutCommand(String userId) {
        return new LogoutCommand(userId);
    }
}
