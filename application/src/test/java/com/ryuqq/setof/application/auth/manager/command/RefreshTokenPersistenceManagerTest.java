package com.ryuqq.setof.application.auth.manager.command;

import static org.mockito.Mockito.verify;

import com.ryuqq.setof.application.auth.port.out.command.RefreshTokenPersistencePort;
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
@DisplayName("RefreshTokenPersistenceManager")
class RefreshTokenPersistenceManagerTest {

    private static final String MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
    private static final String TOKEN_VALUE = "refresh-token-123";
    private static final long EXPIRES_IN_SECONDS = 604800L;

    @Mock private RefreshTokenPersistencePort refreshTokenPersistencePort;

    private RefreshTokenPersistenceManager refreshTokenPersistenceManager;

    @BeforeEach
    void setUp() {
        refreshTokenPersistenceManager =
                new RefreshTokenPersistenceManager(refreshTokenPersistencePort);
    }

    @Nested
    @DisplayName("persist")
    class PersistTest {

        @Test
        @DisplayName("RefreshToken 저장")
        void shouldPersistRefreshToken() {
            // Given
            RefreshToken refreshToken =
                    RefreshToken.create(MEMBER_ID, TOKEN_VALUE, EXPIRES_IN_SECONDS, Instant.now());

            // When
            refreshTokenPersistenceManager.persist(refreshToken);

            // Then
            verify(refreshTokenPersistencePort).persist(refreshToken);
        }
    }

    @Nested
    @DisplayName("deleteByMemberId")
    class DeleteByMemberIdTest {

        @Test
        @DisplayName("회원 ID로 토큰 삭제")
        void shouldDeleteByMemberId() {
            // When
            refreshTokenPersistenceManager.deleteByMemberId(MEMBER_ID);

            // Then
            verify(refreshTokenPersistencePort).deleteByMemberId(MEMBER_ID);
        }
    }

    @Nested
    @DisplayName("deleteByToken")
    class DeleteByTokenTest {

        @Test
        @DisplayName("토큰 값으로 삭제")
        void shouldDeleteByToken() {
            // When
            refreshTokenPersistenceManager.deleteByToken(TOKEN_VALUE);

            // Then
            verify(refreshTokenPersistencePort).deleteByToken(TOKEN_VALUE);
        }
    }
}
