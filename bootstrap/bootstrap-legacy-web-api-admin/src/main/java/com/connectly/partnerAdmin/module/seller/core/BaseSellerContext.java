package com.connectly.partnerAdmin.module.seller.core;

import com.connectly.partnerAdmin.auth.enums.ApprovalStatus;
import com.connectly.partnerAdmin.auth.enums.RoleType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseSellerContext implements SellerContext {

    protected Long sellerId;
    protected String email;
    private String passwordHash;
    private RoleType roleType;
    private ApprovalStatus approvalStatus;

    @QueryProjection
    public BaseSellerContext(Long sellerId, String email, String passwordHash, RoleType roleType, ApprovalStatus approvalStatus) {
        this.sellerId = sellerId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roleType = roleType;
        this.approvalStatus = approvalStatus;
    }

    public BaseSellerContext(Long sellerId, String email) {
        this.sellerId = sellerId;
        this.email = email;
    }
}
