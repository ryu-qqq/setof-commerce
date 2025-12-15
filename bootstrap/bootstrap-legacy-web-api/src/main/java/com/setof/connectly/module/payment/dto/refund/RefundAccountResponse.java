package com.setof.connectly.module.payment.dto.refund;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.payment.dto.account.AccountResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefundAccountResponse extends AccountResponse {

    private long refundAccountId;
    private String accountHolderName;

    @QueryProjection
    public RefundAccountResponse(
            String bankName, String accountNumber, long refundAccountId, String accountHolderName) {
        super(bankName, accountNumber);
        this.refundAccountId = refundAccountId;
        this.accountHolderName = accountHolderName;
    }
}
