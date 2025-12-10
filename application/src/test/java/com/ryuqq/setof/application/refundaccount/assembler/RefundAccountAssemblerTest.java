package com.ryuqq.setof.application.refundaccount.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;
import com.ryuqq.setof.domain.bank.BankFixture;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.refundaccount.RefundAccountFixture;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RefundAccountAssembler")
class RefundAccountAssemblerTest {

    private RefundAccountAssembler assembler;

    @BeforeEach
    void setUp() {
        assembler = new RefundAccountAssembler();
    }

    @Nested
    @DisplayName("toResponse")
    class ToResponseTest {

        @Test
        @DisplayName("RefundAccount와 Bank를 RefundAccountResponse로 변환 성공")
        void shouldConvertRefundAccountToResponse() {
            // Given
            RefundAccount refundAccount = RefundAccountFixture.createWithId(1L);
            Bank bank = BankFixture.create();

            // When
            RefundAccountResponse result = assembler.toResponse(refundAccount, bank);

            // Then
            assertNotNull(result);
            assertEquals(refundAccount.getIdValue(), result.id());
            assertEquals(refundAccount.getBankId(), result.bankId());
            assertEquals(bank.getBankNameValue(), result.bankName());
            assertEquals(bank.getBankCodeValue(), result.bankCode());
            assertEquals(refundAccount.getMaskedAccountNumber(), result.maskedAccountNumber());
            assertEquals(refundAccount.getAccountHolderNameValue(), result.accountHolderName());
            assertTrue(result.isVerified());
            assertNotNull(result.verifiedAt());
        }

        @Test
        @DisplayName("미검증 상태 RefundAccount 변환 성공")
        void shouldConvertUnverifiedRefundAccountToResponse() {
            // Given
            RefundAccount refundAccount = RefundAccountFixture.createUnverifiedWithId(2L);
            Bank bank = BankFixture.create();

            // When
            RefundAccountResponse result = assembler.toResponse(refundAccount, bank);

            // Then
            assertNotNull(result);
            assertEquals(refundAccount.getIdValue(), result.id());
            assertFalse(result.isVerified());
            assertNull(result.verifiedAt());
        }

        @Test
        @DisplayName("커스텀 환불계좌와 은행 정보 변환 성공")
        void shouldConvertCustomRefundAccountAndBankToResponse() {
            // Given
            RefundAccount refundAccount =
                    RefundAccountFixture.createCustom(
                            5L,
                            RefundAccountFixture.DEFAULT_MEMBER_ID,
                            2L,
                            "9876543210987",
                            "김철수",
                            true);
            Bank bank = BankFixture.createCustom(2L, "088", "신한은행", 2, true);

            // When
            RefundAccountResponse result = assembler.toResponse(refundAccount, bank);

            // Then
            assertNotNull(result);
            assertEquals(5L, result.id());
            assertEquals(2L, result.bankId());
            assertEquals("신한은행", result.bankName());
            assertEquals("088", result.bankCode());
            assertEquals("9876****0987", result.maskedAccountNumber());
            assertEquals("김철수", result.accountHolderName());
        }
    }
}
