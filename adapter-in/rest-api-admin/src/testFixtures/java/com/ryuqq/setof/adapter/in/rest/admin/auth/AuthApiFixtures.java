package com.ryuqq.setof.adapter.in.rest.admin.auth;

import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.dto.command.LoginApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.dto.response.LoginApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.auth.dto.response.MyInfoApiResponse;
import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.auth.dto.response.MyInfoResult;
import java.util.List;

/**
 * Auth API 테스트 Fixtures.
 *
 * <p>인증 API 테스트에서 사용되는 Request/Response/Result 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class AuthApiFixtures {

    private AuthApiFixtures() {}

    // ===== 기본 상수 =====
    public static final String DEFAULT_IDENTIFIER = "admin@example.com";
    public static final String DEFAULT_PASSWORD = "password123!";
    public static final String DEFAULT_USER_ID = "user-123";
    public static final String DEFAULT_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.access";
    public static final String DEFAULT_REFRESH_TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refresh";
    public static final String DEFAULT_TOKEN_TYPE = "Bearer";
    public static final long DEFAULT_EXPIRES_IN = 3600L;

    // ===== Login Request Fixtures =====

    public static LoginApiRequest loginRequest() {
        return new LoginApiRequest(DEFAULT_IDENTIFIER, DEFAULT_PASSWORD);
    }

    public static LoginApiRequest loginRequest(String identifier, String password) {
        return new LoginApiRequest(identifier, password);
    }

    // ===== Login Response Fixtures =====

    public static LoginApiResponse loginResponse() {
        return new LoginApiResponse(
                DEFAULT_ACCESS_TOKEN,
                DEFAULT_REFRESH_TOKEN,
                DEFAULT_TOKEN_TYPE,
                DEFAULT_EXPIRES_IN);
    }

    public static LoginApiResponse loginResponse(
            String accessToken, String refreshToken, String tokenType, long expiresIn) {
        return new LoginApiResponse(accessToken, refreshToken, tokenType, expiresIn);
    }

    // ===== Login Result Fixtures (Application Layer) =====

    public static LoginResult successLoginResult() {
        return LoginResult.success(
                DEFAULT_USER_ID,
                DEFAULT_ACCESS_TOKEN,
                DEFAULT_REFRESH_TOKEN,
                DEFAULT_EXPIRES_IN,
                DEFAULT_TOKEN_TYPE);
    }

    public static LoginResult successLoginResult(
            String userId,
            String accessToken,
            String refreshToken,
            long expiresIn,
            String tokenType) {
        return LoginResult.success(userId, accessToken, refreshToken, expiresIn, tokenType);
    }

    public static LoginResult failureLoginResult() {
        return LoginResult.failure("UNAUTHORIZED", "Invalid credentials");
    }

    public static LoginResult failureLoginResult(String errorCode, String errorMessage) {
        return LoginResult.failure(errorCode, errorMessage);
    }

    // ===== MyInfo Result Fixtures (Application Layer) =====

    public static MyInfoResult myInfoResult() {
        return new MyInfoResult(
                DEFAULT_USER_ID,
                DEFAULT_IDENTIFIER,
                "관리자",
                "tenant-123",
                "테넌트명",
                "org-123",
                "조직명",
                List.of(new MyInfoResult.RoleInfo("role-1", "ADMIN")),
                List.of("READ", "WRITE"));
    }

    public static MyInfoResult myInfoResult(
            String userId,
            String email,
            String name,
            String tenantId,
            String tenantName,
            String organizationId,
            String organizationName,
            List<MyInfoResult.RoleInfo> roles,
            List<String> permissions) {
        return new MyInfoResult(
                userId,
                email,
                name,
                tenantId,
                tenantName,
                organizationId,
                organizationName,
                roles,
                permissions);
    }

    public static MyInfoResult myInfoResultWithoutRoles() {
        return new MyInfoResult(
                DEFAULT_USER_ID,
                DEFAULT_IDENTIFIER,
                "관리자",
                "tenant-123",
                "테넌트명",
                "org-123",
                "조직명",
                null,
                null);
    }

    // ===== MyInfo API Response Fixtures =====

    public static MyInfoApiResponse myInfoApiResponse() {
        return MyInfoApiResponse.from(myInfoResult());
    }

    // ===== Authorization Header Fixtures =====

    public static String bearerToken() {
        return "Bearer " + DEFAULT_ACCESS_TOKEN;
    }

    public static String bearerToken(String accessToken) {
        return "Bearer " + accessToken;
    }

    public static String invalidAuthorizationHeader() {
        return "InvalidHeader";
    }
}
