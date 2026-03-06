package com.ryuqq.setof.domain.productgroup.query;

import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupStatus;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.util.List;

/**
 * 상품그룹 검색 조건.
 *
 * <p>커서 기반 페이징과 함께 상품그룹 목록을 조회하기 위한 조건을 정의합니다.
 *
 * @param sellerId 셀러 ID (nullable)
 * @param brandId 브랜드 ID (nullable)
 * @param categoryId 카테고리 ID (nullable)
 * @param categoryIds 카테고리 ID 목록 필터 (empty이면 전체)
 * @param brandIds 브랜드 ID 목록 필터 (empty이면 전체)
 * @param status 상품그룹 상태 (nullable)
 * @param searchField 검색 필드 (nullable, null이면 전체 필드 검색)
 * @param searchWord 검색어 (nullable)
 * @param lowestPrice 최저가 필터 (nullable)
 * @param highestPrice 최고가 필터 (nullable)
 * @param cursorValue 커서 정렬 키 값 (nullable, 비-ID 정렬 시 사용)
 * @param queryContext 정렬 + 커서 페이징 컨텍스트 (필수)
 */
public record ProductGroupSearchCriteria(
        SellerId sellerId,
        BrandId brandId,
        CategoryId categoryId,
        List<Long> categoryIds,
        List<Long> brandIds,
        ProductGroupStatus status,
        ProductGroupSearchField searchField,
        String searchWord,
        Long lowestPrice,
        Long highestPrice,
        String cursorValue,
        CursorQueryContext<ProductGroupSortKey, Long> queryContext) {

    public ProductGroupSearchCriteria {
        if (queryContext == null) {
            throw new IllegalArgumentException("queryContext는 필수입니다");
        }
        categoryIds = categoryIds != null ? List.copyOf(categoryIds) : List.of();
        brandIds = brandIds != null ? List.copyOf(brandIds) : List.of();
    }

    /**
     * 기본 검색 조건 생성 (필터 없음, 기본 정렬).
     *
     * @param queryContext 정렬 + 커서 페이징 컨텍스트
     * @return 기본 ProductGroupSearchCriteria
     */
    public static ProductGroupSearchCriteria of(
            CursorQueryContext<ProductGroupSortKey, Long> queryContext) {
        return new ProductGroupSearchCriteria(
                null,
                null,
                null,
                List.of(),
                List.of(),
                null,
                null,
                null,
                null,
                null,
                null,
                queryContext);
    }

    public boolean hasSellerId() {
        return sellerId != null;
    }

    public boolean hasBrandId() {
        return brandId != null;
    }

    public boolean hasCategoryId() {
        return categoryId != null;
    }

    public boolean hasCategoryIds() {
        return !categoryIds.isEmpty();
    }

    public boolean hasBrandIds() {
        return !brandIds.isEmpty();
    }

    public boolean hasStatus() {
        return status != null;
    }

    public boolean hasSearchField() {
        return searchField != null;
    }

    public boolean hasSearchCondition() {
        return searchWord != null && !searchWord.isBlank();
    }

    public boolean hasPriceFilter() {
        return lowestPrice != null || highestPrice != null;
    }

    public boolean hasCursorValue() {
        return cursorValue != null && !cursorValue.isBlank();
    }
}
