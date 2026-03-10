package com.ryuqq.setof.application.auth;

import com.ryuqq.setof.application.auth.dto.response.KakaoLoginResult;
import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;

/**
 * Auth Application Response 테스트 Fixtures.
 *
 * <p>LoginResult, TokenPairResponse, KakaoLoginResult 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public final class AuthResponseFixtures {

    private AuthResponseFixtures() {}

    public static final String DEFAULT_ACCESS_TOKEN = "test.access.token";
    public static final String DEFAULT_REFRESH_TOKEN = "test.refresh.token";
    public static final long DEFAULT_ACCESS_TOKEN_EXPIRES_IN = 3600L;
    public static final long DEFAULT_REFRESH_TOKEN_EXPIRES_IN = 86400L;
    public static final String DEFAULT_TOKEN_TYPE = "Bearer";
    public static final String DEFAULT_USER_ID = "1";
    public static final String DEFAULT_MEMBER_ID = "1";

    // ===== TokenPairResponse =====

    public static TokenPairResponse tokenPairResponse() {
        return new TokenPairResponse(
                DEFAULT_ACCESS_TOKEN,
                DEFAULT_ACCESS_TOKEN_EXPIRES_IN,
                DEFAULT_REFRESH_TOKEN,
                DEFAULT_REFRESH_TOKEN_EXPIRES_IN);
    }

    public static TokenPairResponse tokenPairResponse(String accessToken, String refreshToken) {
        return new TokenPairResponse(
                accessToken,
                DEFAULT_ACCESS_TOKEN_EXPIRES_IN,
                refreshToken,
                DEFAULT_REFRESH_TOKEN_EXPIRES_IN);
    }

    // ===== LoginResult =====

    public static LoginResult loginResultSuccess() {
        return LoginResult.success(
                DEFAULT_USER_ID,
                DEFAULT_ACCESS_TOKEN,
                DEFAULT_REFRESH_TOKEN,
                DEFAULT_ACCESS_TOKEN_EXPIRES_IN,
                DEFAULT_TOKEN_TYPE);
    }

    public static LoginResult loginResultSuccess(String userId) {
        return LoginResult.success(
                userId,
                DEFAULT_ACCESS_TOKEN,
                DEFAULT_REFRESH_TOKEN,
                DEFAULT_ACCESS_TOKEN_EXPIRES_IN,
                DEFAULT_TOKEN_TYPE);
    }

    public static LoginResult loginResultFailure() {
        return LoginResult.failure("INVALID_CREDENTIALS", "아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    // ===== KakaoLoginResult =====

    public static KakaoLoginResult kakaoLoginResultForNewMember() {
        return KakaoLoginResult.ofNewMember(loginResultSuccess());
    }

    public static KakaoLoginResult kakaoLoginResultForExistingMember() {
        return KakaoLoginResult.ofExistingMember(loginResultSuccess());
    }
}
