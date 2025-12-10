package com.ryuqq.setof.application.auth.manager.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.auth.port.out.query.RefreshTokenQueryPort;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenReadManager")
class RefreshTokenReadManagerTest {

    private static final String MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
    private static final String TOKEN_VALUE = "refresh-token-123";

    @Mock private RefreshTokenQueryPort refreshTokenQueryPort;

    private RefreshTokenReadManager refreshTokenReadManager;

    @BeforeEach
    void setUp() {
        refreshTokenReadManager = new RefreshTokenReadManager(refreshTokenQueryPort);
    }

    @Nested
    @DisplayName("findTokenByMemberId")
    class FindTokenByMemberIdTest {

        @Test
        @DisplayName("회원 ID로 토큰 조회 성공")
        void shouldFindTokenByMemberId() {
            // Given
            when(refreshTokenQueryPort.findTokenByMemberId(MEMBER_ID))
                    .thenReturn(Optional.of(TOKEN_VALUE));

            // When
            Optional<String> result = refreshTokenReadManager.findTokenByMemberId(MEMBER_ID);

            // Then
            assertTrue(result.isPresent());
            assertEquals(TOKEN_VALUE, result.get());
            verify(refreshTokenQueryPort).findTokenByMemberId(MEMBER_ID);
        }

        @Test
        @DisplayName("회원 ID로 토큰 조회 실패 - 토큰 없음")
        void shouldReturnEmptyWhenNoToken() {
            // Given
            when(refreshTokenQueryPort.findTokenByMemberId(MEMBER_ID)).thenReturn(Optional.empty());

            // When
            Optional<String> result = refreshTokenReadManager.findTokenByMemberId(MEMBER_ID);

            // Then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("findMemberIdByToken")
    class FindMemberIdByTokenTest {

        @Test
        @DisplayName("토큰으로 회원 ID 조회 성공")
        void shouldFindMemberIdByToken() {
            // Given
            when(refreshTokenQueryPort.findMemberIdByToken(TOKEN_VALUE))
                    .thenReturn(Optional.of(MEMBER_ID));

            // When
            Optional<String> result = refreshTokenReadManager.findMemberIdByToken(TOKEN_VALUE);

            // Then
            assertTrue(result.isPresent());
            assertEquals(MEMBER_ID, result.get());
            verify(refreshTokenQueryPort).findMemberIdByToken(TOKEN_VALUE);
        }

        @Test
        @DisplayName("토큰으로 회원 ID 조회 실패 - 회원 없음")
        void shouldReturnEmptyWhenNoMember() {
            // Given
            when(refreshTokenQueryPort.findMemberIdByToken(TOKEN_VALUE))
                    .thenReturn(Optional.empty());

            // When
            Optional<String> result = refreshTokenReadManager.findMemberIdByToken(TOKEN_VALUE);

            // Then
            assertTrue(result.isEmpty());
        }
    }
}
