package com.ryuqq.setof.application.auth.manager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheCommandPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenCacheCommandManager 단위 테스트")
class RefreshTokenCacheCommandManagerTest {

    @InjectMocks private RefreshTokenCacheCommandManager sut;

    @Mock private RefreshTokenCacheCommandPort refreshTokenCacheCommandPort;

    @Nested
    @DisplayName("persist() - Refresh Token 캐시 저장")
    class PersistTest {

        @Test
        @DisplayName("Refresh Token을 캐시에 저장할 때 RefreshTokenCacheCommandPort.persist를 호출한다")
        void persist_ValidToken_CallsCommandPort() {
            // given
            String refreshToken = "test.refresh.token";
            String memberId = "1";
            long expiresInSeconds = 86400L;

            willDoNothing()
                    .given(refreshTokenCacheCommandPort)
                    .persist(any(), eq(memberId), eq(expiresInSeconds));

            // when
            sut.persist(refreshToken, memberId, expiresInSeconds);

            // then
            then(refreshTokenCacheCommandPort)
                    .should()
                    .persist(any(), eq(memberId), eq(expiresInSeconds));
        }

        @Test
        @DisplayName("다른 만료 시간으로 Refresh Token을 저장할 때 RefreshTokenCacheCommandPort.persist를 호출한다")
        void persist_DifferentExpiry_CallsCommandPort() {
            // given
            String refreshToken = "another.refresh.token";
            String memberId = "2";
            long expiresInSeconds = 3600L;

            willDoNothing()
                    .given(refreshTokenCacheCommandPort)
                    .persist(any(), eq(memberId), eq(expiresInSeconds));

            // when
            sut.persist(refreshToken, memberId, expiresInSeconds);

            // then
            then(refreshTokenCacheCommandPort)
                    .should()
                    .persist(any(), eq(memberId), eq(expiresInSeconds));
        }
    }

    @Nested
    @DisplayName("delete() - Refresh Token 캐시 삭제")
    class DeleteTest {

        @Test
        @DisplayName("Refresh Token 캐시를 삭제할 때 RefreshTokenCacheCommandPort.delete를 호출한다")
        void delete_ValidToken_CallsCommandPort() {
            // given
            String refreshToken = "test.refresh.token";
            willDoNothing().given(refreshTokenCacheCommandPort).delete(any());

            // when
            sut.delete(refreshToken);

            // then
            then(refreshTokenCacheCommandPort).should().delete(any());
        }

        @Test
        @DisplayName("다른 Refresh Token 삭제 시 RefreshTokenCacheCommandPort.delete를 호출한다")
        void delete_AnotherToken_CallsCommandPort() {
            // given
            String refreshToken = "another.refresh.token";
            willDoNothing().given(refreshTokenCacheCommandPort).delete(any());

            // when
            sut.delete(refreshToken);

            // then
            then(refreshTokenCacheCommandPort).should().delete(any());
        }
    }
}
