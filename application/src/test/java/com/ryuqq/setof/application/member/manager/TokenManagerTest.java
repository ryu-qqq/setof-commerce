package com.ryuqq.setof.application.member.manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.port.out.TokenProviderPort;
import com.ryuqq.setof.domain.core.member.exception.InvalidRefreshTokenException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TokenManager")
@ExtendWith(MockitoExtension.class)
class TokenManagerTest {

    @Mock private TokenProviderPort tokenProviderPort;

    @Mock private RefreshTokenPersistenceManager refreshTokenPersistenceManager;

    @Mock private RefreshTokenCacheManager refreshTokenCacheManager;

    private TokenManager tokenManager;

    @BeforeEach
    void setUp() {
        tokenManager =
                new TokenManager(
                        tokenProviderPort,
                        refreshTokenPersistenceManager,
                        refreshTokenCacheManager);
    }

    @Nested
    @DisplayName("issueTokens")
    class IssueTokensTest {

        @Test
        @DisplayName("토큰 쌍 발급 및 저장 성공")
        void shouldIssueTokensAndSave() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            TokenPairResponse expectedTokenPair = createTokenPair();

            when(tokenProviderPort.generateTokenPair(memberId)).thenReturn(expectedTokenPair);

            // When
            TokenPairResponse result = tokenManager.issueTokens(memberId);

            // Then
            assertNotNull(result);
            assertEquals(expectedTokenPair.accessToken(), result.accessToken());
            assertEquals(expectedTokenPair.refreshToken(), result.refreshToken());

            verify(tokenProviderPort).generateTokenPair(memberId);
            verify(refreshTokenPersistenceManager)
                    .save(
                            memberId,
                            expectedTokenPair.refreshToken(),
                            expectedTokenPair.refreshTokenExpiresIn());
            verify(refreshTokenCacheManager)
                    .save(
                            memberId,
                            expectedTokenPair.refreshToken(),
                            expectedTokenPair.refreshTokenExpiresIn());
        }

        @Test
        @DisplayName("토큰 발급 시 RDB 저장 먼저, Cache 저장 나중")
        void shouldSaveToRdbBeforeCache() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            TokenPairResponse tokenPair = createTokenPair();

            when(tokenProviderPort.generateTokenPair(memberId)).thenReturn(tokenPair);

            // When
            tokenManager.issueTokens(memberId);

