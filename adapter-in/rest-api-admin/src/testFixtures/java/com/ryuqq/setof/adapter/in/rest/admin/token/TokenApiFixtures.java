package com.ryuqq.setof.adapter.in.rest.admin.token;

import com.ryuqq.setof.adapter.in.rest.admin.v2.token.dto.command.LoginApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.token.dto.response.LoginApiResponse;

/**
 * Token API 테스트 Fixtures.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class TokenApiFixtures {

    private TokenApiFixtures() {}

    // ===== 기본 상수 =====
    public static final String DEFAULT_IDENTIFIER = "admin@example.com";
    public static final String DEFAULT_PASSWORD = "password123!";
    public static final String DEFAULT_ACCESS_TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U";
    public static final String DEFAULT_REFRESH_TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJyZWZyZXNoIn0.8n0fOXbdJ6p7Y5L0B1vI2n3qR4sM6wK8xA9zC0eD2fQ";
    public static final String DEFAULT_TOKEN_TYPE = "Bearer";
    public static final long DEFAULT_EXPIRES_IN = 3600L;

    // ===== Request Fixtures =====

    /** 기본 로그인 요청. */
    public static LoginApiRequest loginRequest() {
        return new LoginApiRequest(DEFAULT_IDENTIFIER, DEFAULT_PASSWORD);
    }

    /** 커스텀 로그인 요청. */
    public static LoginApiRequest loginRequest(String identifier, String password) {
        return new LoginApiRequest(identifier, password);
    }

    // ===== Response Fixtures =====

    /** 기본 로그인 응답. */
    public static LoginApiResponse loginResponse() {
        return new LoginApiResponse(
                DEFAULT_ACCESS_TOKEN,
                DEFAULT_REFRESH_TOKEN,
                DEFAULT_TOKEN_TYPE,
                DEFAULT_EXPIRES_IN);
    }

    /** 커스텀 로그인 응답. */
    public static LoginApiResponse loginResponse(
            String accessToken, String refreshToken, long expiresIn) {
        return new LoginApiResponse(accessToken, refreshToken, DEFAULT_TOKEN_TYPE, expiresIn);
    }
}
