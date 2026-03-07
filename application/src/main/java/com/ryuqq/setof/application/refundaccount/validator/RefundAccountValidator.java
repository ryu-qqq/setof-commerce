package com.ryuqq.setof.application.refundaccount.validator;

import com.ryuqq.setof.application.refundaccount.manager.AccountVerificationManager;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import org.springframework.stereotype.Component;

/**
 * RefundAccountValidator - 환불 계좌 검증.
 *
 * <p>AccountVerificationManager를 통해 계좌 실명 검증을 수행하고, 도메인 객체의 validateAccountHolder로 결과를 위임합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class RefundAccountValidator {

    private final AccountVerificationManager accountVerificationManager;

    public RefundAccountValidator(AccountVerificationManager accountVerificationManager) {
        this.accountVerificationManager = accountVerificationManager;
    }

    /**
     * 환불 계좌의 실명을 검증합니다.
     *
     * @param refundAccount 검증 대상 환불 계좌
     * @throws com.ryuqq.setof.domain.refundaccount.exception.AccountVerificationFailedException 검증
     *     실패 시
     */
    public void validateAccount(RefundAccount refundAccount) {
        boolean verified =
                accountVerificationManager.verify(
                        refundAccount.bankName(),
                        refundAccount.accountNumber(),
                        refundAccount.accountHolderName());
        refundAccount.validateAccountHolder(verified ? refundAccount.accountHolderName() : null);
    }
}
