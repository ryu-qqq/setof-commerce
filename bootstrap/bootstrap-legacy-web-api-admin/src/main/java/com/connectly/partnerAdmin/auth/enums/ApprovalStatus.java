package com.connectly.partnerAdmin.auth.enums;

public enum ApprovalStatus {
    PENDING,
    APPROVED,
    REJECTED
;
    public boolean isApproved(){
        return this.equals(APPROVED);
    }
}
