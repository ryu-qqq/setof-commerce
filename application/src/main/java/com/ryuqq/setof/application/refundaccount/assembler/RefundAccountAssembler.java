package com.ryuqq.setof.application.refundaccount.assembler;

import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResult;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import org.springframework.stereotype.Component;

/**
 * RefundAccountAssembler - 환불 계좌 도메인 → Result DTO 변환.
 *
 * <p>Persistence 레이어에서 반환된 도메인 객체를 Application 레이어의 Result DTO로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class RefundAccountAssembler {

    public RefundAccountResult toResult(RefundAccount refundAccount) {
        return RefundAccountResult.of(
                refundAccount.idValue(),
                refundAccount.bankName(),
                refundAccount.accountNumber(),
                refundAccount.accountHolderName());
    }
}
