package com.ryuqq.setof.application.refundaccount.service.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.bank.manager.query.BankReadManager;
import com.ryuqq.setof.application.refundaccount.assembler.RefundAccountAssembler;
import com.ryuqq.setof.application.refundaccount.component.RefundAccountValidator;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountByBankNameCommand;
import com.ryuqq.setof.application.refundaccount.dto.command.RegisterRefundAccountCommand;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;
import com.ryuqq.setof.application.refundaccount.factory.command.RefundAccountCommandFactory;
import com.ryuqq.setof.application.refundaccount.manager.command.RefundAccountPersistenceManager;
import com.ryuqq.setof.application.refundaccount.manager.query.RefundAccountReadManager;
import com.ryuqq.setof.domain.bank.BankFixture;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.refundaccount.RefundAccountFixture;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.exception.AccountVerificationFailedException;
import com.ryuqq.setof.domain.refundaccount.exception.RefundAccountAlreadyExistsException;
import com.ryuqq.setof.domain.refundaccount.vo.RefundAccountId;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("RegisterRefundAccountService")
@ExtendWith(MockitoExtension.class)
class RegisterRefundAccountServiceTest {

    @Mock private RefundAccountReadManager refundAccountReadManager;
    @Mock private RefundAccountPersistenceManager refundAccountPersistenceManager;
    @Mock private RefundAccountCommandFactory refundAccountCommandFactory;
    @Mock private BankReadManager bankReadManager;
    @Mock private RefundAccountValidator refundAccountValidator;

    private RefundAccountAssembler refundAccountAssembler;
    private RegisterRefundAccountService registerRefundAccountService;

    @BeforeEach
    void setUp() {
        refundAccountAssembler = new RefundAccountAssembler();
        registerRefundAccountService =
                new RegisterRefundAccountService(
                        refundAccountReadManager,
                        refundAccountPersistenceManager,
                        refundAccountCommandFactory,
                        refundAccountAssembler,
                        bankReadManager,
                        refundAccountValidator);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("환불계좌 등록 성공")
        void shouldRegisterRefundAccount() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;
            Long bankId = RefundAccountFixture.DEFAULT_BANK_ID;
            RegisterRefundAccountCommand command = createCommand(memberId, bankId);
            Bank bank = BankFixture.createWithId(bankId);
            RefundAccount refundAccount = RefundAccountFixture.createWithId(1L);
            RefundAccountId savedId = RefundAccountId.of(1L);

            when(refundAccountReadManager.existsByMemberId(memberId)).thenReturn(false);
            when(bankReadManager.findById(bankId)).thenReturn(bank);
            // RefundAccountValidator.validateAccount()는 void 메서드 - 성공 시 아무것도 안함
            when(refundAccountCommandFactory.createVerified(any())).thenReturn(refundAccount);
            when(refundAccountPersistenceManager.persist(any())).thenReturn(savedId);
            when(refundAccountReadManager.findById(1L)).thenReturn(refundAccount);

            // When
            RefundAccountResponse result = registerRefundAccountService.execute(command);

            // Then
            assertNotNull(result);
            verify(refundAccountPersistenceManager, times(1)).persist(any());
        }

        @Test
        @DisplayName("이미 환불계좌가 존재할 때 예외 발생")
        void shouldThrowExceptionWhenAccountAlreadyExists() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;
            Long bankId = RefundAccountFixture.DEFAULT_BANK_ID;
            RegisterRefundAccountCommand command = createCommand(memberId, bankId);

            when(refundAccountReadManager.existsByMemberId(memberId)).thenReturn(true);

