package com.ryuqq.setof.application.discount.dto.query;

import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.DiscountType;

/**
 * 할인 정책 검색 Query DTO
 *
 * @param sellerId 셀러 ID (필수)
 * @param discountGroup 할인 그룹 (nullable)
 * @param discountType 할인 타입 (nullable)
 * @param discountTargetType 적용 대상 타입 (nullable)
 * @param targetId 적용 대상 ID (nullable)
 * @param activeOnly 활성 정책만 조회 (default: true)
 * @param includeDeleted 삭제된 정책 포함 (default: false)
 * @param validOnly 현재 유효한 정책만 조회 (default: true)
 * @author development-team
 * @since 1.0.0
 */
public record DiscountPolicySearchQuery(
        Long sellerId,
        DiscountGroup discountGroup,
        DiscountType discountType,
        DiscountTargetType discountTargetType,
        Long targetId,
        boolean activeOnly,
        boolean includeDeleted,
        boolean validOnly) {

    /**
     * 셀러의 모든 활성 정책 조회용 팩토리 메서드
     *
     * @param sellerId 셀러 ID
     * @return 검색 쿼리
     */
    public static DiscountPolicySearchQuery forActivePolicies(Long sellerId) {
        return new DiscountPolicySearchQuery(sellerId, null, null, null, null, true, false, true);
    }

    /**
     * 셀러의 전체 정책 조회용 팩토리 메서드
     *
     * @param sellerId 셀러 ID
     * @return 검색 쿼리
     */
    public static DiscountPolicySearchQuery forAllPolicies(Long sellerId) {
        return new DiscountPolicySearchQuery(sellerId, null, null, null, null, false, false, false);
    }

    /**
     * 특정 그룹의 정책 조회용 팩토리 메서드
     *
     * @param sellerId 셀러 ID
     * @param discountGroup 할인 그룹
     * @return 검색 쿼리
     */
    public static DiscountPolicySearchQuery forGroup(Long sellerId, DiscountGroup discountGroup) {
        return new DiscountPolicySearchQuery(
                sellerId, discountGroup, null, null, null, true, false, true);
    }
}
