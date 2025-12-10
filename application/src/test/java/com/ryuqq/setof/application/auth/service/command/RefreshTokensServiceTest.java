package com.ryuqq.setof.application.auth.service.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.auth.dto.command.RefreshTokenCommand;
import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.auth.facade.command.RefreshTokenFacade;
import com.ryuqq.setof.application.auth.port.in.command.IssueTokensUseCase;
import com.ryuqq.setof.application.auth.port.in.command.RevokeTokensUseCase;
import com.ryuqq.setof.domain.auth.exception.InvalidRefreshTokenException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokensService")
class RefreshTokensServiceTest {

    private static final String MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
    private static final String OLD_REFRESH_TOKEN = "old-refresh-token";
    private static final String NEW_ACCESS_TOKEN = "new-access-token";
    private static final String NEW_REFRESH_TOKEN = "new-refresh-token";
    private static final long ACCESS_EXPIRES_IN = 3600L;
    private static final long REFRESH_EXPIRES_IN = 604800L;

    @Mock private RefreshTokenFacade refreshTokenFacade;

    @Mock private RevokeTokensUseCase revokeTokensUseCase;

    @Mock private IssueTokensUseCase issueTokensUseCase;

    private RefreshTokensService refreshTokensService;

    @BeforeEach
    void setUp() {
        refreshTokensService =
                new RefreshTokensService(
                        refreshTokenFacade, revokeTokensUseCase, issueTokensUseCase);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("토큰 갱신 성공")
        void shouldRefreshTokensSuccessfully() {
            // Given
            RefreshTokenCommand command = RefreshTokenCommand.of(OLD_REFRESH_TOKEN);
            TokenPairResponse newTokenPair =
                    new TokenPairResponse(
                            NEW_ACCESS_TOKEN,
                            ACCESS_EXPIRES_IN,
                            NEW_REFRESH_TOKEN,
                            REFRESH_EXPIRES_IN);

            when(refreshTokenFacade.findMemberIdByToken(OLD_REFRESH_TOKEN))
                    .thenReturn(Optional.of(MEMBER_ID));
            when(issueTokensUseCase.execute(MEMBER_ID)).thenReturn(newTokenPair);

            // When
            TokenPairResponse result = refreshTokensService.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(NEW_ACCESS_TOKEN, result.accessToken());
            assertEquals(NEW_REFRESH_TOKEN, result.refreshToken());
        }

        @Test
        @DisplayName("기존 토큰 무효화 후 새 토큰 발급")
        void shouldRevokeOldTokenAndIssueNew() {
            // Given
            RefreshTokenCommand command = RefreshTokenCommand.of(OLD_REFRESH_TOKEN);
            TokenPairResponse newTokenPair =
                    new TokenPairResponse(
                            NEW_ACCESS_TOKEN,
                            ACCESS_EXPIRES_IN,
                            NEW_REFRESH_TOKEN,
                            REFRESH_EXPIRES_IN);

            when(refreshTokenFacade.findMemberIdByToken(OLD_REFRESH_TOKEN))
                    .thenReturn(Optional.of(MEMBER_ID));
            when(issueTokensUseCase.execute(MEMBER_ID)).thenReturn(newTokenPair);

            // When
            refreshTokensService.execute(command);

            // Then
            verify(revokeTokensUseCase).revokeByToken(OLD_REFRESH_TOKEN);
            verify(issueTokensUseCase).execute(MEMBER_ID);
        }

        @Test
        @DisplayName("유효하지 않은 토큰으로 갱신 시 예외 발생")
        void shouldThrowExceptionWhenTokenInvalid() {
            // Given
            RefreshTokenCommand command = RefreshTokenCommand.of(OLD_REFRESH_TOKEN);

            when(refreshTokenFacade.findMemberIdByToken(OLD_REFRESH_TOKEN))
                    .thenReturn(Optional.empty());

            // When & Then
            assertThrows(
                    InvalidRefreshTokenException.class,
                    () -> refreshTokensService.execute(command));

            verify(revokeTokensUseCase, never()).revokeByToken(OLD_REFRESH_TOKEN);
            verify(issueTokensUseCase, never()).execute(MEMBER_ID);
        }

        @Test
        @DisplayName("RefreshTokenFacade로 회원 ID 조회")
        void shouldLookupMemberIdFromFacade() {
            // Given
            RefreshTokenCommand command = RefreshTokenCommand.of(OLD_REFRESH_TOKEN);
            TokenPairResponse newTokenPair =
                    new TokenPairResponse(
                            NEW_ACCESS_TOKEN,
                            ACCESS_EXPIRES_IN,
                            NEW_REFRESH_TOKEN,
                            REFRESH_EXPIRES_IN);

            when(refreshTokenFacade.findMemberIdByToken(OLD_REFRESH_TOKEN))
                    .thenReturn(Optional.of(MEMBER_ID));
            when(issueTokensUseCase.execute(MEMBER_ID)).thenReturn(newTokenPair);

            // When
            refreshTokensService.execute(command);

            // Then
            verify(refreshTokenFacade).findMemberIdByToken(OLD_REFRESH_TOKEN);
        }
    }
}
