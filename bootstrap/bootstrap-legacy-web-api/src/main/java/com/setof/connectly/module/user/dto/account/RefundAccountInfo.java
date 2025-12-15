package com.setof.connectly.module.user.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefundAccountInfo {

    @JsonIgnore private Boolean isAuthenticated;
    private Long refundAccountId;
    private String bankName;
    private String accountNumber;
    private String accountHolderName;

    @Builder
    @QueryProjection
    public RefundAccountInfo(
            Long refundAccountId, String bankName, String accountNumber, String accountHolderName) {
        this.refundAccountId = refundAccountId;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
    }

    @QueryProjection
    public RefundAccountInfo(String bankName, String accountNumber, String accountHolderName) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
    }

    public RefundAccountInfo(Boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
        this.refundAccountId = 0L;
        this.bankName = "";
        this.accountNumber = "";
        this.accountHolderName = "";
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }
}
