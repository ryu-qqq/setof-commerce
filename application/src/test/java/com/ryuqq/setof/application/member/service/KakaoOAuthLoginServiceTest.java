package com.ryuqq.setof.application.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.assembler.MemberAssembler;
import com.ryuqq.setof.application.member.component.KakaoOAuthPolicyValidator;
import com.ryuqq.setof.application.member.component.MemberCreator;
import com.ryuqq.setof.application.member.component.MemberReader;
import com.ryuqq.setof.application.member.component.MemberUpdater;
import com.ryuqq.setof.application.member.dto.command.IntegrateKakaoCommand;
import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import com.ryuqq.setof.application.member.dto.response.KakaoOAuthResponse;
import com.ryuqq.setof.application.member.dto.response.RegisterMemberResponse;
import com.ryuqq.setof.application.member.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.facade.RegisterMemberFacade;
import com.ryuqq.setof.application.member.manager.MemberPersistenceManager;
import com.ryuqq.setof.application.member.manager.TokenManager;
import com.ryuqq.setof.domain.core.member.MemberFixture;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.exception.InactiveMemberException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("KakaoOAuthLoginService")
@ExtendWith(MockitoExtension.class)
class KakaoOAuthLoginServiceTest {

    @Mock
    private MemberReader memberReader;

    @Mock
    private MemberCreator memberCreator;

    @Mock
    private MemberUpdater memberUpdater;

    @Mock
    private MemberAssembler memberAssembler;

    @Mock
    private KakaoOAuthPolicyValidator kakaoOAuthPolicyValidator;

    @Mock
    private RegisterMemberFacade registerMemberFacade;

    @Mock
    private MemberPersistenceManager memberPersistenceManager;

    @Mock
    private TokenManager tokenManager;

    private KakaoOAuthLoginService service;

