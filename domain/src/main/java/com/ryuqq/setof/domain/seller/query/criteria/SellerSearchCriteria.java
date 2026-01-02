package com.ryuqq.setof.domain.seller.query.criteria;

import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.seller.vo.SellerSortBy;
import java.time.Instant;

/**
 * SellerSearchCriteria - 셀러 검색 조건 Domain Criteria
 *
 * <p>셀러 목록 조회 시 사용되는 검색 조건입니다. 오프셋 기반 페이징을 지원합니다.
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
 * @param sellerName 셀러명 검색어 (nullable)
 * @param approvalStatus 승인 상태 (nullable)
 * @param includeDeleted 삭제된 셀러 포함 여부
 * @param startDate 시작 일시 (nullable)
 * @param endDate 종료 일시 (nullable)
 * @param sortBy 정렬 기준 (nullable, 기본값 CREATED_AT)
 * @param sortDirection 정렬 방향 (nullable, 기본값 DESC)
 * @param page 페이지 번호 (0-based)
 * @param pageSize 페이지 크기
 * @author development-team
 * @since 1.0.0
 */
public record SellerSearchCriteria(
        String sellerName,
        String approvalStatus,
        boolean includeDeleted,
        Instant startDate,
        Instant endDate,
        SellerSortBy sortBy,
        SortDirection sortDirection,
        int page,
        int pageSize) {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    /** Compact Constructor - 유효성 검증 및 정규화 */
    public SellerSearchCriteria {
        if (page < 0) {
            page = 0;
        }
        if (pageSize <= 0 || pageSize > MAX_PAGE_SIZE) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        if (sortBy == null) {
            sortBy = SellerSortBy.defaultSortBy();
        }
        if (sortDirection == null) {
            sortDirection = SortDirection.defaultDirection();
        }
    }

    /**
     * Static Factory Method - 기본 검색
     *
     * @param sellerName 셀러명 검색어
     * @param approvalStatus 승인 상태
     * @param page 페이지 번호
     * @param pageSize 페이지 크기
     * @return SellerSearchCriteria 인스턴스
     */
    public static SellerSearchCriteria of(
            String sellerName, String approvalStatus, int page, int pageSize) {
        return new SellerSearchCriteria(
                sellerName,
                approvalStatus,
                false,
                null,
                null,
                SellerSortBy.defaultSortBy(),
                SortDirection.defaultDirection(),
                page,
                pageSize);
    }

    /**
     * Static Factory Method - Admin 조회 (정렬 및 기간 포함)
     *
     * @param sellerName 셀러명 검색어
     * @param approvalStatus 승인 상태
     * @param includeDeleted 삭제된 셀러 포함 여부
     * @param startDate 시작 일시
     * @param endDate 종료 일시
     * @param sortBy 정렬 기준
     * @param sortDirection 정렬 방향
     * @param page 페이지 번호
     * @param pageSize 페이지 크기
     * @return SellerSearchCriteria 인스턴스
     */
    public static SellerSearchCriteria ofAdmin(
            String sellerName,
            String approvalStatus,
            boolean includeDeleted,
            Instant startDate,
            Instant endDate,
            SellerSortBy sortBy,
            SortDirection sortDirection,
            int page,
            int pageSize) {
        return new SellerSearchCriteria(
                sellerName,
                approvalStatus,
                includeDeleted,
                startDate,
                endDate,
                sortBy,
                sortDirection,
                page,
                pageSize);
    }

    /**
     * 셀러명 필터 존재 여부 확인
     *
     * @return 셀러명이 존재하면 true
     */
    public boolean hasSellerName() {
        return sellerName != null && !sellerName.isBlank();
    }

    /**
     * 승인 상태 필터 존재 여부 확인
     *
     * @return 승인 상태가 존재하면 true
     */
    public boolean hasApprovalStatus() {
        return approvalStatus != null && !approvalStatus.isBlank();
    }

    /**
     * 기간 필터 존재 여부 확인
     *
     * @return 시작일과 종료일이 모두 존재하면 true
     */
    public boolean hasDateRange() {
        return startDate != null && endDate != null;
    }

    /**
     * 정렬 기준 반환 (null-safe)
     *
     * @return 정렬 기준 (null이면 CREATED_AT)
     */
    public SellerSortBy effectiveSortBy() {
        return sortBy != null ? sortBy : SellerSortBy.defaultSortBy();
    }

    /**
     * 정렬 방향 반환 (null-safe)
     *
     * @return 정렬 방향 (null이면 DESC)
     */
    public SortDirection effectiveSortDirection() {
        return sortDirection != null ? sortDirection : SortDirection.defaultDirection();
    }

    /**
     * 오프셋 계산
     *
     * @return offset (page * pageSize)
     */
    public long offset() {
        return (long) page * pageSize;
    }

    /**
     * 다음 페이지 존재 판단을 위한 조회 크기 반환
     *
     * <p>hasNext 판단을 위해 pageSize + 1 반환
     *
     * @return pageSize + 1
     */
    public int fetchSize() {
        return pageSize + 1;
    }
}
