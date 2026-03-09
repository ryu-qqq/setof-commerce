package com.ryuqq.setof.application.order.dto.query;

import java.time.LocalDate;
import java.util.List;

/**
 * 주문 목록 조회 파라미터 DTO.
 *
 * <p>커서 기반 페이징 + 날짜 범위 + 상태 필터를 포함합니다.
 *
 * @param userId 사용자 ID (필수)
 * @param startDate 검색 시작일
 * @param endDate 검색 종료일
 * @param orderStatuses 주문 상태 필터 목록
 * @param lastOrderId 커서 페이징용 마지막 주문 ID (null이면 첫 페이지)
 * @param size 페이지 크기 (기본: 20)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record OrderSearchParams(
        long userId,
        LocalDate startDate,
        LocalDate endDate,
        List<String> orderStatuses,
        Long lastOrderId,
        Integer size) {

    private static final int DEFAULT_SIZE = 20;

    public OrderSearchParams {
        if (size == null || size <= 0) {
            size = DEFAULT_SIZE;
        }
    }

    public static OrderSearchParams of(
            long userId,
            LocalDate startDate,
            LocalDate endDate,
            List<String> orderStatuses,
            Long lastOrderId,
            Integer size) {
        return new OrderSearchParams(userId, startDate, endDate, orderStatuses, lastOrderId, size);
    }

    public int pageSize() {
        return size != null ? size : DEFAULT_SIZE;
    }
}
