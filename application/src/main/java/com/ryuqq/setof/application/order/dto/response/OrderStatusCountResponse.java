package com.ryuqq.setof.application.order.dto.response;

import java.util.Map;

/**
 * 주문 상태별 개수 응답 DTO
 *
 * @param counts 상태별 개수 Map (status → count)
 * @param total 전체 주문 개수
 * @author development-team
 * @since 1.0.0
 */
public record OrderStatusCountResponse(Map<String, Long> counts, long total) {

    public static OrderStatusCountResponse of(Map<String, Long> counts) {
        long total = counts.values().stream().mapToLong(Long::longValue).sum();
        return new OrderStatusCountResponse(counts, total);
    }
}
