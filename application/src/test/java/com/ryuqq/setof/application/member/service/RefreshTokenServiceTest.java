package com.ryuqq.setof.application.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.dto.command.RefreshTokenCommand;
import com.ryuqq.setof.application.member.dto.response.RefreshTokenResponse;
import com.ryuqq.setof.application.member.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.manager.TokenManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("RefreshTokenService")
@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private TokenManager tokenManager;

    private RefreshTokenService service;

    @BeforeEach
    void setUp() {
        service = new RefreshTokenService(tokenManager);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 Refresh Token으로 새 토큰 쌍 발급")
        void shouldRefreshTokensSuccessfully() {
            // Given
            String refreshToken = "valid_refresh_token";
            RefreshTokenCommand command = new RefreshTokenCommand(refreshToken);
            TokenPairResponse expectedTokenPair = new TokenPairResponse(
                    "new_access_token",
                    "new_refresh_token",
                    3600L,
                    604800L);

            when(tokenManager.refreshTokens(refreshToken)).thenReturn(expectedTokenPair);

            // When
            RefreshTokenResponse result = service.execute(command);

            // Then
            assertNotNull(result);
            assertNotNull(result.tokens());
            assertEquals("new_access_token", result.tokens().accessToken());
            assertEquals("new_refresh_token", result.tokens().refreshToken());
            assertEquals(3600L, result.tokens().accessTokenExpiresIn());
            assertEquals(604800L, result.tokens().refreshTokenExpiresIn());

            verify(tokenManager).refreshTokens(refreshToken);
        }

        @Test
        @DisplayName("TokenManager에서 예외 발생 시 전파")
        void shouldPropagateExceptionFromTokenManager() {
            // Given
            String invalidToken = "invalid_refresh_token";
            RefreshTokenCommand command = new RefreshTokenCommand(invalidToken);

            when(tokenManager.refreshTokens(invalidToken))
                    .thenThrow(new RuntimeException("Invalid refresh token"));

            // When & Then
            assertThrows(RuntimeException.class, () -> service.execute(command));

            verify(tokenManager).refreshTokens(invalidToken);
        }
    }
}
