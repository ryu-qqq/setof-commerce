package com.ryuqq.setof.application.refundaccount.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.bank.manager.query.BankReadManager;
import com.ryuqq.setof.application.refundaccount.assembler.RefundAccountAssembler;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;
import com.ryuqq.setof.application.refundaccount.manager.query.RefundAccountReadManager;
import com.ryuqq.setof.domain.bank.BankFixture;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.refundaccount.RefundAccountFixture;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("GetRefundAccountService")
@ExtendWith(MockitoExtension.class)
class GetRefundAccountServiceTest {

    @Mock private RefundAccountReadManager refundAccountReadManager;
    @Mock private BankReadManager bankReadManager;

    private RefundAccountAssembler refundAccountAssembler;
    private GetRefundAccountService getRefundAccountService;

    @BeforeEach
    void setUp() {
        refundAccountAssembler = new RefundAccountAssembler();
        getRefundAccountService =
                new GetRefundAccountService(
                        refundAccountReadManager, refundAccountAssembler, bankReadManager);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("환불계좌 조회 성공")
        void shouldReturnRefundAccount() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;
            Long bankId = RefundAccountFixture.DEFAULT_BANK_ID;
            RefundAccount refundAccount = RefundAccountFixture.createWithId(1L);
            Bank bank = BankFixture.createWithId(bankId);

            when(refundAccountReadManager.findByMemberId(memberId))
                    .thenReturn(Optional.of(refundAccount));
            when(bankReadManager.findById(bankId)).thenReturn(bank);

            // When
            Optional<RefundAccountResponse> result = getRefundAccountService.execute(memberId);

            // Then
            assertTrue(result.isPresent());
            assertEquals(refundAccount.getIdValue(), result.get().id());
            verify(refundAccountReadManager, times(1)).findByMemberId(memberId);
            verify(bankReadManager, times(1)).findById(bankId);
        }

        @Test
        @DisplayName("환불계좌가 없을 때 빈 Optional 반환")
        void shouldReturnEmptyWhenNoAccount() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;

            when(refundAccountReadManager.findByMemberId(memberId)).thenReturn(Optional.empty());

            // When
            Optional<RefundAccountResponse> result = getRefundAccountService.execute(memberId);

            // Then
            assertTrue(result.isEmpty());
            verify(bankReadManager, never()).findById(any());
        }

        @Test
        @DisplayName("응답에 올바른 환불계좌 정보 포함")
        void shouldContainCorrectRefundAccountInfo() {
            // Given
            UUID memberId = RefundAccountFixture.DEFAULT_MEMBER_ID;
            Long bankId = RefundAccountFixture.DEFAULT_BANK_ID;
            RefundAccount refundAccount = RefundAccountFixture.createWithId(1L);
            Bank bank = BankFixture.createWithId(bankId);

            when(refundAccountReadManager.findByMemberId(memberId))
                    .thenReturn(Optional.of(refundAccount));
            when(bankReadManager.findById(bankId)).thenReturn(bank);

            // When
            Optional<RefundAccountResponse> result = getRefundAccountService.execute(memberId);

            // Then
            assertTrue(result.isPresent());
            RefundAccountResponse response = result.get();
            assertEquals(refundAccount.getIdValue(), response.id());
            assertEquals(refundAccount.getMaskedAccountNumber(), response.maskedAccountNumber());
            assertEquals(refundAccount.getAccountHolderNameValue(), response.accountHolderName());
            assertEquals(bank.getBankNameValue(), response.bankName());
            assertTrue(response.isVerified());
        }
    }

    private Long any() {
        return org.mockito.ArgumentMatchers.any();
    }
}
