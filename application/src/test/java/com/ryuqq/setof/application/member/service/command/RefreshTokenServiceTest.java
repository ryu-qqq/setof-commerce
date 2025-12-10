package com.ryuqq.setof.application.member.service.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.auth.port.in.command.RefreshTokensUseCase;
import com.ryuqq.setof.application.member.dto.command.RefreshTokenCommand;
import com.ryuqq.setof.application.member.dto.response.RefreshTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenService")
class RefreshTokenServiceTest {

    @Mock private RefreshTokensUseCase refreshTokensUseCase;

    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        refreshTokenService = new RefreshTokenService(refreshTokensUseCase);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("Refresh Token으로 새로운 토큰 발급")
        void shouldIssueNewTokensWithRefreshToken() {
            // Given
            String refreshToken = "valid-refresh-token";
            RefreshTokenCommand command = new RefreshTokenCommand(refreshToken);

            TokenPairResponse newTokens =
                    new TokenPairResponse("new-access-token", 3600L, "new-refresh-token", 604800L);

            when(refreshTokensUseCase.execute(any())).thenReturn(newTokens);

            // When
            RefreshTokenResponse result = refreshTokenService.execute(command);

            // Then
            assertNotNull(result);
            assertNotNull(result.tokens());
            assertEquals("new-access-token", result.tokens().accessToken());
            assertEquals("new-refresh-token", result.tokens().refreshToken());
            verify(refreshTokensUseCase).execute(any());
        }

        @Test
        @DisplayName("RefreshTokensUseCase로 토큰 갱신 요청")
        void shouldDelegateToRefreshTokensUseCase() {
            // Given
            String refreshToken = "refresh-token-123";
            RefreshTokenCommand command = new RefreshTokenCommand(refreshToken);

            TokenPairResponse tokens = new TokenPairResponse("access", 3600L, "refresh", 604800L);
            when(refreshTokensUseCase.execute(any())).thenReturn(tokens);

            // When
            refreshTokenService.execute(command);

            // Then
            verify(refreshTokensUseCase).execute(any());
        }
    }
}
