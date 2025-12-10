package com.ryuqq.setof.application.member.service.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.auth.port.in.command.IssueTokensUseCase;
import com.ryuqq.setof.application.member.dto.bundle.KakaoOAuthResult;
import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import com.ryuqq.setof.application.member.dto.response.KakaoOAuthResponse;
import com.ryuqq.setof.application.member.facade.command.KakaoOAuthFacade;
import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("KakaoOAuthLoginService")
class KakaoOAuthLoginServiceTest {

    private static final String MEMBER_ID = "member-uuid-123";
    private static final String MEMBER_NAME = "홍길동";

    @Mock private KakaoOAuthFacade kakaoOAuthFacade;

    @Mock private IssueTokensUseCase issueTokensUseCase;

    private KakaoOAuthLoginService kakaoOAuthLoginService;

    @BeforeEach
    void setUp() {
        kakaoOAuthLoginService = new KakaoOAuthLoginService(kakaoOAuthFacade, issueTokensUseCase);
    }

    @Nested
    @DisplayName("execute - 시나리오별 응답 변환")
    class ExecuteTest {

        @Test
        @DisplayName("기존 카카오 회원 - isNewMember=false, wasIntegrated=false")
        void shouldReturnExistingKakaoMemberResponse() {
            // Given
            KakaoOAuthCommand command = createCommand();
            KakaoOAuthResult result = KakaoOAuthResult.existingKakao(MEMBER_ID);
            TokenPairResponse tokens = createTokens();

            when(kakaoOAuthFacade.processKakaoLogin(command)).thenReturn(result);
            when(issueTokensUseCase.execute(MEMBER_ID)).thenReturn(tokens);

            // When
            KakaoOAuthResponse response = kakaoOAuthLoginService.execute(command);

            // Then
            assertNotNull(response);
            assertEquals(MEMBER_ID, response.memberId());
            assertFalse(response.isNewMember());
            assertFalse(response.wasIntegrated());
            verify(kakaoOAuthFacade).processKakaoLogin(command);
            verify(issueTokensUseCase).execute(MEMBER_ID);
        }

        @Test
        @DisplayName("기존 회원 (카카오 연동 없이) - isNewMember=false, wasIntegrated=false")
        void shouldReturnExistingMemberResponse() {
            // Given
            KakaoOAuthCommand command = createCommand();
            KakaoOAuthResult result = KakaoOAuthResult.existingMember(MEMBER_ID);
            TokenPairResponse tokens = createTokens();

            when(kakaoOAuthFacade.processKakaoLogin(command)).thenReturn(result);
            when(issueTokensUseCase.execute(MEMBER_ID)).thenReturn(tokens);

            // When
            KakaoOAuthResponse response = kakaoOAuthLoginService.execute(command);

            // Then
            assertNotNull(response);
            assertEquals(MEMBER_ID, response.memberId());
            assertFalse(response.isNewMember());
            assertFalse(response.wasIntegrated());
        }

        @Test
        @DisplayName("카카오 연동 완료 - isNewMember=false, wasIntegrated=true")
        void shouldReturnIntegratedMemberResponse() {
            // Given
            KakaoOAuthCommand command = createCommand();
            KakaoOAuthResult result = KakaoOAuthResult.integrated(MEMBER_ID, MEMBER_NAME);
            TokenPairResponse tokens = createTokens();

            when(kakaoOAuthFacade.processKakaoLogin(command)).thenReturn(result);
            when(issueTokensUseCase.execute(MEMBER_ID)).thenReturn(tokens);

            // When
            KakaoOAuthResponse response = kakaoOAuthLoginService.execute(command);

            // Then
            assertNotNull(response);
            assertEquals(MEMBER_ID, response.memberId());
            assertEquals(MEMBER_NAME, response.memberName());
            assertFalse(response.isNewMember());
            assertTrue(response.wasIntegrated());
        }

        @Test
        @DisplayName("신규 카카오 회원 - isNewMember=true")
        void shouldReturnNewMemberResponse() {
            // Given
            KakaoOAuthCommand command = createCommand();
            KakaoOAuthResult result = KakaoOAuthResult.newMember(MEMBER_ID);
            TokenPairResponse tokens = createTokens();

            when(kakaoOAuthFacade.processKakaoLogin(command)).thenReturn(result);
            when(issueTokensUseCase.execute(MEMBER_ID)).thenReturn(tokens);

            // When
            KakaoOAuthResponse response = kakaoOAuthLoginService.execute(command);

            // Then
            assertNotNull(response);
            assertEquals(MEMBER_ID, response.memberId());
            assertTrue(response.isNewMember());
            assertFalse(response.wasIntegrated());
        }
    }

    private KakaoOAuthCommand createCommand() {
        return new KakaoOAuthCommand(
                "kakao_12345",
                "01012345678",
                "kakao@example.com",
                "카카오사용자",
                LocalDate.of(1990, 1, 1),
                "M",
                Collections.emptyList(),
                false);
    }

    private TokenPairResponse createTokens() {
        return new TokenPairResponse("access-token", 3600L, "refresh-token", 604800L);
    }
}
