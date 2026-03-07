package com.ryuqq.setof.application.refundaccount.validator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.refundaccount.RefundAccountDomainFixtures;
import com.ryuqq.setof.application.refundaccount.manager.AccountVerificationManager;
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
@DisplayName("RefundAccountValidator 단위 테스트")
class RefundAccountValidatorTest {

    @InjectMocks private RefundAccountValidator sut;

    @Mock private AccountVerificationManager accountVerificationManager;

    @Nested
    @DisplayName("validateAccount() - 환불 계좌 실명 검증")
    class ValidateAccountTest {

        @Test
        @DisplayName("계좌 실명 검증 성공 시 예외 없이 완료된다")
        void validateAccount_VerificationSuccess_NoException() {
            // given
            Long refundAccountId = 100L;
            Long userId = 1L;
            RefundAccount refundAccount =
                    RefundAccountDomainFixtures.activeRefundAccount(refundAccountId, userId);

            given(
                            accountVerificationManager.verify(
                                    refundAccount.bankName(),
                                    refundAccount.accountNumber(),
                                    refundAccount.accountHolderName()))
                    .willReturn(true);

            // when & then
            assertThatCode(() -> sut.validateAccount(refundAccount)).doesNotThrowAnyException();
            then(accountVerificationManager)
                    .should()
                    .verify(
                            refundAccount.bankName(),
                            refundAccount.accountNumber(),
                            refundAccount.accountHolderName());
        }

        @Test
        @DisplayName("계좌 실명 검증 실패 시 AccountVerificationFailedException이 발생한다")
        void validateAccount_VerificationFails_ThrowsException() {
            // given
            Long refundAccountId = 100L;
            Long userId = 1L;
            RefundAccount refundAccount =
                    RefundAccountDomainFixtures.activeRefundAccount(refundAccountId, userId);

            given(
                            accountVerificationManager.verify(
                                    refundAccount.bankName(),
                                    refundAccount.accountNumber(),
                                    refundAccount.accountHolderName()))
                    .willReturn(false);

            // when & then
            assertThatThrownBy(() -> sut.validateAccount(refundAccount))
                    .isInstanceOf(AccountVerificationFailedException.class);
        }

        @Test
        @DisplayName("AccountVerificationManager의 verify 메서드에 계좌 정보를 정확히 전달한다")
        void validateAccount_PassesCorrectBankInfo_ToManager() {
            // given
            String bankName = "국민은행";
            String accountNumber = "123-456-789012";
            String holderName = "김철수";
            RefundAccount refundAccount =
                    RefundAccountDomainFixtures.activeRefundAccount(
                            100L, 1L, bankName, accountNumber, holderName);

            given(accountVerificationManager.verify(bankName, accountNumber, holderName))
                    .willReturn(true);

            // when
            sut.validateAccount(refundAccount);

            // then
            then(accountVerificationManager).should().verify(bankName, accountNumber, holderName);
        }
    }
}
