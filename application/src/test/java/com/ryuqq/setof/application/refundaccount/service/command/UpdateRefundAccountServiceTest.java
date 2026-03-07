package com.ryuqq.setof.application.refundaccount.service.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import com.ryuqq.setof.application.refundaccount.RefundAccountCommandFixtures;
import com.ryuqq.setof.application.refundaccount.RefundAccountDomainFixtures;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.factory.RefundAccountCommandFactory;
import com.ryuqq.setof.application.refundaccount.manager.RefundAccountCommandManager;
import com.ryuqq.setof.application.refundaccount.manager.RefundAccountReadManager;
import com.ryuqq.setof.application.refundaccount.validator.RefundAccountValidator;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccountUpdateData;
import com.ryuqq.setof.domain.refundaccount.exception.AccountVerificationFailedException;
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
@DisplayName("UpdateRefundAccountService 단위 테스트")
class UpdateRefundAccountServiceTest {

    @InjectMocks private UpdateRefundAccountService sut;

    @Mock private RefundAccountReadManager readManager;

    @Mock private RefundAccountCommandFactory factory;

    @Mock private RefundAccountCommandManager commandManager;

    @Mock private RefundAccountValidator validator;

    @Nested
    @DisplayName("execute() - 환불 계좌 수정")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 환불 계좌를 수정하고 각 협력 객체를 순서대로 호출한다")
        void execute_ValidCommand_CallsCollaboratorsInOrder() {
            // given
            UpdateRefundAccountCommand command = RefundAccountCommandFixtures.updateCommand();
            Long userId = command.userId();
            Long refundAccountId = command.refundAccountId();
            RefundAccount refundAccount =
                    RefundAccountDomainFixtures.activeRefundAccount(refundAccountId, userId);
            RefundAccountUpdateData updateData = RefundAccountDomainFixtures.updateData();

            given(readManager.getByUserIdAndId(userId, refundAccountId)).willReturn(refundAccount);
            given(factory.createUpdateData(command)).willReturn(updateData);
            willDoNothing().given(validator).validateAccount(refundAccount);
            given(commandManager.persist(refundAccount)).willReturn(refundAccountId);

            // when
            sut.execute(command);

            // then
            then(readManager).should().getByUserIdAndId(userId, refundAccountId);
            then(factory).should().createUpdateData(command);
            then(validator).should().validateAccount(refundAccount);
            then(commandManager).should().persist(refundAccount);
        }

        @Test
        @DisplayName("존재하지 않는 환불 계좌 수정 시 RefundAccountNotFoundException이 발생한다")
        void execute_NonExistingAccount_ThrowsException() {
            // given
            UpdateRefundAccountCommand command = RefundAccountCommandFixtures.updateCommand();
            Long userId = command.userId();
            Long refundAccountId = command.refundAccountId();

            given(readManager.getByUserIdAndId(userId, refundAccountId))
                    .willThrow(RefundAccountNotFoundException.class);

            // when & then
            assertThatThrownBy(() -> sut.execute(command))
                    .isInstanceOf(RefundAccountNotFoundException.class);

            then(factory).shouldHaveNoInteractions();
            then(validator).shouldHaveNoInteractions();
            then(commandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("계좌 검증 실패 시 AccountVerificationFailedException이 발생하고 persist를 호출하지 않는다")
        void execute_VerificationFails_ThrowsExceptionAndDoesNotPersist() {
            // given
            UpdateRefundAccountCommand command = RefundAccountCommandFixtures.updateCommand();
            Long userId = command.userId();
            Long refundAccountId = command.refundAccountId();
            RefundAccount refundAccount =
                    RefundAccountDomainFixtures.activeRefundAccount(refundAccountId, userId);
            RefundAccountUpdateData updateData = RefundAccountDomainFixtures.updateData();

            given(readManager.getByUserIdAndId(userId, refundAccountId)).willReturn(refundAccount);
            given(factory.createUpdateData(command)).willReturn(updateData);
            willThrow(AccountVerificationFailedException.class)
                    .given(validator)
                    .validateAccount(refundAccount);

            // when & then
            assertThatThrownBy(() -> sut.execute(command))
                    .isInstanceOf(AccountVerificationFailedException.class);

            then(commandManager).shouldHaveNoInteractions();
        }
    }
}
