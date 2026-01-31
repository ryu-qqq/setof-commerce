package com.ryuqq.setof.adapter.out.client.authhub;

import com.ryuqq.authhub.sdk.api.AuthApi;
import com.ryuqq.authhub.sdk.exception.AuthHubException;
import com.ryuqq.authhub.sdk.exception.AuthHubUnauthorizedException;
import com.ryuqq.authhub.sdk.model.auth.LoginRequest;
import com.ryuqq.authhub.sdk.model.auth.LoginResponse;
import com.ryuqq.authhub.sdk.model.auth.LogoutRequest;
import com.ryuqq.authhub.sdk.model.auth.MyContextResponse;
import com.ryuqq.authhub.sdk.model.auth.RefreshTokenRequest;
import com.ryuqq.authhub.sdk.model.auth.TokenResponse;
import com.ryuqq.authhub.sdk.model.common.ApiResponse;
import com.ryuqq.setof.adapter.out.client.authhub.dto.AuthHubLoginResult;
import com.ryuqq.setof.adapter.out.client.authhub.dto.AuthHubRefreshResult;
import com.ryuqq.setof.adapter.out.client.authhub.dto.AuthHubUserContext;
import org.springframework.stereotype.Component;

/**
 * AuthHub 인증 클라이언트 어댑터.
 *
 * <p>AuthHub SDK를 사용하여 로그인, 토큰 갱신, 사용자 정보 조회를 수행합니다.
 */
@Component
public class AuthHubAuthClientAdapter {

    private final AuthApi authApi;

    public AuthHubAuthClientAdapter(AuthApi authApi) {
        this.authApi = authApi;
    }

    /**
     * 로그인을 수행합니다.
     *
     * @param identifier 사용자 식별자 (이메일 또는 사용자명)
     * @param password 비밀번호
     * @return 로그인 결과
     */
    public AuthHubLoginResult login(String identifier, String password) {
        try {
            LoginRequest request = new LoginRequest(identifier, password);
            ApiResponse<LoginResponse> response = authApi.login(request);
            LoginResponse data = response.data();

            return AuthHubLoginResult.success(
                    data.userId(),
                    data.accessToken(),
                    data.refreshToken(),
                    data.expiresIn(),
                    data.tokenType());

        } catch (AuthHubUnauthorizedException e) {
            return AuthHubLoginResult.failure("UNAUTHORIZED", e.getMessage());

        } catch (AuthHubException e) {
            return AuthHubLoginResult.failure("AUTH_ERROR", e.getMessage());
        }
    }

    /**
     * 토큰을 갱신합니다.
     *
     * @param refreshToken 리프레시 토큰
     * @return 토큰 갱신 결과
     */
    public AuthHubRefreshResult refresh(String refreshToken) {
        try {
            RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
            ApiResponse<TokenResponse> response = authApi.refresh(request);
            TokenResponse data = response.data();

            return AuthHubRefreshResult.success(
                    data.accessToken(),
                    data.refreshToken(),
                    data.accessTokenExpiresIn(),
                    data.refreshTokenExpiresIn());

        } catch (AuthHubUnauthorizedException e) {
            return AuthHubRefreshResult.failure("UNAUTHORIZED", e.getMessage());

        } catch (AuthHubException e) {
            return AuthHubRefreshResult.failure("AUTH_ERROR", e.getMessage());
        }
    }

    /**
     * 현재 사용자 정보를 조회합니다.
     *
     * @return 사용자 컨텍스트
     */
    public AuthHubUserContext getMe() {
        try {
            ApiResponse<MyContextResponse> response = authApi.getMe();
            MyContextResponse data = response.data();

            return AuthHubUserContext.from(data);

        } catch (AuthHubUnauthorizedException e) {
            throw new IllegalStateException("Unauthorized: " + e.getMessage(), e);

        } catch (AuthHubException e) {
            throw new IllegalStateException("Failed to get user context: " + e.getMessage(), e);
        }
    }

    /**
     * 로그아웃을 수행합니다.
     *
     * @param userId 사용자 ID
     */
    public void logout(String userId) {
        try {
            LogoutRequest request = new LogoutRequest(userId);
            authApi.logout(request);

        } catch (AuthHubException e) {
            throw new IllegalStateException("Failed to logout: " + e.getMessage(), e);
        }
    }
}
