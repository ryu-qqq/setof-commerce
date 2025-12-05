package com.ryuqq.setof.application.member.manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.port.out.command.RefreshTokenCachePort;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("RefreshTokenCacheManager")
@ExtendWith(MockitoExtension.class)
class RefreshTokenCacheManagerTest {

    @Mock private RefreshTokenCachePort refreshTokenCachePort;

    private RefreshTokenCacheManager manager;

    @BeforeEach
    void setUp() {
        manager = new RefreshTokenCacheManager(refreshTokenCachePort);
    }

    @Nested
    @DisplayName("save")
    class SaveTest {

        @Test
        @DisplayName("Refresh Token 캐시 저장 성공")
        void shouldSaveRefreshTokenToCache() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            String refreshToken = "refresh_token_123";
            long expiresInSeconds = 604800L;

            // When
            manager.save(memberId, refreshToken, expiresInSeconds);

            // Then
            verify(refreshTokenCachePort).save(memberId, refreshToken, expiresInSeconds);
        }
    }

    @Nested
    @DisplayName("findByMemberId")
    class FindByMemberIdTest {

        @Test
        @DisplayName("회원 ID로 Refresh Token 조회 성공")
        void shouldFindRefreshTokenByMemberId() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            String expectedToken = "refresh_token_123";

            when(refreshTokenCachePort.findByMemberId(memberId))
                    .thenReturn(Optional.of(expectedToken));

            // When
            Optional<String> result = manager.findByMemberId(memberId);

            // Then
            assertTrue(result.isPresent());
            assertEquals(expectedToken, result.get());
            verify(refreshTokenCachePort).findByMemberId(memberId);
        }

        @Test
        @DisplayName("존재하지 않는 회원 ID로 조회 시 빈 Optional 반환")
        void shouldReturnEmptyOptionalWhenNotFound() {
            // Given
            String memberId = "non-existent-id";

            when(refreshTokenCachePort.findByMemberId(memberId)).thenReturn(Optional.empty());

            // When
            Optional<String> result = manager.findByMemberId(memberId);

            // Then
            assertTrue(result.isEmpty());
            verify(refreshTokenCachePort).findByMemberId(memberId);
        }
    }

    @Nested
    @DisplayName("findMemberIdByToken")
    class FindMemberIdByTokenTest {

        @Test
        @DisplayName("Refresh Token으로 회원 ID 조회 성공")
        void shouldFindMemberIdByToken() {
            // Given
            String refreshToken = "refresh_token_123";
            String expectedMemberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";

            when(refreshTokenCachePort.findMemberIdByToken(refreshToken))
                    .thenReturn(Optional.of(expectedMemberId));

            // When
            Optional<String> result = manager.findMemberIdByToken(refreshToken);

            // Then
            assertTrue(result.isPresent());
            assertEquals(expectedMemberId, result.get());
            verify(refreshTokenCachePort).findMemberIdByToken(refreshToken);
        }

        @Test
        @DisplayName("존재하지 않는 토큰으로 조회 시 빈 Optional 반환")
        void shouldReturnEmptyOptionalWhenTokenNotFound() {
            // Given
            String invalidToken = "invalid_token";

            when(refreshTokenCachePort.findMemberIdByToken(invalidToken))
                    .thenReturn(Optional.empty());

            // When
            Optional<String> result = manager.findMemberIdByToken(invalidToken);

            // Then
            assertTrue(result.isEmpty());
            verify(refreshTokenCachePort).findMemberIdByToken(invalidToken);
        }
    }

    @Nested
    @DisplayName("deleteByMemberId")
    class DeleteByMemberIdTest {

        @Test
        @DisplayName("회원 ID로 캐시 삭제 성공")
        void shouldDeleteCacheByMemberId() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";

            // When
            manager.deleteByMemberId(memberId);

            // Then
            verify(refreshTokenCachePort).deleteByMemberId(memberId);
        }
    }

    @Nested
    @DisplayName("deleteByToken")
    class DeleteByTokenTest {

        @Test
        @DisplayName("토큰 값으로 캐시 삭제 성공")
        void shouldDeleteCacheByToken() {
            // Given
            String refreshToken = "refresh_token_789";

            // When
            manager.deleteByToken(refreshToken);

            // Then
            verify(refreshTokenCachePort).deleteByToken(refreshToken);
        }
    }
}
