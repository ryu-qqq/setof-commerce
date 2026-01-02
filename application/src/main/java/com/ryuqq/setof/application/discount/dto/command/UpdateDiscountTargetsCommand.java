package com.ryuqq.setof.application.discount.dto.command;

import java.util.List;

/**
 * 할인 정책 적용 대상 수정 Command DTO
 *
 * @param discountPolicyId 할인 정책 ID
 * @param sellerId 셀러 ID (권한 확인용)
 * @param targetIds 새로운 적용 대상 ID 목록
 * @author development-team
 * @since 1.0.0
 */
public record UpdateDiscountTargetsCommand(
        Long discountPolicyId, Long sellerId, List<Long> targetIds) {}
