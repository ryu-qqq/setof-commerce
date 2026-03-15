package com.ryuqq.setof.application.member.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.member.MemberCommandFixtures;
import com.ryuqq.setof.application.member.dto.command.ResetPasswordCommand;
import com.ryuqq.setof.application.member.manager.MemberAuthCommandManager;
import com.ryuqq.setof.application.member.manager.PasswordManager;
import com.ryuqq.setof.application.member.port.out.query.MemberAuthQueryPort;
import com.ryuqq.setof.application.member.validator.MemberValidator;
import com.ryuqq.setof.domain.member.MemberFixtures;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.aggregate.MemberAuth;
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
    @Mock private MemberAuthQueryPort memberAuthQueryPort;
    @Mock private MemberAuthCommandManager memberAuthCommandManager;
    @Mock private PasswordManager passwordManager;

    @Nested
    @DisplayName("execute() - 비밀번호 재설정")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 비밀번호를 재설정한다")
        void execute_ValidCommand_ResetsPassword() {
            // given
            ResetPasswordCommand command = MemberCommandFixtures.resetPasswordCommand();
            Member member = MemberFixtures.activeMember();
            MemberAuth auth = MemberFixtures.activePhoneMemberAuth();
            String encodedPassword = "$2a$10$newEncodedPassword";

            given(memberValidator.getByPhoneNumber(command.phoneNumber())).willReturn(member);
            given(memberAuthQueryPort.findPhoneAuthByMemberId(member.idValue())).willReturn(auth);
            given(passwordManager.encode(command.newPassword())).willReturn(encodedPassword);

            // when
            sut.execute(command);

            // then
            then(memberValidator).should().getByPhoneNumber(command.phoneNumber());
            then(memberAuthQueryPort).should().findPhoneAuthByMemberId(member.idValue());
            then(passwordManager).should().encode(command.newPassword());
            then(memberAuthCommandManager).should().persist(auth);
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

            then(memberAuthQueryPort).shouldHaveNoInteractions();
            then(memberAuthCommandManager).shouldHaveNoInteractions();
            then(passwordManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("Validator에서 조회한 회원의 인증 수단이 변경되어 persist된다")
        void execute_ValidCommand_AuthChangedAndPersisted() {
            // given
            ResetPasswordCommand command = MemberCommandFixtures.resetPasswordCommand();
            Member member = MemberFixtures.activeMember();
            MemberAuth auth = MemberFixtures.activePhoneMemberAuth();
            String encodedPassword = "$2a$10$anotherEncoded";

            given(memberValidator.getByPhoneNumber(command.phoneNumber())).willReturn(member);
            given(memberAuthQueryPort.findPhoneAuthByMemberId(member.idValue())).willReturn(auth);
            given(passwordManager.encode(command.newPassword())).willReturn(encodedPassword);

            // when
            sut.execute(command);

            // then
            then(memberAuthCommandManager).should().persist(auth);
        }
    }
}