            // Then
            InOrder inOrder = inOrder(refreshTokenPersistenceManager, refreshTokenCacheManager);
            inOrder.verify(refreshTokenPersistenceManager)
                    .save(memberId, tokenPair.refreshToken(), tokenPair.refreshTokenExpiresIn());
            inOrder.verify(refreshTokenCacheManager)
                    .save(memberId, tokenPair.refreshToken(), tokenPair.refreshTokenExpiresIn());
        }
    }

    @Nested
    @DisplayName("revokeTokensByMemberId")
    class RevokeTokensByMemberIdTest {

        @Test
        @DisplayName("회원 ID로 토큰 무효화 성공")
        void shouldRevokeTokensByMemberId() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";

            // When
            tokenManager.revokeTokensByMemberId(memberId);

            // Then
            verify(refreshTokenCacheManager).deleteByMemberId(memberId);
            verify(refreshTokenPersistenceManager).deleteByMemberId(memberId);
        }

        @Test
        @DisplayName("Cache 삭제 먼저, RDB 삭제 나중 (순서 보장)")
        void shouldDeleteCacheBeforeRdb() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";

            // When
            tokenManager.revokeTokensByMemberId(memberId);

            // Then
            InOrder inOrder = inOrder(refreshTokenCacheManager, refreshTokenPersistenceManager);
            inOrder.verify(refreshTokenCacheManager).deleteByMemberId(memberId);
            inOrder.verify(refreshTokenPersistenceManager).deleteByMemberId(memberId);
        }
    }

    @Nested
    @DisplayName("revokeToken")
    class RevokeTokenTest {

        @Test
        @DisplayName("토큰 값으로 무효화 성공")
        void shouldRevokeTokenByValue() {
            // Given
            String refreshToken = "refresh_token_123";

            // When
            tokenManager.revokeToken(refreshToken);

            // Then
            verify(refreshTokenCacheManager).deleteByToken(refreshToken);
            verify(refreshTokenPersistenceManager).deleteByToken(refreshToken);
        }
    }

    @Nested
    @DisplayName("refreshTokens")
    class RefreshTokensTest {

        @Test
        @DisplayName("유효한 Refresh Token으로 새 토큰 쌍 발급")
        void shouldRefreshTokensSuccessfully() {
            // Given
            String oldRefreshToken = "old_refresh_token";
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            TokenPairResponse newTokenPair = createTokenPair();

            when(refreshTokenCacheManager.findMemberIdByToken(oldRefreshToken))
                    .thenReturn(Optional.of(memberId));
            when(tokenProviderPort.generateTokenPair(memberId)).thenReturn(newTokenPair);

            // When
            TokenPairResponse result = tokenManager.refreshTokens(oldRefreshToken);

            // Then
            assertNotNull(result);
            assertEquals(newTokenPair.accessToken(), result.accessToken());
            assertEquals(newTokenPair.refreshToken(), result.refreshToken());

            verify(refreshTokenCacheManager).findMemberIdByToken(oldRefreshToken);
            verify(refreshTokenCacheManager).deleteByToken(oldRefreshToken);
            verify(refreshTokenPersistenceManager).deleteByToken(oldRefreshToken);
            verify(tokenProviderPort).generateTokenPair(memberId);
        }

        @Test
        @DisplayName("유효하지 않은 Refresh Token으로 갱신 시 InvalidRefreshTokenException 발생")
        void shouldThrowExceptionWhenInvalidRefreshToken() {
            // Given
            String invalidRefreshToken = "invalid_token";

            when(refreshTokenCacheManager.findMemberIdByToken(invalidRefreshToken))
                    .thenReturn(Optional.empty());

            // When & Then
            assertThrows(
                    InvalidRefreshTokenException.class,
                    () -> tokenManager.refreshTokens(invalidRefreshToken));

            verify(refreshTokenCacheManager).findMemberIdByToken(invalidRefreshToken);
            verify(refreshTokenCacheManager, never()).deleteByToken(anyString());
            verify(refreshTokenPersistenceManager, never()).deleteByToken(anyString());
            verify(tokenProviderPort, never()).generateTokenPair(anyString());
        }

        @Test
        @DisplayName("토큰 갱신 시 기존 토큰 무효화 후 새 토큰 발급 순서 보장")
        void shouldRevokeOldTokenBeforeIssuingNew() {
            // Given
            String oldRefreshToken = "old_refresh_token";
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            TokenPairResponse newTokenPair = createTokenPair();

            when(refreshTokenCacheManager.findMemberIdByToken(oldRefreshToken))
                    .thenReturn(Optional.of(memberId));
            when(tokenProviderPort.generateTokenPair(memberId)).thenReturn(newTokenPair);

            // When
            tokenManager.refreshTokens(oldRefreshToken);

            // Then - 순서 검증: 조회 → 삭제 → 새 토큰 발급
            InOrder inOrder =
                    inOrder(
                            refreshTokenCacheManager,
                            refreshTokenPersistenceManager,
                            tokenProviderPort);
            inOrder.verify(refreshTokenCacheManager).findMemberIdByToken(oldRefreshToken);
            inOrder.verify(refreshTokenCacheManager).deleteByToken(oldRefreshToken);
            inOrder.verify(refreshTokenPersistenceManager).deleteByToken(oldRefreshToken);
            inOrder.verify(tokenProviderPort).generateTokenPair(memberId);
        }
    }

    private TokenPairResponse createTokenPair() {
        return new TokenPairResponse("access_token_123", "refresh_token_456", 3600L, 604800L);
    }
}
