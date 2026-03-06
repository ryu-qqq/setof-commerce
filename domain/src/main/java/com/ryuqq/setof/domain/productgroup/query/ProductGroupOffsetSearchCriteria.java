package com.ryuqq.setof.domain.productgroup.query;

import com.ryuqq.setof.domain.common.vo.DateRange;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupStatus;
import java.util.List;

/**
 * ProductGroup 오프셋 기반 검색 조건.
 *
 * <p>어드민 화면의 오프셋 페이징과 함께 상품그룹 목록을 조회하기 위한 조건을 정의합니다.
 *
 * @param statuses 상품 그룹 상태 필터 (empty이면 전체)
 * @param sellerIds 판매자 ID 목록 필터 (empty이면 전체)
 * @param brandIds 브랜드 ID 목록 필터 (empty이면 전체)
 * @param categoryIds 카테고리 ID 목록 필터 (empty이면 전체)
 * @param productGroupIds 상품 그룹 ID 목록 필터 (empty이면 전체)
 * @param searchField 검색 필드 (null이면 전체 필드 검색)
 * @param searchWord 검색어 (null이면 전체)
 * @param dateRange 날짜 범위 필터 (null이면 제한 없음)
 * @param queryContext 정렬 및 페이징 정보
 */
public record ProductGroupOffsetSearchCriteria(
        List<ProductGroupStatus> statuses,
        List<Long> sellerIds,
        List<Long> brandIds,
        List<Long> categoryIds,
        List<Long> productGroupIds,
        ProductGroupSearchField searchField,
        String searchWord,
        Long lowestPrice,
        Long highestPrice,
        DateRange dateRange,
        QueryContext<ProductGroupSortKey> queryContext) {

    public ProductGroupOffsetSearchCriteria {
        statuses = statuses != null ? List.copyOf(statuses) : List.of();
        sellerIds = sellerIds != null ? List.copyOf(sellerIds) : List.of();
        brandIds = brandIds != null ? List.copyOf(brandIds) : List.of();
        categoryIds = categoryIds != null ? List.copyOf(categoryIds) : List.of();
        productGroupIds = productGroupIds != null ? List.copyOf(productGroupIds) : List.of();
    }

    public static ProductGroupOffsetSearchCriteria of(
            List<ProductGroupStatus> statuses,
            List<Long> sellerIds,
            List<Long> brandIds,
            List<Long> categoryIds,
            List<Long> productGroupIds,
            ProductGroupSearchField searchField,
            String searchWord,
            Long lowestPrice,
            Long highestPrice,
            DateRange dateRange,
            QueryContext<ProductGroupSortKey> queryContext) {
        return new ProductGroupOffsetSearchCriteria(
                statuses,
                sellerIds,
                brandIds,
                categoryIds,
                productGroupIds,
                searchField,
                searchWord,
                lowestPrice,
                highestPrice,
                dateRange,
                queryContext);
    }

    public static ProductGroupOffsetSearchCriteria defaultCriteria() {
        return new ProductGroupOffsetSearchCriteria(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                null,
                null,
                null,
                null,
                null,
                QueryContext.defaultOf(ProductGroupSortKey.defaultKey()));
    }

    /** 상태 필터가 있는지 확인. */
    public boolean hasStatusFilter() {
        return !statuses.isEmpty();
    }

    /** 판매자 필터가 있는지 확인. */
    public boolean hasSellerFilter() {
        return !sellerIds.isEmpty();
    }

    /** 브랜드 필터가 있는지 확인. */
    public boolean hasBrandFilter() {
        return !brandIds.isEmpty();
    }

    /** 카테고리 필터가 있는지 확인. */
    public boolean hasCategoryFilter() {
        return !categoryIds.isEmpty();
    }

    /** 상품 그룹 ID 필터가 있는지 확인. */
    public boolean hasProductGroupIdFilter() {
        return !productGroupIds.isEmpty();
    }

    /** 날짜 범위 필터가 있는지 확인. */
    public boolean hasDateRange() {
        return dateRange != null && !dateRange.isEmpty();
    }

    /** 가격 필터가 있는지 확인. */
    public boolean hasPriceFilter() {
        return lowestPrice != null || highestPrice != null;
    }

    /** 검색 조건이 있는지 확인. */
    public boolean hasSearchCondition() {
        return searchWord != null && !searchWord.isBlank();
    }

    /** 특정 필드 검색인지 확인. */
    public boolean hasSearchField() {
        return searchField != null;
    }

    /** 페이지 크기 반환 (편의 메서드). */
    public int size() {
        return queryContext.size();
    }

    /** 오프셋 반환 (편의 메서드). */
    public long offset() {
        return queryContext.offset();
    }

    /** 현재 페이지 번호 반환 (편의 메서드). */
    public int page() {
        return queryContext.page();
    }
}
