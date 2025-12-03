package com.ryuqq.setof.application.member.manager;

import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.port.out.command.RefreshTokenPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("RefreshTokenPersistenceManager")
@ExtendWith(MockitoExtension.class)
class RefreshTokenPersistenceManagerTest {

    @Mock
    private RefreshTokenPersistencePort refreshTokenPersistencePort;

    private RefreshTokenPersistenceManager manager;

    @BeforeEach
    void setUp() {
        manager = new RefreshTokenPersistenceManager(refreshTokenPersistencePort);
    }

    @Nested
    @DisplayName("save")
    class SaveTest {

        @Test
        @DisplayName("Refresh Token RDB 저장 성공")
        void shouldSaveRefreshToken() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            String refreshToken = "refresh_token_123";
            long expiresInSeconds = 604800L;

            // When
            manager.save(memberId, refreshToken, expiresInSeconds);

            // Then
            verify(refreshTokenPersistencePort).save(memberId, refreshToken, expiresInSeconds);
        }

        @Test
        @DisplayName("만료 시간이 다른 Refresh Token 저장 성공")
        void shouldSaveRefreshTokenWithDifferentExpiry() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            String refreshToken = "refresh_token_456";
            long expiresInSeconds = 86400L; // 1일

            // When
            manager.save(memberId, refreshToken, expiresInSeconds);

            // Then
            verify(refreshTokenPersistencePort).save(memberId, refreshToken, expiresInSeconds);
        }
    }

    @Nested
    @DisplayName("deleteByMemberId")
    class DeleteByMemberIdTest {

        @Test
        @DisplayName("회원 ID로 Refresh Token 삭제 성공")
        void shouldDeleteRefreshTokenByMemberId() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";

            // When
            manager.deleteByMemberId(memberId);

            // Then
            verify(refreshTokenPersistencePort).deleteByMemberId(memberId);
        }
    }

    @Nested
    @DisplayName("deleteByToken")
    class DeleteByTokenTest {

        @Test
        @DisplayName("토큰 값으로 Refresh Token 삭제 성공")
        void shouldDeleteRefreshTokenByValue() {
            // Given
            String refreshToken = "refresh_token_789";

            // When
            manager.deleteByToken(refreshToken);

            // Then
            verify(refreshTokenPersistencePort).deleteByToken(refreshToken);
        }
    }
}
