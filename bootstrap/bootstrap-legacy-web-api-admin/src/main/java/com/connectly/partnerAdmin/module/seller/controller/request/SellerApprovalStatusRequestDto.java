package com.connectly.partnerAdmin.module.seller.controller.request;

import com.connectly.partnerAdmin.auth.enums.ApprovalStatus;

import java.util.List;

public record SellerApprovalStatusRequestDto(
        List<Long> sellerIds,
        ApprovalStatus approvalStatus
) {}
