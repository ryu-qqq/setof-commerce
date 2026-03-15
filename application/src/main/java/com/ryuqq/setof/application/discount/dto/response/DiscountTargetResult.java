package com.ryuqq.setof.application.discount.dto.response;

/**
 * 할인 적용 대상 결과 DTO.
 *
 * <p>{@code DiscountPolicyResult} 내부에 포함되는 개별 할인 대상 정보입니다.
 *
 * @param id 할인 대상 엔티티 ID
 * @param targetType 대상 유형 (예: PRODUCT_GROUP, CATEGORY)
 * @param targetId 대상 엔티티 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DiscountTargetResult(Long id, String targetType, long targetId) {}
