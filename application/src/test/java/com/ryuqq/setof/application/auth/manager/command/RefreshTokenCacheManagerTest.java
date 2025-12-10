package com.ryuqq.setof.application.auth.manager.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheCommandPort;
import com.ryuqq.setof.domain.auth.vo.RefreshTokenCacheKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenCacheManager")
class RefreshTokenCacheManagerTest {

    private static final String MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
    private static final String TOKEN_VALUE = "refresh-token-123";
    private static final long EXPIRES_IN_SECONDS = 604800L;

    @Mock private RefreshTokenCacheCommandPort refreshTokenCacheCommandPort;

    private RefreshTokenCacheManager refreshTokenCacheManager;

    @BeforeEach
    void setUp() {
        refreshTokenCacheManager = new RefreshTokenCacheManager(refreshTokenCacheCommandPort);
    }

    @Nested
    @DisplayName("persist")
    class PersistTest {

        @Test
        @DisplayName("캐시에 토큰 저장")
        void shouldPersistToCache() {
            // When
            refreshTokenCacheManager.persist(TOKEN_VALUE, MEMBER_ID, EXPIRES_IN_SECONDS);

            // Then
            verify(refreshTokenCacheCommandPort)
                    .persist(
                            any(RefreshTokenCacheKey.class), eq(MEMBER_ID), eq(EXPIRES_IN_SECONDS));
        }
    }

    @Nested
    @DisplayName("delete")
    class DeleteTest {

        @Test
        @DisplayName("캐시에서 토큰 삭제")
        void shouldDeleteFromCache() {
            // When
            refreshTokenCacheManager.delete(TOKEN_VALUE);

            // Then
            verify(refreshTokenCacheCommandPort).delete(any(RefreshTokenCacheKey.class));
        }
    }
}
