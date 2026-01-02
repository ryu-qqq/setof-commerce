package com.ryuqq.setof.application.order.dto.query;

import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.order.vo.OrderSortBy;
import java.time.Instant;
import java.util.List;

/**
 * GetOrdersQuery - 주문 목록 조회 Query DTO
 *
 * <p>주문 목록 조회 시 사용되는 검색 조건을 담는 record입니다.
 *
 * @param memberId 회원 ID (필수)
 * @param orderStatuses 주문 상태 목록 (선택)
 * @param startDate 시작 일시 (선택)
 * @param endDate 종료 일시 (선택)
 * @param sortBy 정렬 기준 (선택, 기본값 CREATED_AT)
 * @param sortDirection 정렬 방향 (선택, 기본값 DESC)
 * @param lastOrderId 마지막 조회 주문 ID - 커서 페이징용 (선택)
 * @param pageSize 페이지 크기
 * @author development-team
 * @since 1.0.0
 */
public record GetOrdersQuery(
        String memberId,
        List<String> orderStatuses,
        Instant startDate,
        Instant endDate,
        OrderSortBy sortBy,
        SortDirection sortDirection,
        String lastOrderId,
        int pageSize) {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    public GetOrdersQuery {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("memberId must not be null or blank");
        }
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
     * @param memberId 회원 ID
     * @param orderStatuses 주문 상태 목록
     * @param startDate 시작 일시
     * @param endDate 종료 일시
     * @param lastOrderId 마지막 조회 주문 ID
     * @param pageSize 페이지 크기
     * @return GetOrdersQuery 인스턴스
     */
    public static GetOrdersQuery of(
            String memberId,
            List<String> orderStatuses,
            Instant startDate,
            Instant endDate,
            String lastOrderId,
            int pageSize) {
        return new GetOrdersQuery(
                memberId,
                orderStatuses,
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
     * @param memberId 회원 ID
     * @param orderStatuses 주문 상태 목록
     * @param startDate 시작 일시
     * @param endDate 종료 일시
     * @param sortBy 정렬 기준
     * @param sortDirection 정렬 방향
     * @param lastOrderId 마지막 조회 주문 ID
     * @param pageSize 페이지 크기
     * @return GetOrdersQuery 인스턴스
     */
    public static GetOrdersQuery of(
            String memberId,
            List<String> orderStatuses,
            Instant startDate,
            Instant endDate,
            OrderSortBy sortBy,
            SortDirection sortDirection,
            String lastOrderId,
            int pageSize) {
        return new GetOrdersQuery(
                memberId,
                orderStatuses,
                startDate,
                endDate,
                sortBy,
                sortDirection,
                lastOrderId,
                pageSize);
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
}
