package com.ryuqq.setof.application.auth.service.command;

import static org.mockito.Mockito.verify;

import com.ryuqq.setof.application.auth.facade.command.RefreshTokenFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("RevokeTokensService")
class RevokeTokensServiceTest {

    private static final String MEMBER_ID = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
    private static final String REFRESH_TOKEN = "refresh-token-456";

    @Mock private RefreshTokenFacade refreshTokenFacade;

    private RevokeTokensService revokeTokensService;

    @BeforeEach
    void setUp() {
        revokeTokensService = new RevokeTokensService(refreshTokenFacade);
    }

    @Nested
    @DisplayName("revokeByMemberId")
    class RevokeByMemberIdTest {

        @Test
        @DisplayName("회원 ID로 토큰 무효화")
        void shouldRevokeTokensByMemberId() {
            // When
            revokeTokensService.revokeByMemberId(MEMBER_ID);

            // Then
            verify(refreshTokenFacade).deleteByMemberId(MEMBER_ID);
        }
    }

    @Nested
    @DisplayName("revokeByToken")
    class RevokeByTokenTest {

        @Test
        @DisplayName("토큰 값으로 토큰 무효화")
        void shouldRevokeTokenByTokenValue() {
            // When
            revokeTokensService.revokeByToken(REFRESH_TOKEN);

            // Then
            verify(refreshTokenFacade).deleteByToken(REFRESH_TOKEN);
        }
    }
}
