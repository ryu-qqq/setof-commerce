package com.ryuqq.setof.application.claim.dto.command;

/**
 * CompleteClaimCommand - 클레임 완료 처리 Command
 *
 * @param claimId 클레임 ID
 * @param adminId 완료 처리자 ID
 * @author development-team
 * @since 2.0.0
 */
public record CompleteClaimCommand(String claimId, String adminId) {}
