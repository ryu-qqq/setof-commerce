package com.ryuqq.setof.adapter.out.persistence.order.condition;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * AdminOrderSearchCondition - Admin 주문 검색 조건
 *
 * <p>Admin에서 주문 목록 조회 시 사용되는 Persistence Layer 전용 검색 조건 DTO
 *
 * <p>memberId 대신 sellerId로 필터링하며, 검색어 필터를 추가로 지원합니다.
 *
 * @param sellerId 셀러 ID (nullable)
 * @param statuses 주문 상태 목록 (nullable)
 * @param searchKeyword 검색어 (nullable) - 주문번호, 구매자명 등
 * @param startDate 시작 일시 (nullable)
 * @param endDate 종료 일시 (nullable)
 * @param lastOrderId 마지막 주문 ID - 커서 기반 페이징 (nullable)
 * @param limit 조회 개수
 * @author development-team
 * @since 1.0.0
 */
public record AdminOrderSearchCondition(
        Long sellerId,
        List<String> statuses,
        String searchKeyword,
        Instant startDate,
        Instant endDate,
        UUID lastOrderId,
        int limit) {

    public static AdminOrderSearchCondition of(
            Long sellerId,
            List<String> statuses,
            String searchKeyword,
            Instant startDate,
            Instant endDate,
            UUID lastOrderId,
            int limit) {
        return new AdminOrderSearchCondition(
                sellerId, statuses, searchKeyword, startDate, endDate, lastOrderId, limit);
    }

    public boolean hasSellerId() {
        return sellerId != null;
    }

    public boolean hasStatuses() {
        return statuses != null && !statuses.isEmpty();
    }

    public boolean hasSearchKeyword() {
        return searchKeyword != null && !searchKeyword.isBlank();
    }

    public boolean hasStartDate() {
        return startDate != null;
    }

    public boolean hasEndDate() {
        return endDate != null;
    }

    public boolean hasCursor() {
        return lastOrderId != null;
    }
}
