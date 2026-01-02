package com.ryuqq.setof.domain.order.query.criteria;

import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.order.vo.OrderSortBy;
import java.time.Instant;
import java.util.List;

/**
 * OrderSearchCriteria - 주문 검색 조건 Domain Criteria
 *
 * <p>주문 목록 조회 시 사용되는 검색 조건입니다. 커서 기반 페이징을 지원합니다.
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
 * @param memberId 회원 ID (nullable)
 * @param sellerId 셀러 ID (nullable, Admin용)
 * @param orderStatuses 주문 상태 목록 (nullable)
 * @param searchKeyword 검색어 (nullable, Admin용)
 * @param startDate 시작 일시 (nullable)
 * @param endDate 종료 일시 (nullable)
 * @param sortBy 정렬 기준 (nullable, 기본값 CREATED_AT)
 * @param sortDirection 정렬 방향 (nullable, 기본값 DESC)
 * @param lastOrderId 마지막 조회 주문 ID - 커서 페이징용 (nullable)
 * @param pageSize 페이지 크기
 * @author development-team
 * @since 1.0.0
 */
public record OrderSearchCriteria(
        String memberId,
        Long sellerId,
        List<String> orderStatuses,
        String searchKeyword,
        Instant startDate,
        Instant endDate,
        OrderSortBy sortBy,
        SortDirection sortDirection,
        String lastOrderId,
        int pageSize) {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    /** Compact Constructor - 유효성 검증 및 정규화 */
    public OrderSearchCriteria {
        if (pageSize <= 0 || pageSize > MAX_PAGE_SIZE) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        if (sortBy == null) {
            sortBy = OrderSortBy.defaultSortBy();
        }
        if (sortDirection == null) {
            sortDirection = SortDirection.defaultDirection();
        }
    }

    /**
     * Static Factory Method - 회원별 조회 (User API)
     *
     * @param memberId 회원 ID
     * @param orderStatuses 주문 상태 목록
     * @param startDate 시작 일시
     * @param endDate 종료 일시
     * @param lastOrderId 마지막 조회 주문 ID
     * @param pageSize 페이지 크기
     * @return OrderSearchCriteria 인스턴스
     */
    public static OrderSearchCriteria ofMember(
            String memberId,
            List<String> orderStatuses,
            Instant startDate,
            Instant endDate,
            String lastOrderId,
            int pageSize) {
        return new OrderSearchCriteria(
                memberId,
                null,
                orderStatuses,
                null,
                startDate,
                endDate,
                OrderSortBy.defaultSortBy(),
                SortDirection.defaultDirection(),
                lastOrderId,
                pageSize);
    }

    /**
     * Static Factory Method - 회원별 조회 (정렬 포함)
     *
     * @param memberId 회원 ID
     * @param orderStatuses 주문 상태 목록
     * @param startDate 시작 일시
     * @param endDate 종료 일시
     * @param sortBy 정렬 기준
     * @param sortDirection 정렬 방향
     * @param lastOrderId 마지막 조회 주문 ID
     * @param pageSize 페이지 크기
     * @return OrderSearchCriteria 인스턴스
     */
    public static OrderSearchCriteria ofMember(
            String memberId,
            List<String> orderStatuses,
            Instant startDate,
            Instant endDate,
            OrderSortBy sortBy,
            SortDirection sortDirection,
            String lastOrderId,
            int pageSize) {
        return new OrderSearchCriteria(
                memberId,
                null,
                orderStatuses,
                null,
                startDate,
                endDate,
                sortBy,
                sortDirection,
                lastOrderId,
                pageSize);
    }

    /**
     * Static Factory Method - Admin 조회
     *
     * @param sellerId 셀러 ID
     * @param orderStatuses 주문 상태 목록
     * @param searchKeyword 검색어
     * @param startDate 시작 일시
     * @param endDate 종료 일시
     * @param sortBy 정렬 기준
     * @param sortDirection 정렬 방향
     * @param lastOrderId 마지막 조회 주문 ID
     * @param pageSize 페이지 크기
     * @return OrderSearchCriteria 인스턴스
     */
    public static OrderSearchCriteria ofAdmin(
            Long sellerId,
            List<String> orderStatuses,
            String searchKeyword,
            Instant startDate,
            Instant endDate,
            OrderSortBy sortBy,
            SortDirection sortDirection,
            String lastOrderId,
            int pageSize) {
        return new OrderSearchCriteria(
                null,
                sellerId,
                orderStatuses,
                searchKeyword,
                startDate,
                endDate,
                sortBy,
                sortDirection,
                lastOrderId,
                pageSize);
    }

    /**
     * 회원 ID 필터 존재 여부 확인
     *
     * @return 회원 ID가 존재하면 true
     */
    public boolean hasMemberId() {
        return memberId != null && !memberId.isBlank();
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
     * 주문 상태 필터 존재 여부 확인
     *
     * @return 주문 상태 목록이 존재하면 true
     */
    public boolean hasOrderStatuses() {
        return orderStatuses != null && !orderStatuses.isEmpty();
    }

    /**
     * 검색어 존재 여부 확인
     *
     * @return 검색어가 존재하면 true
     */
    public boolean hasSearchKeyword() {
        return searchKeyword != null && !searchKeyword.isBlank();
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
     * 커서 존재 여부 확인
     *
     * @return 커서(lastOrderId)가 존재하면 true
     */
    public boolean hasCursor() {
        return lastOrderId != null && !lastOrderId.isBlank();
    }

    /**
     * 정렬 기준 반환 (null-safe)
     *
     * @return 정렬 기준 (null이면 CREATED_AT)
     */
    public OrderSortBy effectiveSortBy() {
        return sortBy != null ? sortBy : OrderSortBy.defaultSortBy();
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
     * Slice 조회를 위한 실제 조회 크기 반환
     *
     * <p>hasNext 판단을 위해 pageSize + 1 반환
     *
     * @return pageSize + 1
     */
    public int fetchSize() {
        return pageSize + 1;
    }
}
