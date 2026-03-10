package com.ryuqq.setof.application.member.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.member.MemberCommandFixtures;
import com.ryuqq.setof.application.member.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.member.dto.command.WithdrawalCommand;
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
@DisplayName("WithdrawalService 단위 테스트")
class WithdrawalServiceTest {

    @InjectMocks private WithdrawalService sut;

    @Mock private MemberValidator memberValidator;
    @Mock private MemberCommandFactory memberCommandFactory;
    @Mock private MemberCommandManager memberCommandManager;

    @Nested
    @DisplayName("execute() - 회원 탈퇴")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 회원 탈퇴를 처리한다")
        void execute_ValidCommand_ProcessesWithdrawal() {
            // given
            WithdrawalCommand command = MemberCommandFixtures.withdrawalCommand();
            Member member = MemberFixtures.activeMigratedMember();
            StatusChangeContext context = MemberCommandFixtures.statusChangeContext();

            given(memberValidator.getByLegacyId(command.userId())).willReturn(member);
            given(
                            memberCommandFactory.createStatusChangeContext(
                                    member, command.withdrawalReason()))
                    .willReturn(context);
            willDoNothing().given(memberCommandManager).persist(context);

            // when
            sut.execute(command);

            // then
            then(memberValidator).should().getByLegacyId(command.userId());
            then(memberCommandFactory)
                    .should()
                    .createStatusChangeContext(member, command.withdrawalReason());
            then(memberCommandManager).should().persist(context);
        }

        @Test
        @DisplayName("존재하지 않는 userId면 MemberNotFoundException이 발생한다")
        void execute_NonExistingUser_ThrowsMemberNotFoundException() {
            // given
            WithdrawalCommand command = MemberCommandFixtures.withdrawalCommand(99999L, "탈퇴 사유");

            given(memberValidator.getByLegacyId(command.userId()))
                    .willThrow(new MemberNotFoundException(String.valueOf(command.userId())));

            // when & then
            assertThatThrownBy(() -> sut.execute(command))
                    .isInstanceOf(MemberNotFoundException.class);

            then(memberCommandFactory).shouldHaveNoInteractions();
            then(memberCommandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("Validator에서 조회한 회원과 탈퇴 사유가 Factory에 전달된다")
        void execute_ValidCommand_MemberAndReasonPassedToFactory() {
            // given
            WithdrawalCommand command = MemberCommandFixtures.withdrawalCommand();
            Member member = MemberFixtures.activeMigratedMember();
            StatusChangeContext context = MemberCommandFixtures.statusChangeContext();

            given(memberValidator.getByLegacyId(command.userId())).willReturn(member);
            given(
                            memberCommandFactory.createStatusChangeContext(
                                    member, command.withdrawalReason()))
                    .willReturn(context);
            willDoNothing().given(memberCommandManager).persist(context);

            // when
            sut.execute(command);

            // then
            then(memberCommandFactory)
                    .should()
                    .createStatusChangeContext(member, command.withdrawalReason());
        }
    }
}
