package com.ryuqq.setof.application.member.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.member.MemberCommandFixtures;
import com.ryuqq.setof.application.member.dto.command.ResetPasswordCommand;
import com.ryuqq.setof.application.member.dto.command.UpdatePasswordContext;
import com.ryuqq.setof.application.member.factory.MemberCommandFactory;
import com.ryuqq.setof.application.member.manager.MemberCommandManager;
import com.ryuqq.setof.application.member.validator.MemberValidator;
import com.ryuqq.setof.domain.member.MemberFixtures;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.MemberNotFoundException;
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
@DisplayName("ResetPasswordService 단위 테스트")
class ResetPasswordServiceTest {

    @InjectMocks private ResetPasswordService sut;

    @Mock private MemberValidator memberValidator;
    @Mock private MemberCommandFactory memberCommandFactory;
    @Mock private MemberCommandManager memberCommandManager;

    @Nested
    @DisplayName("execute() - 비밀번호 재설정")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 비밀번호를 재설정한다")
        void execute_ValidCommand_ResetsPassword() {
            // given
            ResetPasswordCommand command = MemberCommandFixtures.resetPasswordCommand();
            Member member = MemberFixtures.activeMigratedMember();
            UpdatePasswordContext context = MemberCommandFixtures.updatePasswordContext();

            given(memberValidator.getByPhoneNumber(command.phoneNumber())).willReturn(member);
            given(memberCommandFactory.createUpdatePasswordContext(member, command.newPassword()))
                    .willReturn(context);
            willDoNothing().given(memberCommandManager).persist(context);

            // when
            sut.execute(command);

            // then
            then(memberValidator).should().getByPhoneNumber(command.phoneNumber());
            then(memberCommandFactory)
                    .should()
                    .createUpdatePasswordContext(member, command.newPassword());
            then(memberCommandManager).should().persist(context);
        }

        @Test
        @DisplayName("존재하지 않는 전화번호면 MemberNotFoundException이 발생한다")
        void execute_NonExistingPhone_ThrowsMemberNotFoundException() {
            // given
            ResetPasswordCommand command =
                    MemberCommandFixtures.resetPasswordCommand("01000000000", "newPw123!");

            given(memberValidator.getByPhoneNumber(command.phoneNumber()))
                    .willThrow(new MemberNotFoundException(command.phoneNumber()));

            // when & then
            assertThatThrownBy(() -> sut.execute(command))
                    .isInstanceOf(MemberNotFoundException.class);

            then(memberCommandFactory).shouldHaveNoInteractions();
            then(memberCommandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("Validator에서 조회한 회원이 Factory에 그대로 전달된다")
        void execute_ValidCommand_MemberPassedToFactory() {
            // given
            ResetPasswordCommand command = MemberCommandFixtures.resetPasswordCommand();
            Member member = MemberFixtures.activeMigratedMember();
            UpdatePasswordContext context = MemberCommandFixtures.updatePasswordContext();

            given(memberValidator.getByPhoneNumber(command.phoneNumber())).willReturn(member);
            given(memberCommandFactory.createUpdatePasswordContext(member, command.newPassword()))
                    .willReturn(context);
            willDoNothing().given(memberCommandManager).persist(context);

            // when
            sut.execute(command);

            // then
            then(memberCommandFactory)
                    .should()
                    .createUpdatePasswordContext(member, command.newPassword());
        }
    }
}
