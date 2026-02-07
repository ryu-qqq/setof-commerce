package com.ryuqq.setof.domain.legacy.product.dto.query;

import java.util.List;

/**
 * LegacyProductGroupSearchCondition - 레거시 상품그룹 검색 조건 DTO.
 *
 * <p>상품그룹 목록 조회 시 사용되는 검색 조건입니다.
 *
 * @param lastDomainId 커서 페이징용 마지막 ID
 * @param cursorValue 커서 값 (정렬 기준값)
 * @param lowestPrice 최저가 필터
 * @param highestPrice 최고가 필터
 * @param categoryId 카테고리 ID 필터
 * @param brandId 브랜드 ID 필터
 * @param sellerId 셀러 ID 필터
 * @param categoryIds 다중 카테고리 ID 필터
 * @param brandIds 다중 브랜드 ID 필터
 * @param orderType 정렬 타입
 * @param pageSize 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyProductGroupSearchCondition(
        Long lastDomainId,
        String cursorValue,
        Long lowestPrice,
        Long highestPrice,
        Long categoryId,
        Long brandId,
        Long sellerId,
        List<Long> categoryIds,
        List<Long> brandIds,
        String orderType,
        int pageSize) {

    /**
     * 브랜드별 상품 조회 조건.
     *
     * @param brandId 브랜드 ID
     * @param pageSize 페이지 크기
     * @return LegacyProductGroupSearchCondition
     */
    public static LegacyProductGroupSearchCondition ofBrand(Long brandId, int pageSize) {
        return new LegacyProductGroupSearchCondition(
                null, null, null, null, null, brandId, null, null, null, "RECOMMEND", pageSize);
    }

    /**
     * 셀러별 상품 조회 조건.
     *
     * @param sellerId 셀러 ID
     * @param pageSize 페이지 크기
     * @return LegacyProductGroupSearchCondition
     */
    public static LegacyProductGroupSearchCondition ofSeller(Long sellerId, int pageSize) {
        return new LegacyProductGroupSearchCondition(
                null, null, null, null, null, null, sellerId, null, null, "RECOMMEND", pageSize);
    }

    /**
     * ID 목록으로 조회 조건.
     *
     * @return LegacyProductGroupSearchCondition
     */
    public static LegacyProductGroupSearchCondition empty() {
        return new LegacyProductGroupSearchCondition(
                null, null, null, null, null, null, null, null, null, null, 0);
    }

    /**
     * 필터 조건이 있는지 확인.
     *
     * @return 필터 조건이 있으면 true
     */
    public boolean hasFilter() {
        return categoryId != null
                || brandId != null
                || sellerId != null
                || (categoryIds != null && !categoryIds.isEmpty())
                || (brandIds != null && !brandIds.isEmpty())
                || lowestPrice != null
                || highestPrice != null;
    }

    /**
     * 커서 페이징 조건이 있는지 확인.
     *
     * @return 커서 조건이 있으면 true
     */
    public boolean hasCursor() {
        return lastDomainId != null && cursorValue != null;
    }
}