    @BeforeEach
    void setUp() {
        service = new KakaoOAuthLoginService(
                memberReader, memberCreator, memberUpdater, memberAssembler,
                kakaoOAuthPolicyValidator, registerMemberFacade,
                memberPersistenceManager, tokenManager);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("기존 카카오 회원 로그인 시 검증 후 토큰 발급")
        void shouldValidateAndReturnTokensWhenExistingKakaoMember() {
            // Given
            String kakaoId = "kakao_12345";
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            KakaoOAuthCommand command = createKakaoCommand(kakaoId, "01012345678");
            Member kakaoMember = MemberFixture.createKakaoMemberWithSocialId(kakaoId);
            TokenPairResponse tokens = createTokenPair();
            KakaoOAuthResponse expectedResponse = KakaoOAuthResponse.existingKakaoMember(memberId, tokens);

            when(memberReader.findBySocialId(kakaoId)).thenReturn(Optional.of(kakaoMember));
            when(tokenManager.issueTokens(kakaoMember.getIdValue())).thenReturn(tokens);
            when(memberAssembler.toExistingKakaoMemberResponse(kakaoMember.getIdValue(), tokens))
                    .thenReturn(expectedResponse);

            // When
            KakaoOAuthResponse result = service.execute(command);

            // Then
            assertNotNull(result);
            assertFalse(result.isNewMember());
            assertFalse(result.needsIntegration());
            assertNotNull(result.tokens());

            verify(memberReader).findBySocialId(kakaoId);
            verify(kakaoOAuthPolicyValidator).validateCanKakaoLogin(kakaoMember);
            verify(tokenManager).issueTokens(kakaoMember.getIdValue());
            verify(memberAssembler).toExistingKakaoMemberResponse(kakaoMember.getIdValue(), tokens);
            verify(memberReader, never()).findByPhoneNumber(anyString());
            verify(memberCreator, never()).createKakaoMember(any());
        }

        @Test
        @DisplayName("비활성 카카오 회원 로그인 시 InactiveMemberException 발생")
        void shouldThrowExceptionWhenInactiveKakaoMember() {
            // Given
            String kakaoId = "kakao_12345";
            KakaoOAuthCommand command = createKakaoCommand(kakaoId, "01012345678");
            Member kakaoMember = MemberFixture.createKakaoMemberWithSocialId(kakaoId);

            when(memberReader.findBySocialId(kakaoId)).thenReturn(Optional.of(kakaoMember));
            doThrow(new InactiveMemberException())
                    .when(kakaoOAuthPolicyValidator).validateCanKakaoLogin(kakaoMember);

            // When & Then
            assertThrows(InactiveMemberException.class, () -> service.execute(command));

            verify(memberReader).findBySocialId(kakaoId);
            verify(kakaoOAuthPolicyValidator).validateCanKakaoLogin(kakaoMember);
            verify(tokenManager, never()).issueTokens(anyString());
        }

        @Test
        @DisplayName("동일 핸드폰 LOCAL 회원 존재 시 카카오 연동 (계정 통합)")
        void shouldIntegrateLocalMemberWhenPhoneNumberMatches() {
            // Given
            String kakaoId = "kakao_12345";
            String phoneNumber = "01012345678";
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            KakaoOAuthCommand command = createKakaoCommand(kakaoId, phoneNumber);
            Member localMember = MemberFixture.createLocalMemberWithId(memberId);
            TokenPairResponse tokens = createTokenPair();
            KakaoOAuthResponse expectedResponse = KakaoOAuthResponse.existingKakaoMember(memberId, tokens);

            when(memberReader.findBySocialId(kakaoId)).thenReturn(Optional.empty());
            when(memberReader.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(localMember));
            when(tokenManager.issueTokens(localMember.getIdValue())).thenReturn(tokens);
            when(memberAssembler.toExistingKakaoMemberResponse(localMember.getIdValue(), tokens))
                    .thenReturn(expectedResponse);

            // When
            KakaoOAuthResponse result = service.execute(command);

            // Then
            assertNotNull(result);
            assertFalse(result.isNewMember());
            assertFalse(result.needsIntegration());
            assertNotNull(result.tokens());

            verify(memberReader).findBySocialId(kakaoId);
            verify(memberReader).findByPhoneNumber(phoneNumber);
            verify(memberUpdater).linkKakaoWithProfile(eq(localMember), any(IntegrateKakaoCommand.class));
            verify(memberPersistenceManager).persist(localMember);
            verify(tokenManager).issueTokens(localMember.getIdValue());
            verify(memberCreator, never()).createKakaoMember(any());
        }

        @Test
        @DisplayName("신규 카카오 회원 자동 가입 및 토큰 발급")
        void shouldRegisterNewKakaoMemberAndReturnTokens() {
            // Given
            String kakaoId = "kakao_12345";
            String phoneNumber = "01012345678";
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            KakaoOAuthCommand command = createKakaoCommand(kakaoId, phoneNumber);
            Member newKakaoMember = MemberFixture.createKakaoMemberWithSocialId(kakaoId);
            TokenPairResponse tokens = createTokenPair();
            RegisterMemberResponse registerResponse = new RegisterMemberResponse(memberId, tokens);
            KakaoOAuthResponse expectedResponse = KakaoOAuthResponse.newMember(memberId, tokens);

            when(memberReader.findBySocialId(kakaoId)).thenReturn(Optional.empty());
            when(memberReader.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());
            when(memberCreator.createKakaoMember(command)).thenReturn(newKakaoMember);
            when(registerMemberFacade.register(newKakaoMember)).thenReturn(registerResponse);
            when(memberAssembler.toNewKakaoMemberResponse(memberId, tokens))
                    .thenReturn(expectedResponse);

            // When
            KakaoOAuthResponse result = service.execute(command);

            // Then
            assertNotNull(result);
            assertTrue(result.isNewMember());
            assertFalse(result.needsIntegration());
            assertNotNull(result.tokens());

            verify(memberReader).findBySocialId(kakaoId);
            verify(memberReader).findByPhoneNumber(phoneNumber);
            verify(memberCreator).createKakaoMember(command);
            verify(registerMemberFacade).register(newKakaoMember);
            verify(memberAssembler).toNewKakaoMemberResponse(memberId, tokens);
        }
    }

    private KakaoOAuthCommand createKakaoCommand(String kakaoId, String phoneNumber) {
        return new KakaoOAuthCommand(
                kakaoId,
                phoneNumber,
                "test@example.com",
                "테스트",
                LocalDate.of(1990, 1, 1),
                "M",
                Collections.emptyList());
    }

    private TokenPairResponse createTokenPair() {
        return new TokenPairResponse(
                "access_token_123",
                "refresh_token_456",
                3600L,
                604800L);
    }
}
