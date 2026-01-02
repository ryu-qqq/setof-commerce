package com.ryuqq.setof.application.claim.dto.command;

/**
 * RejectClaimCommand - 클레임 반려 Command
 *
 * @param claimId 클레임 ID
 * @param adminId 반려 처리자 ID
 * @param rejectReason 반려 사유
 * @author development-team
 * @since 2.0.0
 */
public record RejectClaimCommand(String claimId, String adminId, String rejectReason) {}
