package com.ryuqq.setof.adapter.out.persistence.order.condition;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * OrderSearchCondition - Persistence Layer 전용 주문 검색 조건
 *
 * <p>Domain Layer의 OrderSearchCriteria를 Persistence Layer로 변환한 조건 DTO
 *
 * <p>Repository에서 Domain VO에 직접 의존하지 않도록 분리
 *
 * <p><strong>지원 조회 유형:</strong>
 *
 * <ul>
 *   <li>회원별 조회 (User API) - memberId 기준
 *   <li>셀러별 조회 (Admin API) - sellerId 기준
 * </ul>
 *
 * @param memberId 회원 ID (nullable, User API용)
 * @param sellerId 셀러 ID (nullable, Admin API용)
 * @param statuses 주문 상태 목록 (nullable)
 * @param searchKeyword 검색어 (nullable, Admin용)
 * @param startDate 시작 일시 (nullable)
 * @param endDate 종료 일시 (nullable)
 * @param sortField 정렬 필드
 * @param sortAscending 오름차순 정렬 여부
 * @param lastOrderId 마지막 주문 ID - 커서 기반 페이징 (nullable)
 * @param limit 조회 개수
 * @author development-team
 * @since 1.0.0
 */
public record OrderSearchCondition(
        String memberId,
        Long sellerId,
        List<String> statuses,
        String searchKeyword,
        Instant startDate,
        Instant endDate,
        SortField sortField,
        boolean sortAscending,
        UUID lastOrderId,
        int limit) {

    /** 정렬 필드 enum */
    public enum SortField {
        ID,
        ORDER_DATE,
        CREATED_AT,
        UPDATED_AT,
        TOTAL_AMOUNT
    }

    /**
     * Static Factory Method - 전체 조건
     *
     * @param memberId 회원 ID
     * @param sellerId 셀러 ID
     * @param statuses 주문 상태 목록
     * @param searchKeyword 검색어
     * @param startDate 시작 일시
     * @param endDate 종료 일시
     * @param sortField 정렬 필드
     * @param sortAscending 오름차순 정렬 여부
     * @param lastOrderId 마지막 주문 ID
     * @param limit 조회 개수
     * @return OrderSearchCondition 인스턴스
     */
    public static OrderSearchCondition of(
            String memberId,
            Long sellerId,
            List<String> statuses,
            String searchKeyword,
            Instant startDate,
            Instant endDate,
            SortField sortField,
            boolean sortAscending,
            UUID lastOrderId,
            int limit) {
        return new OrderSearchCondition(
                memberId,
                sellerId,
                statuses,
                searchKeyword,
                startDate,
                endDate,
                sortField != null ? sortField : SortField.CREATED_AT,
                sortAscending,
                lastOrderId,
                limit);
    }

    /**
     * 회원 ID 필터 존재 여부
     *
     * @return 회원 ID가 존재하면 true
     */
    public boolean hasMemberId() {
        return memberId != null && !memberId.isBlank();
    }

    /**
     * 셀러 ID 필터 존재 여부
     *
     * @return 셀러 ID가 존재하면 true
     */
    public boolean hasSellerId() {
        return sellerId != null;
    }

    /**
     * 상태 필터 존재 여부
     *
     * @return 상태 필터가 존재하면 true
     */
    public boolean hasStatuses() {
        return statuses != null && !statuses.isEmpty();
    }

    /**
     * 검색어 존재 여부
     *
     * @return 검색어가 존재하면 true
     */
    public boolean hasSearchKeyword() {
        return searchKeyword != null && !searchKeyword.isBlank();
    }

    /**
     * 시작 일시 필터 존재 여부
     *
     * @return 시작 일시 필터가 존재하면 true
     */
    public boolean hasStartDate() {
        return startDate != null;
    }

    /**
     * 종료 일시 필터 존재 여부
     *
     * @return 종료 일시 필터가 존재하면 true
     */
    public boolean hasEndDate() {
        return endDate != null;
    }

    /**
     * 기간 필터 존재 여부
     *
     * @return 시작일과 종료일이 모두 존재하면 true
     */
    public boolean hasDateRange() {
        return startDate != null && endDate != null;
    }

    /**
     * 커서 존재 여부 (페이징용)
     *
     * @return 마지막 주문 ID가 존재하면 true
     */
    public boolean hasCursor() {
        return lastOrderId != null;
    }
}
