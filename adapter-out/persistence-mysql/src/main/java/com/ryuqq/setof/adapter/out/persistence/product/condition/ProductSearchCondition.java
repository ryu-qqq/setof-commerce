package com.ryuqq.setof.adapter.out.persistence.product.condition;

import java.time.Instant;

/**
 * ProductSearchCondition - Persistence Layer 전용 검색 조건
 *
 * <p>Domain Layer의 ProductGroupSearchCriteria를 Persistence Layer로 변환한 조건 DTO
 *
 * <p>Repository에서 Domain VO에 직접 의존하지 않도록 분리
 *
 * @param sellerId 셀러 ID (nullable)
 * @param categoryId 카테고리 ID (nullable)
 * @param brandId 브랜드 ID (nullable)
 * @param name 상품그룹명 검색어 (nullable)
 * @param status 상품 상태 (nullable)
 * @param startInstant 검색 시작 시간 (nullable)
 * @param endInstant 검색 종료 시간 (nullable)
 * @param isCreatedAtSearch 생성일 기준 검색 여부 (true: createdAt, false: updatedAt)
 * @param sortField 정렬 필드 (CREATED_AT, PRICE)
 * @param sortAscending 오름차순 정렬 여부
 * @param offset 페이징 offset
 * @param limit 페이징 limit
 * @author development-team
 * @since 1.0.0
 */
public record ProductSearchCondition(
        Long sellerId,
        Long categoryId,
        Long brandId,
        String name,
        String status,
        Instant startInstant,
        Instant endInstant,
        boolean isCreatedAtSearch,
        SortField sortField,
        boolean sortAscending,
        long offset,
        int limit) {

    /** 정렬 필드 enum (Persistence Layer 전용) */
    public enum SortField {
        CREATED_AT,
        PRICE
    }

    /** 셀러 ID 필터 존재 여부 */
    public boolean hasSellerId() {
        return sellerId != null;
    }

    /** 카테고리 ID 필터 존재 여부 */
    public boolean hasCategoryId() {
        return categoryId != null;
    }

    /** 브랜드 ID 필터 존재 여부 */
    public boolean hasBrandId() {
        return brandId != null;
    }

    /** 이름 필터 존재 여부 */
    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    /** 상태 필터 존재 여부 */
    public boolean hasStatus() {
        return status != null && !status.isBlank();
    }

    /** 날짜 범위 필터 존재 여부 */
    public boolean hasDateRange() {
        return startInstant != null || endInstant != null;
    }
}
