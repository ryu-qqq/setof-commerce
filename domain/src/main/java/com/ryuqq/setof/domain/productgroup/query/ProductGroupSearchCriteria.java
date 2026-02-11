package com.ryuqq.setof.domain.productgroup.query;

import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupStatus;
import com.ryuqq.setof.domain.seller.id.SellerId;

/**
 * 상품그룹 검색 조건.
 *
 * <p>커서 기반 페이징과 함께 상품그룹 목록을 조회하기 위한 조건을 정의합니다.
 *
 * @param sellerId 셀러 ID (nullable)
 * @param brandId 브랜드 ID (nullable)
 * @param categoryId 카테고리 ID (nullable)
 * @param status 상품그룹 상태 (nullable)
 * @param keyword 검색 키워드 (nullable, 상품그룹명 검색)
 * @param queryContext 정렬 + 커서 페이징 컨텍스트 (필수)
 */
public record ProductGroupSearchCriteria(
        SellerId sellerId,
        BrandId brandId,
        CategoryId categoryId,
        ProductGroupStatus status,
        String keyword,
        CursorQueryContext<ProductGroupSortKey, Long> queryContext) {

    public ProductGroupSearchCriteria {
        if (queryContext == null) {
            throw new IllegalArgumentException("queryContext는 필수입니다");
        }
    }

    /**
     * 기본 검색 조건 생성 (필터 없음, 기본 정렬).
     *
     * @param queryContext 정렬 + 커서 페이징 컨텍스트
     * @return 기본 ProductGroupSearchCriteria
     */
    public static ProductGroupSearchCriteria of(
            CursorQueryContext<ProductGroupSortKey, Long> queryContext) {
        return new ProductGroupSearchCriteria(null, null, null, null, null, queryContext);
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

    public boolean hasStatus() {
        return status != null;
    }

    public boolean hasKeyword() {
        return keyword != null && !keyword.isBlank();
    }
}
