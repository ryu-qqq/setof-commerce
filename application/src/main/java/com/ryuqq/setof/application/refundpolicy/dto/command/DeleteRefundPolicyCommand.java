package com.ryuqq.setof.application.refundpolicy.dto.command;

/**
 * 환불 정책 삭제 Command DTO
 *
 * @param refundPolicyId 환불 정책 ID
 * @param sellerId 셀러 ID
 * @author development-team
 * @since 1.0.0
 */
public record DeleteRefundPolicyCommand(Long refundPolicyId, Long sellerId) {}
