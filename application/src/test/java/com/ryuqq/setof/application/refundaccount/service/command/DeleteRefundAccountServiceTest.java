package com.ryuqq.setof.application.refundaccount.service.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.refundaccount.RefundAccountCommandFixtures;
import com.ryuqq.setof.application.refundaccount.RefundAccountDomainFixtures;
import com.ryuqq.setof.application.refundaccount.dto.command.DeleteRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.manager.RefundAccountCommandManager;
import com.ryuqq.setof.application.refundaccount.manager.RefundAccountReadManager;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.exception.RefundAccountNotFoundException;
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
@DisplayName("DeleteRefundAccountService 단위 테스트")
class DeleteRefundAccountServiceTest {

    @InjectMocks private DeleteRefundAccountService sut;

    @Mock private RefundAccountReadManager readManager;

    @Mock private RefundAccountCommandManager commandManager;

    @Nested
    @DisplayName("execute() - 환불 계좌 삭제")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 환불 계좌를 삭제하고 readManager와 commandManager를 호출한다")
        void execute_ValidCommand_CallsReadManagerAndCommandManager() {
            // given
            DeleteRefundAccountCommand command = RefundAccountCommandFixtures.deleteCommand();
            Long userId = command.userId();
            Long refundAccountId = command.refundAccountId();
            RefundAccount refundAccount =
                    RefundAccountDomainFixtures.activeRefundAccount(refundAccountId, userId);

            given(readManager.getByUserIdAndId(userId, refundAccountId)).willReturn(refundAccount);
            given(commandManager.persist(any(RefundAccount.class))).willReturn(refundAccountId);

            // when
            sut.execute(command);

            // then
            then(readManager).should().getByUserIdAndId(userId, refundAccountId);
            then(commandManager).should().persist(refundAccount);
        }

        @Test
        @DisplayName("삭제 후 환불 계좌는 삭제 상태가 된다")
        void execute_AfterDelete_AccountIsMarkedDeleted() {
            // given
            DeleteRefundAccountCommand command = RefundAccountCommandFixtures.deleteCommand();
            Long userId = command.userId();
            Long refundAccountId = command.refundAccountId();
            RefundAccount refundAccount =
                    RefundAccountDomainFixtures.activeRefundAccount(refundAccountId, userId);

            given(readManager.getByUserIdAndId(userId, refundAccountId)).willReturn(refundAccount);
            given(commandManager.persist(any(RefundAccount.class))).willReturn(refundAccountId);

            // when
            sut.execute(command);

            // then
            then(commandManager).should().persist(refundAccount);
        }

        @Test
        @DisplayName("존재하지 않는 환불 계좌 삭제 시 RefundAccountNotFoundException이 발생한다")
        void execute_NonExistingAccount_ThrowsException() {
            // given
            DeleteRefundAccountCommand command = RefundAccountCommandFixtures.deleteCommand();
            Long userId = command.userId();
            Long refundAccountId = command.refundAccountId();

            given(readManager.getByUserIdAndId(userId, refundAccountId))
                    .willThrow(RefundAccountNotFoundException.class);

            // when & then
            assertThatThrownBy(() -> sut.execute(command))
                    .isInstanceOf(RefundAccountNotFoundException.class);

            then(commandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("특정 userId와 refundAccountId 커맨드로 readManager를 호출한다")
        void execute_SpecificIds_CallsReadManagerWithCorrectIds() {
            // given
            Long userId = 5L;
            Long refundAccountId = 999L;
            DeleteRefundAccountCommand command =
                    RefundAccountCommandFixtures.deleteCommand(userId, refundAccountId);
            RefundAccount refundAccount =
                    RefundAccountDomainFixtures.activeRefundAccount(refundAccountId, userId);

            given(readManager.getByUserIdAndId(userId, refundAccountId)).willReturn(refundAccount);
            given(commandManager.persist(any(RefundAccount.class))).willReturn(refundAccountId);

            // when
            sut.execute(command);

            // then
            then(readManager).should().getByUserIdAndId(userId, refundAccountId);
        }
    }
}
