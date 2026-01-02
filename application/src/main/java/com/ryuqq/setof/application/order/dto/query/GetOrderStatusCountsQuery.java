package com.ryuqq.setof.application.order.dto.query;

import java.util.List;

/**
 * GetOrderStatusCountsQuery - 주문 상태별 개수 조회 Query DTO
 *
 * <p>주문 상태별 개수 조회 시 사용되는 검색 조건을 담는 record입니다.
 *
 * @param memberId 회원 ID (필수)
 * @param statuses 조회할 상태 목록 (선택, null이면 전체)
 * @author development-team
 * @since 1.0.0
 */
public record GetOrderStatusCountsQuery(String memberId, List<String> statuses) {

    public GetOrderStatusCountsQuery {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("memberId must not be null or blank");
        }
    }

    public static GetOrderStatusCountsQuery of(String memberId, List<String> statuses) {
        return new GetOrderStatusCountsQuery(memberId, statuses);
    }

    public static GetOrderStatusCountsQuery ofAll(String memberId) {
        return new GetOrderStatusCountsQuery(memberId, null);
    }
}
