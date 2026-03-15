package com.ryuqq.setof.domain.order.vo;

import java.util.List;

/**
 * 마이페이지 주문 상태별 건수 도메인 VO.
 *
 * <p>마이페이지에서 표시할 주문 상태 목록({@link #STATUSES})과 각 상태별 건수를 캡슐화합니다.
 *
 * @param counts 주문 상태별 건수 목록
 * @author ryu-qqq
 * @since 1.2.0
 */
public record MyPageOrderCounts(List<OrderStatusCount> counts) {

    /** 마이페이지에서 조회 대상인 주문 상태 목록. */
    public static final List<String> STATUSES =
            List.of(
                    "ORDER_PROCESSING",
                    "ORDER_COMPLETED",
                    "DELIVERY_PENDING",
                    "DELIVERY_PROCESSING",
                    "DELIVERY_COMPLETED");

    public static MyPageOrderCounts of(List<OrderStatusCount> counts) {
        return new MyPageOrderCounts(counts);
    }

    /**
     * 특정 상태의 건수를 반환합니다.
     *
     * @param status 주문 상태
     * @return 해당 상태의 건수 (없으면 0)
     */
    public long countOf(String status) {
        return counts.stream()
                .filter(c -> c.orderStatus().equals(status))
                .findFirst()
                .map(OrderStatusCount::count)
                .orElse(0L);
    }

    /**
     * 전체 건수 합계를 반환합니다.
     *
     * @return 전체 건수 합계
     */
    public long totalCount() {
        return counts.stream().mapToLong(OrderStatusCount::count).sum();
    }
}
