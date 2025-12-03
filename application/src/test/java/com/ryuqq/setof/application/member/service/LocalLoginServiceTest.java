package com.ryuqq.setof.application.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.assembler.MemberAssembler;
import com.ryuqq.setof.application.member.component.MemberPolicyValidator;
import com.ryuqq.setof.application.member.component.MemberReader;
import com.ryuqq.setof.application.member.dto.command.LocalLoginCommand;
import com.ryuqq.setof.application.member.dto.response.LocalLoginResponse;
import com.ryuqq.setof.application.member.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.manager.TokenManager;
import com.ryuqq.setof.domain.core.member.MemberFixture;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.exception.AlreadyKakaoMemberException;
import com.ryuqq.setof.domain.core.member.exception.InactiveMemberException;
import com.ryuqq.setof.domain.core.member.exception.InvalidPasswordException;
import com.ryuqq.setof.domain.core.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("LocalLoginService")
@ExtendWith(MockitoExtension.class)
class LocalLoginServiceTest {

    @Mock
    private MemberReader memberReader;

    @Mock
    private MemberPolicyValidator memberPolicyValidator;

    @Mock
    private TokenManager tokenManager;

    @Mock
    private MemberAssembler memberAssembler;

    private LocalLoginService service;

    @BeforeEach
    void setUp() {
        service = new LocalLoginService(memberReader, memberPolicyValidator, tokenManager, memberAssembler);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("LOCAL 회원 로그인 성공 시 토큰 발급")
        void shouldLoginSuccessfullyAndReturnTokens() {
            // Given
            String phoneNumber = "01012345678";
            String rawPassword = "password123!";
            LocalLoginCommand command = new LocalLoginCommand(phoneNumber, rawPassword);

            Member localMember = MemberFixture.createLocalMember();
            String memberId = localMember.getIdValue();
            TokenPairResponse tokens = createTokenPair();
            LocalLoginResponse expectedResponse = new LocalLoginResponse(memberId, tokens);

            when(memberReader.getByPhoneNumber(phoneNumber)).thenReturn(localMember);
            when(tokenManager.issueTokens(memberId)).thenReturn(tokens);
            when(memberAssembler.toLocalLoginResponse(memberId, tokens)).thenReturn(expectedResponse);

            // When
            LocalLoginResponse result = service.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(memberId, result.memberId());
            assertNotNull(result.tokens());

            verify(memberReader).getByPhoneNumber(phoneNumber);
            verify(memberPolicyValidator).validateCanLogin(localMember, rawPassword);
            verify(tokenManager).issueTokens(memberId);
            verify(memberAssembler).toLocalLoginResponse(memberId, tokens);
        }

        @Test
        @DisplayName("존재하지 않는 회원 로그인 시 MemberNotFoundException 발생")
        void shouldThrowExceptionWhenMemberNotFound() {
            // Given
            String phoneNumber = "01099999999";
            String rawPassword = "password123!";
            LocalLoginCommand command = new LocalLoginCommand(phoneNumber, rawPassword);

            when(memberReader.getByPhoneNumber(phoneNumber))
                    .thenThrow(new MemberNotFoundException());

            // When & Then
            assertThrows(MemberNotFoundException.class, () -> service.execute(command));

            verify(memberReader).getByPhoneNumber(phoneNumber);
            verify(memberPolicyValidator, never()).validateCanLogin(any(), any());
            verify(tokenManager, never()).issueTokens(anyString());
        }

        @Test
        @DisplayName("카카오 회원 로그인 시 AlreadyKakaoMemberException 발생")
        void shouldThrowExceptionWhenKakaoMember() {
            // Given
            String phoneNumber = "01012345678";
            String rawPassword = "password123!";
            LocalLoginCommand command = new LocalLoginCommand(phoneNumber, rawPassword);

            Member kakaoMember = MemberFixture.createKakaoMember();

            when(memberReader.getByPhoneNumber(phoneNumber)).thenReturn(kakaoMember);
            doThrow(new AlreadyKakaoMemberException())
                    .when(memberPolicyValidator).validateCanLogin(kakaoMember, rawPassword);

            // When & Then
            assertThrows(AlreadyKakaoMemberException.class, () -> service.execute(command));

            verify(memberReader).getByPhoneNumber(phoneNumber);
            verify(memberPolicyValidator).validateCanLogin(kakaoMember, rawPassword);
            verify(tokenManager, never()).issueTokens(anyString());
        }

        @Test
        @DisplayName("비밀번호 불일치 시 InvalidPasswordException 발생")
        void shouldThrowExceptionWhenPasswordMismatch() {
            // Given
            String phoneNumber = "01012345678";
            String wrongPassword = "wrongPassword";
            LocalLoginCommand command = new LocalLoginCommand(phoneNumber, wrongPassword);

            Member localMember = MemberFixture.createLocalMember();

            when(memberReader.getByPhoneNumber(phoneNumber)).thenReturn(localMember);
            doThrow(new InvalidPasswordException())
                    .when(memberPolicyValidator).validateCanLogin(localMember, wrongPassword);

            // When & Then
            assertThrows(InvalidPasswordException.class, () -> service.execute(command));

            verify(memberReader).getByPhoneNumber(phoneNumber);
            verify(memberPolicyValidator).validateCanLogin(localMember, wrongPassword);
            verify(tokenManager, never()).issueTokens(anyString());
        }

        @Test
        @DisplayName("비활성 회원 로그인 시 InactiveMemberException 발생")
        void shouldThrowExceptionWhenMemberInactive() {
            // Given
            String phoneNumber = "01012345678";
            String rawPassword = "password123!";
            LocalLoginCommand command = new LocalLoginCommand(phoneNumber, rawPassword);

            Member inactiveMember = MemberFixture.createInactiveMember("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");

            when(memberReader.getByPhoneNumber(phoneNumber)).thenReturn(inactiveMember);
            doThrow(new InactiveMemberException())
                    .when(memberPolicyValidator).validateCanLogin(inactiveMember, rawPassword);

            // When & Then
            assertThrows(InactiveMemberException.class, () -> service.execute(command));

            verify(memberReader).getByPhoneNumber(phoneNumber);
            verify(memberPolicyValidator).validateCanLogin(inactiveMember, rawPassword);
            verify(tokenManager, never()).issueTokens(anyString());
        }
    }

    private TokenPairResponse createTokenPair() {
        return new TokenPairResponse(
                "access_token_123",
                "refresh_token_456",
                3600L,
                604800L);
    }
}
