package com.ryuqq.setof.application.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.auth.manager.TokenCommandFacade;
import com.ryuqq.setof.application.member.MemberCommandFixtures;
import com.ryuqq.setof.application.member.dto.command.JoinCommand;
import com.ryuqq.setof.application.member.dto.command.MemberRegistrationInfo;
import com.ryuqq.setof.application.member.factory.MemberCommandFactory;
import com.ryuqq.setof.application.member.manager.MemberCommandManager;
import com.ryuqq.setof.application.member.validator.MemberValidator;
import com.ryuqq.setof.domain.member.MemberFixtures;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.MemberAlreadyRegisteredException;
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
@DisplayName("JoinService 단위 테스트")
class JoinServiceTest {

    @InjectMocks private JoinService sut;

    @Mock private MemberValidator memberValidator;
    @Mock private MemberCommandFactory memberCommandFactory;
    @Mock private MemberCommandManager memberCommandManager;
    @Mock private TokenCommandFacade tokenCommandFacade;

    @Nested
    @DisplayName("execute() - 회원 가입")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 회원 가입 후 LoginResult를 반환한다")
        void execute_ValidCommand_ReturnsLoginResult() {
            // given
            JoinCommand command = MemberCommandFixtures.joinCommand();
            Member member = MemberFixtures.newMember();
            MemberRegistrationInfo registrationInfo =
                    MemberCommandFixtures.memberRegistrationInfo();
            Long userId = MemberCommandFixtures.DEFAULT_USER_ID;
            LoginResult expectedResult =
                    LoginResult.success(
                            String.valueOf(userId), "accessToken", "refreshToken", 3600L, "Bearer");

            willDoNothing().given(memberValidator).validateNotRegistered(command.phoneNumber());
            given(memberCommandFactory.createMember(command)).willReturn(member);
            given(memberCommandFactory.createRegistrationInfo(command))
                    .willReturn(registrationInfo);
            given(memberCommandManager.persist(member, registrationInfo)).willReturn(userId);
            given(tokenCommandFacade.issueLoginResult(userId)).willReturn(expectedResult);

            // when
            LoginResult result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedResult);
            assertThat(result.isSuccess()).isTrue();
            then(memberValidator).should().validateNotRegistered(command.phoneNumber());
            then(memberCommandFactory).should().createMember(command);
            then(memberCommandFactory).should().createRegistrationInfo(command);
            then(memberCommandManager).should().persist(member, registrationInfo);
            then(tokenCommandFacade).should().issueLoginResult(userId);
        }

        @Test
        @DisplayName("이미 등록된 전화번호면 MemberAlreadyRegisteredException이 발생한다")
        void execute_AlreadyRegisteredPhone_ThrowsMemberAlreadyRegisteredException() {
            // given
            JoinCommand command = MemberCommandFixtures.joinCommand();

            willThrow(new MemberAlreadyRegisteredException())
                    .given(memberValidator)
                    .validateNotRegistered(command.phoneNumber());

            // when & then
            assertThatThrownBy(() -> sut.execute(command))
                    .isInstanceOf(MemberAlreadyRegisteredException.class);

            then(memberCommandFactory).shouldHaveNoInteractions();
            then(memberCommandManager).shouldHaveNoInteractions();
            then(tokenCommandFacade).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("검증 통과 후 Factory와 Manager가 순서대로 호출된다")
        void execute_ValidCommand_FactoryAndManagerCalledInOrder() {
            // given
            JoinCommand command = MemberCommandFixtures.joinCommand();
            Member member = MemberFixtures.newMember();
            MemberRegistrationInfo registrationInfo =
                    MemberCommandFixtures.memberRegistrationInfo();
            Long userId = 999L;
            LoginResult loginResult =
                    LoginResult.success(
                            String.valueOf(userId), "token", "refresh", 3600L, "Bearer");

            willDoNothing().given(memberValidator).validateNotRegistered(any());
            given(memberCommandFactory.createMember(command)).willReturn(member);
            given(memberCommandFactory.createRegistrationInfo(command))
                    .willReturn(registrationInfo);
            given(memberCommandManager.persist(member, registrationInfo)).willReturn(userId);
            given(tokenCommandFacade.issueLoginResult(userId)).willReturn(loginResult);

            // when
            sut.execute(command);

            // then
            then(memberValidator).should().validateNotRegistered(command.phoneNumber());
            then(memberCommandFactory).should().createMember(command);
            then(memberCommandFactory).should().createRegistrationInfo(command);
            then(memberCommandManager).should().persist(member, registrationInfo);
            then(tokenCommandFacade).should().issueLoginResult(userId);
        }
    }
}
