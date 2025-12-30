package com.connectly.partnerAdmin.module.seller.dto;

import com.connectly.partnerAdmin.auth.enums.ApprovalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerResponse {
    private long sellerId;
    private String sellerName;
    private Double commissionRate;
    private ApprovalStatus approvalStatus;
    private String csPhoneNumber;
    private String csEmail;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;

    @QueryProjection
    public SellerResponse(long sellerId, String sellerName, Double commissionRate, ApprovalStatus approvalStatus, String csPhoneNumber, String csEmail, LocalDateTime insertDate) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.commissionRate = commissionRate;
        this.approvalStatus = approvalStatus;
        this.csPhoneNumber = csPhoneNumber;
        this.csEmail = csEmail;
        this.insertDate = insertDate;
    }
}
