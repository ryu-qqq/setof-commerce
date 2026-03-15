package com.ryuqq.setof.application.discount.dto.command;

import java.util.List;

/**
 * 할인 적용 대상 수정 Command DTO.
 *
 * <p>특정 할인 정책에 대한 적용 대상 목록을 교체(replace)합니다. 기존 대상은 비활성화되고 새 대상이 등록됩니다.
 *
 * @param discountPolicyId 대상을 수정할 할인 정책 ID
 * @param targetType 대상 유형 (예: PRODUCT_GROUP, CATEGORY)
 * @param targetIds 새로 설정할 대상 엔티티 ID 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ModifyDiscountTargetsCommand(
        long discountPolicyId, String targetType, List<Long> targetIds) {}
