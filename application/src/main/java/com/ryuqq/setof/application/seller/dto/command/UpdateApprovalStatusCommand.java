package com.ryuqq.setof.application.seller.dto.command;

/**
 * Update Approval Status Command
 *
 * <p>셀러 승인 상태 변경 요청 데이터를 담는 순수한 불변 객체
 *
 * @param sellerId 셀러 ID
 * @param approvalStatus 승인 상태 (PENDING, APPROVED, REJECTED, SUSPENDED)
 * @author development-team
 * @since 1.0.0
 */
public record UpdateApprovalStatusCommand(Long sellerId, String approvalStatus) {}
