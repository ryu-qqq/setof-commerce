package com.ryuqq.setof.domain.product.query.criteria;

import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.product.vo.ProductSearchPeriod;
import com.ryuqq.setof.domain.product.vo.ProductSortType;

/**
 * ProductGroupSearchCriteria - 상품그룹 검색 조건 Domain Criteria
 *
 * <p>상품그룹 목록 조회 시 사용되는 검색 조건입니다.
 *
 * <p><strong>사용 위치:</strong>
 *
 * <ul>
 *   <li>Domain Layer에서 정의 (query.criteria 패키지)
 *   <li>Persistence Layer Repository에서 사용
 *   <li>Application Layer에서 생성 (Factory/Service)
 * </ul>
 *
 * <p><strong>의존성 방향:</strong>
 *
 * <pre>{@code
 * Application Layer → Domain Layer ← Persistence Layer
 *                      (Criteria)
 * }</pre>
 *
 * @param sellerId 셀러 ID (nullable)
 * @param categoryId 카테고리 ID (nullable)
 * @param brandId 브랜드 ID (nullable)
 * @param name 상품그룹명 검색어 (nullable, like 검색)
 * @param status 상품 상태 (ACTIVE, INACTIVE, null=전체)
 * @param searchPeriod 검색 기간 (nullable)
 * @param sortType 정렬 기준 (nullable, 기본값 LATEST)
 * @param pageRequest 페이징 정보
 * @author development-team
 * @since 1.0.0
 */
public record ProductGroupSearchCriteria(
        Long sellerId,
        Long categoryId,
        Long brandId,
        String name,
        String status,
        ProductSearchPeriod searchPeriod,
        ProductSortType sortType,
        PageRequest pageRequest) {

    /**
     * Static Factory Method - 전체 조건
     *
     * @param sellerId 셀러 ID
     * @param categoryId 카테고리 ID
     * @param brandId 브랜드 ID
     * @param name 상품그룹명
     * @param status 상품 상태
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ProductGroupSearchCriteria 인스턴스
     */
    public static ProductGroupSearchCriteria of(
            Long sellerId,
            Long categoryId,
            Long brandId,
            String name,
            String status,
            int page,
            int size) {
        return new ProductGroupSearchCriteria(
                sellerId,
                categoryId,
                brandId,
                name,
                status,
                null,
                ProductSortType.defaultSort(),
                PageRequest.of(page, size));
    }

    /**
     * Static Factory Method - 전체 조건 (검색 기간, 정렬 포함)
     *
     * @param sellerId 셀러 ID
     * @param categoryId 카테고리 ID
     * @param brandId 브랜드 ID
     * @param name 상품그룹명
     * @param status 상품 상태
     * @param searchPeriod 검색 기간
     * @param sortType 정렬 기준
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ProductGroupSearchCriteria 인스턴스
     */
    public static ProductGroupSearchCriteria of(
            Long sellerId,
            Long categoryId,
            Long brandId,
            String name,
            String status,
            ProductSearchPeriod searchPeriod,
            ProductSortType sortType,
            int page,
            int size) {
        return new ProductGroupSearchCriteria(
                sellerId,
                categoryId,
                brandId,
                name,
                status,
                searchPeriod,
                sortType != null ? sortType : ProductSortType.defaultSort(),
                PageRequest.of(page, size));
    }

    /**
     * Static Factory Method - 셀러별 조회
     *
     * @param sellerId 셀러 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ProductGroupSearchCriteria 인스턴스
     */
    public static ProductGroupSearchCriteria ofSeller(Long sellerId, int page, int size) {
        return new ProductGroupSearchCriteria(
                sellerId,
                null,
                null,
                null,
                null,
                null,
                ProductSortType.defaultSort(),
                PageRequest.of(page, size));
    }

    /**
     * Static Factory Method - 카테고리별 조회
     *
     * @param categoryId 카테고리 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ProductGroupSearchCriteria 인스턴스
     */
    public static ProductGroupSearchCriteria ofCategory(Long categoryId, int page, int size) {
        return new ProductGroupSearchCriteria(
                null,
                categoryId,
                null,
                null,
                null,
                null,
                ProductSortType.defaultSort(),
                PageRequest.of(page, size));
    }

    /**
     * Static Factory Method - 브랜드별 조회
     *
     * @param brandId 브랜드 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ProductGroupSearchCriteria 인스턴스
     */
    public static ProductGroupSearchCriteria ofBrand(Long brandId, int page, int size) {
        return new ProductGroupSearchCriteria(
                null,
                null,
                brandId,
                null,
                null,
                null,
                ProductSortType.defaultSort(),
                PageRequest.of(page, size));
    }

    /**
     * Static Factory Method - 이름 검색
     *
     * @param name 상품그룹명
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ProductGroupSearchCriteria 인스턴스
     */
    public static ProductGroupSearchCriteria ofName(String name, int page, int size) {
        return new ProductGroupSearchCriteria(
                null,
                null,
                null,
                name,
                null,
                null,
                ProductSortType.defaultSort(),
                PageRequest.of(page, size));
    }

    /**
     * Static Factory Method - 기간 검색
     *
     * @param searchPeriod 검색 기간
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ProductGroupSearchCriteria 인스턴스
     */
    public static ProductGroupSearchCriteria ofPeriod(
            ProductSearchPeriod searchPeriod, int page, int size) {
        return new ProductGroupSearchCriteria(
                null,
                null,
                null,
                null,
                null,
                searchPeriod,
                ProductSortType.defaultSort(),
                PageRequest.of(page, size));
    }

    /**
     * 셀러 ID 필터 존재 여부 확인
     *
     * @return 셀러 ID가 존재하면 true
     */
    public boolean hasSellerId() {
        return sellerId != null;
    }

    /**
     * 카테고리 ID 필터 존재 여부 확인
     *
     * @return 카테고리 ID가 존재하면 true
     */
    public boolean hasCategoryId() {
        return categoryId != null;
    }

    /**
     * 브랜드 ID 필터 존재 여부 확인
     *
     * @return 브랜드 ID가 존재하면 true
     */
    public boolean hasBrandId() {
        return brandId != null;
    }

    /**
     * 이름 필터 존재 여부 확인
     *
     * @return 이름 필터가 존재하면 true
     */
    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    /**
     * 상태 필터 존재 여부 확인
     *
     * @return 상태 필터가 존재하면 true
     */
    public boolean hasStatus() {
        return status != null && !status.isBlank();
    }

    /**
     * 검색 기간 존재 여부 확인
     *
     * @return 검색 기간이 존재하고 날짜 범위가 있으면 true
     */
    public boolean hasSearchPeriod() {
        return searchPeriod != null && searchPeriod.hasDateRange();
    }

    /**
     * 정렬 기준 반환 (null-safe)
     *
     * @return 정렬 기준 (null이면 LATEST)
     */
    public ProductSortType effectiveSortType() {
        return sortType != null ? sortType : ProductSortType.defaultSort();
    }

    /**
     * offset 계산 (PageRequest 위임)
     *
     * @return offset 값
     */
    public long offset() {
        return pageRequest.offset();
    }

    /**
     * 페이지 크기 반환 (PageRequest 위임)
     *
     * @return 페이지 크기
     */
    public int size() {
        return pageRequest.size();
    }

    /**
     * 페이지 번호 반환 (PageRequest 위임)
     *
     * @return 페이지 번호
     */
    public int page() {
        return pageRequest.page();
    }
}
