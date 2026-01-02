package com.ryuqq.setof.application.claim.dto.command;

/**
 * ApproveClaimCommand - 클레임 승인 Command
 *
 * @param claimId 클레임 ID
 * @param adminId 승인 처리자 ID
 * @author development-team
 * @since 2.0.0
 */
public record ApproveClaimCommand(String claimId, String adminId) {}
