package com.ryuqq.setof.application.auth.manager.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheQueryPort;
import com.ryuqq.setof.domain.auth.vo.RefreshTokenCacheKey;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenCacheReadManager")
class RefreshTokenCacheReadManagerTest {

    private static final String MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
    private static final String TOKEN_VALUE = "refresh-token-123";

    @Mock private RefreshTokenCacheQueryPort refreshTokenCacheQueryPort;

    private RefreshTokenCacheReadManager refreshTokenCacheReadManager;

    @BeforeEach
    void setUp() {
        refreshTokenCacheReadManager = new RefreshTokenCacheReadManager(refreshTokenCacheQueryPort);
    }

    @Nested
    @DisplayName("findMemberIdByToken")
    class FindMemberIdByTokenTest {

        @Test
        @DisplayName("토큰으로 회원 ID 조회 성공")
        void shouldFindMemberIdByToken() {
            // Given
            when(refreshTokenCacheQueryPort.findMemberIdByToken(any(RefreshTokenCacheKey.class)))
                    .thenReturn(Optional.of(MEMBER_ID));

            // When
            Optional<String> result = refreshTokenCacheReadManager.findMemberIdByToken(TOKEN_VALUE);

            // Then
            assertTrue(result.isPresent());
            assertEquals(MEMBER_ID, result.get());
            verify(refreshTokenCacheQueryPort).findMemberIdByToken(any(RefreshTokenCacheKey.class));
        }

        @Test
        @DisplayName("토큰으로 회원 ID 조회 실패 - 캐시에 없음")
        void shouldReturnEmptyWhenNotInCache() {
            // Given
            when(refreshTokenCacheQueryPort.findMemberIdByToken(any(RefreshTokenCacheKey.class)))
                    .thenReturn(Optional.empty());

            // When
            Optional<String> result = refreshTokenCacheReadManager.findMemberIdByToken(TOKEN_VALUE);

            // Then
            assertTrue(result.isEmpty());
        }
    }
}
