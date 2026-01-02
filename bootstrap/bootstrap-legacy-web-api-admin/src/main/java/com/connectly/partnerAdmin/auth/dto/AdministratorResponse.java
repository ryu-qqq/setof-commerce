package com.connectly.partnerAdmin.auth.dto;

import com.connectly.partnerAdmin.auth.enums.ApprovalStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;


@Getter
public class AdministratorResponse {

    private final long id;
    private final String email;
    private final String fullName;
    private final String phoneNumber;
    private final long sellerId;
    private final String sellerName;

    private final ApprovalStatus approvalStatus;

    @QueryProjection
    public AdministratorResponse(long id, String email, String fullName, String phoneNumber, long sellerId, String sellerName, ApprovalStatus approvalStatus) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.approvalStatus = approvalStatus;
    }
}