            // When & Then
            assertThrows(
                    RefundAccountAlreadyExistsException.class,
                    () -> registerRefundAccountService.execute(command));
            verify(refundAccountPersistenceManager, never()).persist(any());
        }

        @Test
        @DisplayName("계좌 검증 실패 시 예외 발생")
        void shouldThrowExceptionWhenVerificationFails() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;
            Long bankId = RefundAccountFixture.DEFAULT_BANK_ID;
            RegisterRefundAccountCommand command = createCommand(memberId, bankId);
            Bank bank = BankFixture.createWithId(bankId);

            when(refundAccountReadManager.existsByMemberId(memberId)).thenReturn(false);
            when(bankReadManager.findById(bankId)).thenReturn(bank);
            doThrow(
                            new AccountVerificationFailedException(
                                    bank.getBankCodeValue(), command.accountNumber()))
                    .when(refundAccountValidator)
                    .validateAccount(anyString(), anyString(), anyString());

            // When & Then
            assertThrows(
                    AccountVerificationFailedException.class,
                    () -> registerRefundAccountService.execute(command));
            verify(refundAccountPersistenceManager, never()).persist(any());
        }

        @Test
        @DisplayName("등록된 환불계좌 정보가 응답에 포함")
        void shouldContainCorrectInfoInResponse() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;
            Long bankId = RefundAccountFixture.DEFAULT_BANK_ID;
            RegisterRefundAccountCommand command = createCommand(memberId, bankId);
            Bank bank = BankFixture.createWithId(bankId);
            RefundAccount refundAccount = RefundAccountFixture.createWithId(1L);
            RefundAccountId savedId = RefundAccountId.of(1L);

            when(refundAccountReadManager.existsByMemberId(memberId)).thenReturn(false);
            when(bankReadManager.findById(bankId)).thenReturn(bank);
            // RefundAccountValidator.validateAccount()는 void 메서드 - 성공 시 아무것도 안함
            when(refundAccountCommandFactory.createVerified(any())).thenReturn(refundAccount);
            when(refundAccountPersistenceManager.persist(any())).thenReturn(savedId);
            when(refundAccountReadManager.findById(1L)).thenReturn(refundAccount);

            // When
            RefundAccountResponse result = registerRefundAccountService.execute(command);

            // Then
            assertEquals(refundAccount.getIdValue(), result.id());
            assertEquals(refundAccount.getMaskedAccountNumber(), result.maskedAccountNumber());
        }

        private RegisterRefundAccountCommand createCommand(UUID memberId, Long bankId) {
            return RegisterRefundAccountCommand.of(memberId, bankId, "1234567890123", "홍길동");
        }
    }

    @Nested
    @DisplayName("execute (ByBankName)")
    class ExecuteByBankNameTest {

        @Test
        @DisplayName("은행 이름 기반 환불계좌 등록 성공")
        void shouldRegisterRefundAccountByBankName() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;
            String bankName = "KB국민은행";
            RegisterRefundAccountByBankNameCommand command =
                    createByBankNameCommand(memberId, bankName);
            Bank bank = BankFixture.createWithId(1L);
            RefundAccount refundAccount = RefundAccountFixture.createWithId(1L);
            RefundAccountId savedId = RefundAccountId.of(1L);

            when(refundAccountReadManager.existsByMemberId(memberId)).thenReturn(false);
            when(bankReadManager.findByBankName(bankName)).thenReturn(bank);
            // RefundAccountValidator.validateAccount()는 void 메서드 - 성공 시 아무것도 안함
            when(refundAccountCommandFactory.createVerifiedByBankName(any(), any()))
                    .thenReturn(refundAccount);
            when(refundAccountPersistenceManager.persist(any())).thenReturn(savedId);
            when(refundAccountReadManager.findById(1L)).thenReturn(refundAccount);

            // When
            RefundAccountResponse result = registerRefundAccountService.execute(command);

            // Then
            assertNotNull(result);
            verify(bankReadManager, times(1)).findByBankName(bankName);
            verify(refundAccountPersistenceManager, times(1)).persist(any());
        }

        @Test
        @DisplayName("이미 환불계좌가 존재할 때 예외 발생")
        void shouldThrowExceptionWhenAccountAlreadyExistsByBankName() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;
            RegisterRefundAccountByBankNameCommand command =
                    createByBankNameCommand(memberId, "KB국민은행");

            when(refundAccountReadManager.existsByMemberId(memberId)).thenReturn(true);

            // When & Then
            assertThrows(
                    RefundAccountAlreadyExistsException.class,
                    () -> registerRefundAccountService.execute(command));
            verify(refundAccountPersistenceManager, never()).persist(any());
        }

        @Test
        @DisplayName("계좌 검증 실패 시 예외 발생")
        void shouldThrowExceptionWhenVerificationFailsByBankName() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;
            String bankName = "KB국민은행";
            RegisterRefundAccountByBankNameCommand command =
                    createByBankNameCommand(memberId, bankName);
            Bank bank = BankFixture.createWithId(1L);

            when(refundAccountReadManager.existsByMemberId(memberId)).thenReturn(false);
            when(bankReadManager.findByBankName(bankName)).thenReturn(bank);
            doThrow(
                            new AccountVerificationFailedException(
                                    bank.getBankCodeValue(), command.accountNumber()))
                    .when(refundAccountValidator)
                    .validateAccount(anyString(), anyString(), anyString());

            // When & Then
            assertThrows(
                    AccountVerificationFailedException.class,
                    () -> registerRefundAccountService.execute(command));
            verify(refundAccountPersistenceManager, never()).persist(any());
        }

        @Test
        @DisplayName("등록된 환불계좌 정보가 응답에 포함")
        void shouldContainCorrectInfoInResponseByBankName() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;
            String bankName = "KB국민은행";
            RegisterRefundAccountByBankNameCommand command =
                    createByBankNameCommand(memberId, bankName);
            Bank bank = BankFixture.createWithId(1L);
            RefundAccount refundAccount = RefundAccountFixture.createWithId(1L);
            RefundAccountId savedId = RefundAccountId.of(1L);

            when(refundAccountReadManager.existsByMemberId(memberId)).thenReturn(false);
            when(bankReadManager.findByBankName(bankName)).thenReturn(bank);
            // RefundAccountValidator.validateAccount()는 void 메서드 - 성공 시 아무것도 안함
            when(refundAccountCommandFactory.createVerifiedByBankName(any(), any()))
                    .thenReturn(refundAccount);
            when(refundAccountPersistenceManager.persist(any())).thenReturn(savedId);
            when(refundAccountReadManager.findById(1L)).thenReturn(refundAccount);

            // When
            RefundAccountResponse result = registerRefundAccountService.execute(command);

            // Then
            assertEquals(refundAccount.getIdValue(), result.id());
            assertEquals(refundAccount.getMaskedAccountNumber(), result.maskedAccountNumber());
        }

        private RegisterRefundAccountByBankNameCommand createByBankNameCommand(
                UUID memberId, String bankName) {
            return RegisterRefundAccountByBankNameCommand.of(
                    memberId, bankName, "1234567890123", "홍길동");
        }
    }
}
