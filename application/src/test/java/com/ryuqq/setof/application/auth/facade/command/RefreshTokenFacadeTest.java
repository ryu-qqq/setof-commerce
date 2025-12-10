package com.ryuqq.setof.application.auth.facade.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.auth.manager.command.RefreshTokenCacheManager;
import com.ryuqq.setof.application.auth.manager.command.RefreshTokenPersistenceManager;
import com.ryuqq.setof.application.auth.manager.query.RefreshTokenCacheReadManager;
import com.ryuqq.setof.application.auth.manager.query.RefreshTokenReadManager;
import com.ryuqq.setof.domain.auth.aggregate.RefreshToken;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenFacade")
class RefreshTokenFacadeTest {

    private static final String MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
    private static final String TOKEN_VALUE = "refresh-token-123";
    private static final long EXPIRES_IN_SECONDS = 604800L;

    @Mock private RefreshTokenPersistenceManager refreshTokenPersistenceManager;

    @Mock private RefreshTokenCacheManager refreshTokenCacheManager;

    @Mock private RefreshTokenReadManager refreshTokenReadManager;

    @Mock private RefreshTokenCacheReadManager refreshTokenCacheReadManager;

    private RefreshTokenFacade refreshTokenFacade;

    @BeforeEach
    void setUp() {
        refreshTokenFacade =
                new RefreshTokenFacade(
                        refreshTokenPersistenceManager,
                        refreshTokenCacheManager,
                        refreshTokenReadManager,
                        refreshTokenCacheReadManager);
    }

    @Nested
    @DisplayName("persist")
    class PersistTest {

        @Test
        @DisplayName("RDB와 캐시에 토큰 저장")
        void shouldPersistToRdbAndCache() {
            // Given
            RefreshToken refreshToken =
                    RefreshToken.create(MEMBER_ID, TOKEN_VALUE, EXPIRES_IN_SECONDS, Instant.now());

            // When
            refreshTokenFacade.persist(refreshToken);

            // Then
            verify(refreshTokenPersistenceManager).persist(refreshToken);
            verify(refreshTokenCacheManager)
                    .persist(eq(TOKEN_VALUE), eq(MEMBER_ID), eq(EXPIRES_IN_SECONDS));
        }
    }

    @Nested
    @DisplayName("deleteByMemberId")
    class DeleteByMemberIdTest {

        @Test
        @DisplayName("회원 ID로 RDB와 캐시에서 토큰 삭제")
        void shouldDeleteFromRdbAndCache() {
            // Given
            when(refreshTokenReadManager.findTokenByMemberId(MEMBER_ID))
                    .thenReturn(Optional.of(TOKEN_VALUE));

            // When
            refreshTokenFacade.deleteByMemberId(MEMBER_ID);

            // Then
            verify(refreshTokenReadManager).findTokenByMemberId(MEMBER_ID);
            verify(refreshTokenCacheManager).delete(TOKEN_VALUE);
            verify(refreshTokenPersistenceManager).deleteByMemberId(MEMBER_ID);
        }

        @Test
        @DisplayName("토큰 없으면 캐시 삭제 안함")
        void shouldNotDeleteCacheWhenNoToken() {
            // Given
            when(refreshTokenReadManager.findTokenByMemberId(MEMBER_ID))
                    .thenReturn(Optional.empty());

            // When
            refreshTokenFacade.deleteByMemberId(MEMBER_ID);

            // Then
            verify(refreshTokenCacheManager, never()).delete(TOKEN_VALUE);
            verify(refreshTokenPersistenceManager).deleteByMemberId(MEMBER_ID);
        }
    }

    @Nested
    @DisplayName("deleteByToken")
    class DeleteByTokenTest {

        @Test
        @DisplayName("토큰 값으로 RDB와 캐시에서 삭제")
        void shouldDeleteFromRdbAndCache() {
            // When
            refreshTokenFacade.deleteByToken(TOKEN_VALUE);

            // Then
            verify(refreshTokenCacheManager).delete(TOKEN_VALUE);
            verify(refreshTokenPersistenceManager).deleteByToken(TOKEN_VALUE);
        }
    }

    @Nested
    @DisplayName("findMemberIdByToken")
    class FindMemberIdByTokenTest {

        @Test
        @DisplayName("캐시에서 회원 ID 조회")
        void shouldFindMemberIdFromCache() {
            // Given
            when(refreshTokenCacheReadManager.findMemberIdByToken(TOKEN_VALUE))
                    .thenReturn(Optional.of(MEMBER_ID));

            // When
            Optional<String> result = refreshTokenFacade.findMemberIdByToken(TOKEN_VALUE);

            // Then
            assertTrue(result.isPresent());
            assertEquals(MEMBER_ID, result.get());
        }

        @Test
        @DisplayName("캐시에 없으면 빈 결과")
        void shouldReturnEmptyWhenNotInCache() {
            // Given
            when(refreshTokenCacheReadManager.findMemberIdByToken(TOKEN_VALUE))
                    .thenReturn(Optional.empty());

            // When
            Optional<String> result = refreshTokenFacade.findMemberIdByToken(TOKEN_VALUE);

            // Then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("findTokenByMemberId")
    class FindTokenByMemberIdTest {

        @Test
        @DisplayName("RDB에서 토큰 조회")
        void shouldFindTokenFromRdb() {
            // Given
            when(refreshTokenReadManager.findTokenByMemberId(MEMBER_ID))
                    .thenReturn(Optional.of(TOKEN_VALUE));

            // When
            Optional<String> result = refreshTokenFacade.findTokenByMemberId(MEMBER_ID);

            // Then
            assertTrue(result.isPresent());
            assertEquals(TOKEN_VALUE, result.get());
        }

        @Test
        @DisplayName("RDB에 없으면 빈 결과")
        void shouldReturnEmptyWhenNotInRdb() {
            // Given
            when(refreshTokenReadManager.findTokenByMemberId(MEMBER_ID))
                    .thenReturn(Optional.empty());

            // When
            Optional<String> result = refreshTokenFacade.findTokenByMemberId(MEMBER_ID);

            // Then
            assertTrue(result.isEmpty());
        }
    }
}
