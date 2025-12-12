package com.ryuqq.setof.application.shippingpolicy.dto.command;

/**
 * 배송 정책 삭제 Command DTO
 *
 * @param shippingPolicyId 배송 정책 ID
 * @param sellerId 셀러 ID
 * @author development-team
 * @since 1.0.0
 */
public record DeleteShippingPolicyCommand(Long shippingPolicyId, Long sellerId) {}
