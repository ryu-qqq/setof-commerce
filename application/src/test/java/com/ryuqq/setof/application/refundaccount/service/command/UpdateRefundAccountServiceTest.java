package com.ryuqq.setof.application.refundaccount.service.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.bank.manager.query.BankReadManager;
import com.ryuqq.setof.application.refundaccount.assembler.RefundAccountAssembler;
import com.ryuqq.setof.application.refundaccount.dto.command.UpdateRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;
import com.ryuqq.setof.application.refundaccount.factory.command.RefundAccountCommandFactory;
import com.ryuqq.setof.application.refundaccount.manager.command.RefundAccountPersistenceManager;
import com.ryuqq.setof.application.refundaccount.manager.query.RefundAccountReadManager;
import com.ryuqq.setof.application.refundaccount.port.out.external.AccountVerificationPort;
import com.ryuqq.setof.domain.bank.BankFixture;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.refundaccount.RefundAccountFixture;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.exception.AccountVerificationFailedException;
import com.ryuqq.setof.domain.refundaccount.exception.RefundAccountNotOwnerException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("UpdateRefundAccountService")
@ExtendWith(MockitoExtension.class)
class UpdateRefundAccountServiceTest {

    @Mock private RefundAccountReadManager refundAccountReadManager;
    @Mock private RefundAccountPersistenceManager refundAccountPersistenceManager;
    @Mock private RefundAccountCommandFactory refundAccountCommandFactory;
    @Mock private BankReadManager bankReadManager;
    @Mock private AccountVerificationPort accountVerificationPort;

    private RefundAccountAssembler refundAccountAssembler;
    private UpdateRefundAccountService updateRefundAccountService;

    @BeforeEach
    void setUp() {
        refundAccountAssembler = new RefundAccountAssembler();
        updateRefundAccountService =
                new UpdateRefundAccountService(
                        refundAccountReadManager,
                        refundAccountPersistenceManager,
                        refundAccountCommandFactory,
                        refundAccountAssembler,
                        bankReadManager,
                        accountVerificationPort);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("환불계좌 수정 성공")
        void shouldUpdateRefundAccount() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;
            Long refundAccountId = 1L;
            Long bankId = RefundAccountFixture.DEFAULT_BANK_ID;
            UpdateRefundAccountCommand command = createCommand(memberId, refundAccountId, bankId);
            RefundAccount refundAccount = RefundAccountFixture.createWithId(refundAccountId);
            Bank bank = BankFixture.createWithId(bankId);

            when(refundAccountReadManager.findById(refundAccountId)).thenReturn(refundAccount);
            when(bankReadManager.findById(bankId)).thenReturn(bank);
            when(accountVerificationPort.verifyAccount(anyString(), anyString(), anyString()))
                    .thenReturn(true);

            // When
            RefundAccountResponse result = updateRefundAccountService.execute(command);

            // Then
            assertNotNull(result);
            verify(refundAccountCommandFactory, times(1))
                    .applyUpdateVerified(eq(refundAccount), eq(command));
            verify(refundAccountPersistenceManager, times(1)).persist(refundAccount);
        }

        @Test
        @DisplayName("소유권 검증 실패 시 예외 발생")
        void shouldThrowExceptionWhenOwnershipValidationFails() {
            // Given
            UUID otherMemberId = UUID.randomUUID();
            Long refundAccountId = 1L;
            Long bankId = RefundAccountFixture.DEFAULT_BANK_ID;
            UpdateRefundAccountCommand command = createCommand(otherMemberId, refundAccountId, bankId);
            RefundAccount refundAccount = RefundAccountFixture.createWithId(refundAccountId);

            when(refundAccountReadManager.findById(refundAccountId)).thenReturn(refundAccount);

            // When & Then
            assertThrows(
                    RefundAccountNotOwnerException.class,
                    () -> updateRefundAccountService.execute(command));
            verify(refundAccountPersistenceManager, never()).persist(any());
        }

        @Test
        @DisplayName("계좌 검증 실패 시 예외 발생")
        void shouldThrowExceptionWhenVerificationFails() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;
            Long refundAccountId = 1L;
            Long bankId = RefundAccountFixture.DEFAULT_BANK_ID;
            UpdateRefundAccountCommand command = createCommand(memberId, refundAccountId, bankId);
            RefundAccount refundAccount = RefundAccountFixture.createWithId(refundAccountId);
            Bank bank = BankFixture.createWithId(bankId);

            when(refundAccountReadManager.findById(refundAccountId)).thenReturn(refundAccount);
            when(bankReadManager.findById(bankId)).thenReturn(bank);
            when(accountVerificationPort.verifyAccount(anyString(), anyString(), anyString()))
                    .thenReturn(false);

            // When & Then
            assertThrows(
                    AccountVerificationFailedException.class,
                    () -> updateRefundAccountService.execute(command));
            verify(refundAccountPersistenceManager, never()).persist(any());
        }

        @Test
        @DisplayName("수정된 정보가 응답에 포함")
        void shouldContainUpdatedInfoInResponse() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;
            Long refundAccountId = 1L;
            Long bankId = RefundAccountFixture.DEFAULT_BANK_ID;
            UpdateRefundAccountCommand command = createCommand(memberId, refundAccountId, bankId);
            RefundAccount refundAccount = RefundAccountFixture.createWithId(refundAccountId);
            Bank bank = BankFixture.createWithId(bankId);

            when(refundAccountReadManager.findById(refundAccountId)).thenReturn(refundAccount);
            when(bankReadManager.findById(bankId)).thenReturn(bank);
            when(accountVerificationPort.verifyAccount(anyString(), anyString(), anyString()))
                    .thenReturn(true);

            // When
            RefundAccountResponse result = updateRefundAccountService.execute(command);

            // Then
            assertEquals(refundAccount.getIdValue(), result.id());
            assertEquals(bank.getBankNameValue(), result.bankName());
        }

        private UpdateRefundAccountCommand createCommand(
                UUID memberId, Long refundAccountId, Long bankId) {
            return UpdateRefundAccountCommand.of(
                    memberId, refundAccountId, bankId, "9876543210987", "김철수");
        }
    }
}
