package com.ryuqq.setof.application.refundaccount.assembler;

import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import org.springframework.stereotype.Component;

/**
 * RefundAccount Assembler
 *
 * <p>RefundAccount 도메인 객체를 Response DTO로 변환하는 Assembler
 *
 * <p>역할:
 * <ul>
 *   <li>Domain → Response DTO 변환
 *   <li>Law of Demeter Helper 메서드 활용
 *   <li>Bank 정보 조합
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefundAccountAssembler {

    /**
     * RefundAccount 도메인과 Bank 정보를 RefundAccountResponse로 변환
     *
     * @param refundAccount RefundAccount 도메인 객체
     * @param bank Bank 도메인 객체
     * @return RefundAccountResponse
     */
    public RefundAccountResponse toResponse(RefundAccount refundAccount, Bank bank) {
        return RefundAccountResponse.of(
                refundAccount.getIdValue(),
                refundAccount.getBankId(),
                bank.getBankNameValue(),
                bank.getBankCodeValue(),
                refundAccount.getMaskedAccountNumber(),
                refundAccount.getAccountHolderNameValue(),
                refundAccount.isVerified(),
                refundAccount.getVerifiedAt(),
                refundAccount.getCreatedAt(),
                refundAccount.getUpdatedAt());
    }
}
