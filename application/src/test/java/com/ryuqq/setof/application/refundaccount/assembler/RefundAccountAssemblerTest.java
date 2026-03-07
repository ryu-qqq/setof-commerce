package com.ryuqq.setof.application.refundaccount.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.refundaccount.RefundAccountDomainFixtures;
import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResult;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("RefundAccountAssembler 단위 테스트")
class RefundAccountAssemblerTest {

    private RefundAccountAssembler sut;

    @BeforeEach
    void setUp() {
        sut = new RefundAccountAssembler();
    }

    @Nested
    @DisplayName("toResult() - 도메인 객체를 Result DTO로 변환")
    class ToResultTest {

        @Test
        @DisplayName("RefundAccount 도메인 객체를 RefundAccountResult로 변환한다")
        void toResult_ValidDomain_ReturnsResult() {
            // given
            Long refundAccountId = 100L;
            Long userId = 1L;
            RefundAccount refundAccount =
                    RefundAccountDomainFixtures.activeRefundAccount(refundAccountId, userId);

            // when
            RefundAccountResult result = sut.toResult(refundAccount);

            // then
            assertThat(result).isNotNull();
            assertThat(result.refundAccountId()).isEqualTo(refundAccountId);
            assertThat(result.bankName()).isEqualTo(refundAccount.bankName());
            assertThat(result.accountNumber()).isEqualTo(refundAccount.accountNumber());
            assertThat(result.accountHolderName()).isEqualTo(refundAccount.accountHolderName());
        }

        @Test
        @DisplayName("은행 정보가 Result에 정확히 매핑된다")
        void toResult_BankInfoMappedCorrectly() {
            // given
            Long refundAccountId = 100L;
            Long userId = 1L;
            String bankName = "국민은행";
            String accountNumber = "123-456-789012";
            String holderName = "김철수";
            RefundAccount refundAccount =
                    RefundAccountDomainFixtures.activeRefundAccount(
                            refundAccountId, userId, bankName, accountNumber, holderName);

            // when
            RefundAccountResult result = sut.toResult(refundAccount);

            // then
            assertThat(result.bankName()).isEqualTo(bankName);
            assertThat(result.accountNumber()).isEqualTo(accountNumber);
            assertThat(result.accountHolderName()).isEqualTo(holderName);
        }

        @Test
        @DisplayName("refundAccountId가 Result에 정확히 매핑된다")
        void toResult_RefundAccountIdMappedCorrectly() {
            // given
            Long refundAccountId = 999L;
            Long userId = 5L;
            RefundAccount refundAccount =
                    RefundAccountDomainFixtures.activeRefundAccount(refundAccountId, userId);

            // when
            RefundAccountResult result = sut.toResult(refundAccount);

            // then
            assertThat(result.refundAccountId()).isEqualTo(refundAccountId);
        }
    }
}
