package com.ryuqq.setof.application.discount.dto.command;

/**
 * 할인 정책 삭제 Command DTO
 *
 * @param discountPolicyId 할인 정책 ID
 * @param sellerId 셀러 ID (권한 확인용)
 * @author development-team
 * @since 1.0.0
 */
public record DeleteDiscountPolicyCommand(Long discountPolicyId, Long sellerId) {}
