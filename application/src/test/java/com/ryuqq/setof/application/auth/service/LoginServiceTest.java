package com.ryuqq.setof.application.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.auth.AuthCommandFixtures;
import com.ryuqq.setof.application.auth.AuthResponseFixtures;
import com.ryuqq.setof.application.auth.dto.command.LoginCommand;
import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.auth.manager.TokenCommandFacade;
import com.ryuqq.setof.application.member.validator.LoginValidator;
import com.ryuqq.setof.domain.member.aggregate.Member;
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
@DisplayName("LoginService 단위 테스트")
class LoginServiceTest {

    @InjectMocks private LoginService sut;

    @Mock private LoginValidator loginValidator;
    @Mock private TokenCommandFacade tokenCommandFacade;
    @Mock private Member member;

    @Nested
    @DisplayName("execute() - 로그인")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 로그인을 수행하고 LoginResult를 반환한다")
        void execute_ValidCommand_ReturnsLoginResult() {
            // given
            LoginCommand command = AuthCommandFixtures.loginCommand();
            long legacyMemberId = 1L;
            LoginResult expectedResult = AuthResponseFixtures.loginResultSuccess();

            given(loginValidator.validate(command.identifier(), command.password()))
                    .willReturn(member);
            given(member.legacyMemberIdValue()).willReturn(legacyMemberId);
            given(tokenCommandFacade.issueLoginResult(legacyMemberId)).willReturn(expectedResult);

            // when
            LoginResult result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedResult);
            assertThat(result.isSuccess()).isTrue();
            then(loginValidator).should().validate(command.identifier(), command.password());
            then(member).should().legacyMemberIdValue();
            then(tokenCommandFacade).should().issueLoginResult(legacyMemberId);
        }

        @Test
        @DisplayName("다른 식별자로 로그인 시 loginValidator.validate를 호출한다")
        void execute_DifferentIdentifier_CallsLoginValidator() {
            // given
            LoginCommand command = AuthCommandFixtures.loginCommand("010-9999-8888", "otherPass");
            long legacyMemberId = 2L;
            LoginResult expectedResult = AuthResponseFixtures.loginResultSuccess("2");

            given(loginValidator.validate(command.identifier(), command.password()))
                    .willReturn(member);
            given(member.legacyMemberIdValue()).willReturn(legacyMemberId);
            given(tokenCommandFacade.issueLoginResult(legacyMemberId)).willReturn(expectedResult);

            // when
            LoginResult result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedResult);
            then(loginValidator).should().validate(command.identifier(), command.password());
            then(tokenCommandFacade).should().issueLoginResult(legacyMemberId);
        }
    }
}
