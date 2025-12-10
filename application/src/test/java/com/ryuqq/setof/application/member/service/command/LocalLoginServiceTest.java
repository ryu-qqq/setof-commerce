package com.ryuqq.setof.application.member.service.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.auth.port.in.command.IssueTokensUseCase;
import com.ryuqq.setof.application.member.assembler.MemberAssembler;
import com.ryuqq.setof.application.member.dto.command.LocalLoginCommand;
import com.ryuqq.setof.application.member.dto.response.LocalLoginResponse;
import com.ryuqq.setof.application.member.manager.query.MemberReadManager;
import com.ryuqq.setof.application.member.validator.MemberPolicyValidator;
import com.ryuqq.setof.domain.member.MemberFixture;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("LocalLoginService")
class LocalLoginServiceTest {

    @Mock private MemberReadManager memberReadManager;

    @Mock private MemberPolicyValidator memberPolicyValidator;

    @Mock private IssueTokensUseCase issueTokensUseCase;

    @Mock private MemberAssembler memberAssembler;

    private LocalLoginService localLoginService;

    @BeforeEach
    void setUp() {
        localLoginService =
                new LocalLoginService(
                        memberReadManager,
                        memberPolicyValidator,
                        issueTokensUseCase,
                        memberAssembler);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("로그인 성공 시 토큰 발급")
        void shouldIssueTokensOnSuccessfulLogin() {
            // Given
            String phoneNumber = "01012345678";
            String rawPassword = "password123!";
            LocalLoginCommand command = new LocalLoginCommand(phoneNumber, rawPassword);

            Member member = MemberFixture.createLocalMember();
            TokenPairResponse tokens = new TokenPairResponse("access", 3600L, "refresh", 604800L);
            LocalLoginResponse expectedResponse =
                    new LocalLoginResponse(member.getIdValue(), tokens);

            when(memberReadManager.findByPhoneNumber(phoneNumber)).thenReturn(member);
            when(issueTokensUseCase.execute(member.getIdValue())).thenReturn(tokens);
            when(memberAssembler.toLocalLoginResponse(member.getIdValue(), tokens))
                    .thenReturn(expectedResponse);

            // When
            LocalLoginResponse result = localLoginService.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(member.getIdValue(), result.memberId());
            verify(memberPolicyValidator).validateCanLogin(member, rawPassword);
            verify(issueTokensUseCase).execute(member.getIdValue());
        }

        @Test
        @DisplayName("회원 조회 후 정책 검증 수행")
        void shouldValidatePolicyAfterFindingMember() {
            // Given
            String phoneNumber = "01012345678";
            String rawPassword = "password123!";
            LocalLoginCommand command = new LocalLoginCommand(phoneNumber, rawPassword);

            Member member = MemberFixture.createLocalMember();
            TokenPairResponse tokens = new TokenPairResponse("access", 3600L, "refresh", 604800L);
            LocalLoginResponse expectedResponse =
                    new LocalLoginResponse(member.getIdValue(), tokens);

            when(memberReadManager.findByPhoneNumber(phoneNumber)).thenReturn(member);
            when(issueTokensUseCase.execute(member.getIdValue())).thenReturn(tokens);
            when(memberAssembler.toLocalLoginResponse(member.getIdValue(), tokens))
                    .thenReturn(expectedResponse);

            // When
            localLoginService.execute(command);

            // Then
            verify(memberReadManager).findByPhoneNumber(phoneNumber);
            verify(memberPolicyValidator).validateCanLogin(member, rawPassword);
        }
    }
}
