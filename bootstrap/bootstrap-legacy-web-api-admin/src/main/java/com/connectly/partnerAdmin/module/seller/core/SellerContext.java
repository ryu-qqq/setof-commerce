package com.connectly.partnerAdmin.module.seller.core;

import com.connectly.partnerAdmin.auth.enums.ApprovalStatus;
import com.connectly.partnerAdmin.auth.enums.RoleType;

public interface SellerContext {

    Long getSellerId();
    String getEmail();
    String getPasswordHash();
    RoleType getRoleType();
    ApprovalStatus getApprovalStatus();
}
