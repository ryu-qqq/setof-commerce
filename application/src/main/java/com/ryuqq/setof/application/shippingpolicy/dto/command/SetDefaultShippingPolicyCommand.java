package com.ryuqq.setof.application.shippingpolicy.dto.command;

/**
 * 기본 배송 정책 설정 Command DTO
 *
 * @param shippingPolicyId 배송 정책 ID
 * @param sellerId 셀러 ID
 * @author development-team
 * @since 1.0.0
 */
public record SetDefaultShippingPolicyCommand(Long shippingPolicyId, Long sellerId) {}
