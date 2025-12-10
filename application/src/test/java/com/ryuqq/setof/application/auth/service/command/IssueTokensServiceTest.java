package com.ryuqq.setof.application.auth.service.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.auth.facade.command.RefreshTokenFacade;
import com.ryuqq.setof.application.auth.factory.RefreshTokenFactory;
import com.ryuqq.setof.application.auth.port.out.client.TokenProviderPort;
import com.ryuqq.setof.domain.auth.aggregate.RefreshToken;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("IssueTokensService")
class IssueTokensServiceTest {

    private static final String MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
    private static final String ACCESS_TOKEN = "access-token-123";
    private static final String REFRESH_TOKEN = "refresh-token-456";
    private static final long ACCESS_EXPIRES_IN = 3600L;
    private static final long REFRESH_EXPIRES_IN = 604800L;

    @Mock private TokenProviderPort tokenProviderPort;

    @Mock private RefreshTokenFactory refreshTokenFactory;

    @Mock private RefreshTokenFacade refreshTokenFacade;

    private IssueTokensService issueTokensService;

    @BeforeEach
    void setUp() {
        issueTokensService =
                new IssueTokensService(tokenProviderPort, refreshTokenFactory, refreshTokenFacade);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("토큰 발급 성공")
        void shouldIssueTokensSuccessfully() {
            // Given
            TokenPairResponse tokenPair =
                    new TokenPairResponse(
                            ACCESS_TOKEN, ACCESS_EXPIRES_IN, REFRESH_TOKEN, REFRESH_EXPIRES_IN);

            RefreshToken refreshToken =
                    RefreshToken.create(
                            MEMBER_ID, REFRESH_TOKEN, REFRESH_EXPIRES_IN, Instant.now());

            when(tokenProviderPort.generateTokenPair(MEMBER_ID)).thenReturn(tokenPair);
            when(refreshTokenFactory.create(
                            eq(MEMBER_ID), eq(REFRESH_TOKEN), eq(REFRESH_EXPIRES_IN)))
                    .thenReturn(refreshToken);

            // When
            TokenPairResponse result = issueTokensService.execute(MEMBER_ID);

            // Then
            assertNotNull(result);
            assertEquals(ACCESS_TOKEN, result.accessToken());
            assertEquals(REFRESH_TOKEN, result.refreshToken());
            assertEquals(ACCESS_EXPIRES_IN, result.accessTokenExpiresIn());
            assertEquals(REFRESH_EXPIRES_IN, result.refreshTokenExpiresIn());
        }

        @Test
        @DisplayName("TokenProviderPort로 토큰 쌍 생성 요청")
        void shouldCallTokenProviderPort() {
            // Given
            TokenPairResponse tokenPair =
                    new TokenPairResponse(
                            ACCESS_TOKEN, ACCESS_EXPIRES_IN, REFRESH_TOKEN, REFRESH_EXPIRES_IN);

            RefreshToken refreshToken =
                    RefreshToken.create(
                            MEMBER_ID, REFRESH_TOKEN, REFRESH_EXPIRES_IN, Instant.now());

            when(tokenProviderPort.generateTokenPair(MEMBER_ID)).thenReturn(tokenPair);
            when(refreshTokenFactory.create(
                            eq(MEMBER_ID), eq(REFRESH_TOKEN), eq(REFRESH_EXPIRES_IN)))
                    .thenReturn(refreshToken);

            // When
            issueTokensService.execute(MEMBER_ID);

            // Then
            verify(tokenProviderPort).generateTokenPair(MEMBER_ID);
        }

        @Test
        @DisplayName("RefreshTokenFactory로 RefreshToken Aggregate 생성")
        void shouldCreateRefreshTokenAggregate() {
            // Given
            TokenPairResponse tokenPair =
                    new TokenPairResponse(
                            ACCESS_TOKEN, ACCESS_EXPIRES_IN, REFRESH_TOKEN, REFRESH_EXPIRES_IN);

            RefreshToken refreshToken =
                    RefreshToken.create(
                            MEMBER_ID, REFRESH_TOKEN, REFRESH_EXPIRES_IN, Instant.now());

            when(tokenProviderPort.generateTokenPair(MEMBER_ID)).thenReturn(tokenPair);
            when(refreshTokenFactory.create(
                            eq(MEMBER_ID), eq(REFRESH_TOKEN), eq(REFRESH_EXPIRES_IN)))
                    .thenReturn(refreshToken);

            // When
            issueTokensService.execute(MEMBER_ID);

            // Then
            verify(refreshTokenFactory).create(MEMBER_ID, REFRESH_TOKEN, REFRESH_EXPIRES_IN);
        }

        @Test
        @DisplayName("RefreshTokenFacade로 토큰 저장 요청")
        void shouldPersistRefreshToken() {
            // Given
            TokenPairResponse tokenPair =
                    new TokenPairResponse(
                            ACCESS_TOKEN, ACCESS_EXPIRES_IN, REFRESH_TOKEN, REFRESH_EXPIRES_IN);

            RefreshToken refreshToken =
                    RefreshToken.create(
                            MEMBER_ID, REFRESH_TOKEN, REFRESH_EXPIRES_IN, Instant.now());

            when(tokenProviderPort.generateTokenPair(MEMBER_ID)).thenReturn(tokenPair);
            when(refreshTokenFactory.create(
                            eq(MEMBER_ID), eq(REFRESH_TOKEN), eq(REFRESH_EXPIRES_IN)))
                    .thenReturn(refreshToken);

            // When
            issueTokensService.execute(MEMBER_ID);

            // Then
            verify(refreshTokenFacade).persist(any(RefreshToken.class));
        }
    }
}
