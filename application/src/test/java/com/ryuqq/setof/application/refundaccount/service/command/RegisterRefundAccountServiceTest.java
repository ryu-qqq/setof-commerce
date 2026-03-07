package com.ryuqq.setof.application.refundaccount.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import com.ryuqq.setof.application.refundaccount.RefundAccountCommandFixtures;
import com.ryuqq.setof.application.refundaccount.RefundAccountDomainFixtures;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.factory.RefundAccountCommandFactory;
import com.ryuqq.setof.application.refundaccount.manager.RefundAccountCommandManager;
import com.ryuqq.setof.application.refundaccount.validator.RefundAccountValidator;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.exception.AccountVerificationFailedException;
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
@DisplayName("RegisterRefundAccountService 단위 테스트")
class RegisterRefundAccountServiceTest {

    @InjectMocks private RegisterRefundAccountService sut;

    @Mock private RefundAccountCommandFactory factory;

    @Mock private RefundAccountCommandManager commandManager;

    @Mock private RefundAccountValidator validator;

    @Nested
    @DisplayName("execute() - 환불 계좌 등록")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 환불 계좌를 등록하고 ID를 반환한다")
        void execute_ValidCommand_ReturnsRefundAccountId() {
            // given
            RegisterRefundAccountCommand command = RefundAccountCommandFixtures.registerCommand();
            Long userId = command.userId();
            RefundAccount newRefundAccount = RefundAccountDomainFixtures.newRefundAccount(userId);
            Long expectedId = 100L;

            given(factory.createNewRefundAccount(command)).willReturn(newRefundAccount);
            willDoNothing().given(validator).validateAccount(newRefundAccount);
            given(commandManager.persist(newRefundAccount)).willReturn(expectedId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(factory).should().createNewRefundAccount(command);
            then(validator).should().validateAccount(newRefundAccount);
            then(commandManager).should().persist(newRefundAccount);
        }

        @Test
        @DisplayName("계좌 검증 실패 시 AccountVerificationFailedException이 발생하고 persist를 호출하지 않는다")
        void execute_VerificationFails_ThrowsExceptionAndDoesNotPersist() {
            // given
            RegisterRefundAccountCommand command = RefundAccountCommandFixtures.registerCommand();
            Long userId = command.userId();
            RefundAccount newRefundAccount = RefundAccountDomainFixtures.newRefundAccount(userId);

            given(factory.createNewRefundAccount(command)).willReturn(newRefundAccount);
            willThrow(AccountVerificationFailedException.class)
                    .given(validator)
                    .validateAccount(newRefundAccount);

            // when & then
            assertThatThrownBy(() -> sut.execute(command))
                    .isInstanceOf(AccountVerificationFailedException.class);

            then(commandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("Factory → Validator → CommandManager 순서로 호출된다")
        void execute_CallsInCorrectOrder() {
            // given
            RegisterRefundAccountCommand command = RefundAccountCommandFixtures.registerCommand();
            Long userId = command.userId();
            RefundAccount newRefundAccount = RefundAccountDomainFixtures.newRefundAccount(userId);
            Long expectedId = 100L;

            given(factory.createNewRefundAccount(command)).willReturn(newRefundAccount);
            willDoNothing().given(validator).validateAccount(newRefundAccount);
            given(commandManager.persist(newRefundAccount)).willReturn(expectedId);

            // when
            sut.execute(command);

            // then
            org.mockito.InOrder inOrder =
                    org.mockito.Mockito.inOrder(factory, validator, commandManager);
            inOrder.verify(factory).createNewRefundAccount(command);
            inOrder.verify(validator).validateAccount(newRefundAccount);
            inOrder.verify(commandManager).persist(newRefundAccount);
        }
    }
}
