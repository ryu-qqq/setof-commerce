package com.connectly.partnerAdmin.module.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefundAccountInfo {

    private long refundAccountId;
    private String bankName;
    private String accountNumber;
    private String accountHolderName;

    @Builder
    @QueryProjection
    public RefundAccountInfo(long refundAccountId, String bankName, String accountNumber, String accountHolderName) {
        this.refundAccountId = refundAccountId;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
    }
}
