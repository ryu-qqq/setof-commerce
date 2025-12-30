package com.ryuqq.setof.application.order.dto.query;

import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.order.vo.OrderSortBy;
import java.time.Instant;
import java.util.List;

/**
 * GetAdminOrdersQuery - Admin 주문 목록 조회 Query DTO
 *
 * <p>Admin에서 주문 목록 조회 시 사용되는 검색 조건을 담는 record입니다. memberId 대신 sellerId로 필터링합니다.
 *
 * @param sellerId 셀러 ID (선택)
 * @param orderStatuses 주문 상태 목록 (선택)
 * @param searchKeyword 검색어 (선택)
 * @param startDate 시작 일시 (선택)
 * @param endDate 종료 일시 (선택)
 * @param sortBy 정렬 기준 (선택, 기본값 CREATED_AT)
 * @param sortDirection 정렬 방향 (선택, 기본값 DESC)
 * @param lastOrderId 마지막 조회 주문 ID - 커서 페이징용 (선택)
 * @param pageSize 페이지 크기
 * @author development-team
 * @since 1.0.0
 */
public record GetAdminOrdersQuery(
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

    public GetAdminOrdersQuery {
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
     * Static Factory Method - 기본 조회 (정렬 없음)
     *
     * @param sellerId 셀러 ID
     * @param orderStatuses 주문 상태 목록
     * @param searchKeyword 검색어
     * @param startDate 시작 일시
     * @param endDate 종료 일시
     * @param lastOrderId 마지막 조회 주문 ID
     * @param pageSize 페이지 크기
     * @return GetAdminOrdersQuery 인스턴스
     */
    public static GetAdminOrdersQuery of(
            Long sellerId,
            List<String> orderStatuses,
            String searchKeyword,
            Instant startDate,
            Instant endDate,
            String lastOrderId,
            int pageSize) {
        return new GetAdminOrdersQuery(
                sellerId,
                orderStatuses,
                searchKeyword,
                startDate,
                endDate,
                OrderSortBy.defaultSortBy(),
                SortDirection.defaultDirection(),
                lastOrderId,
                pageSize);
    }

    /**
     * Static Factory Method - 정렬 포함
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
     * @return GetAdminOrdersQuery 인스턴스
     */
    public static GetAdminOrdersQuery of(
            Long sellerId,
            List<String> orderStatuses,
            String searchKeyword,
            Instant startDate,
            Instant endDate,
            OrderSortBy sortBy,
            SortDirection sortDirection,
            String lastOrderId,
            int pageSize) {
        return new GetAdminOrdersQuery(
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
     * 셀러 ID 필터 존재 여부 확인
     *
     * @return 셀러 ID가 존재하면 true
     */
    public boolean hasSellerId() {
        return sellerId != null;
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
     * 커서 존재 여부 확인
     *
     * @return 커서(lastOrderId)가 존재하면 true
     */
    public boolean hasCursor() {
        return lastOrderId != null && !lastOrderId.isBlank();
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
     * 주문 상태 필터 존재 여부 확인
     *
     * @return 주문 상태 목록이 존재하면 true
     */
    public boolean hasStatuses() {
        return orderStatuses != null && !orderStatuses.isEmpty();
    }
}
