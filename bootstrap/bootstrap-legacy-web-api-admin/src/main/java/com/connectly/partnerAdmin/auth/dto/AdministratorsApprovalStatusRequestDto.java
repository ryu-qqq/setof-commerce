package com.connectly.partnerAdmin.auth.dto;

import com.connectly.partnerAdmin.auth.enums.ApprovalStatus;

import java.util.List;

public record AdministratorsApprovalStatusRequestDto(
        List<Long> adminIds,
        ApprovalStatus approvalStatus
) {
}
