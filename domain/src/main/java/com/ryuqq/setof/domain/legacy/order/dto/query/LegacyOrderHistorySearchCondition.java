package com.ryuqq.setof.domain.legacy.order.dto.query;

/**
 * LegacyOrderHistorySearchCondition - 레거시 주문 이력 검색 조건 DTO.
 *
 * <p>주문 상태 변경 이력 조회 시 사용됩니다.
 *
 * @param orderId 주문 ID
 * @param orderStatus 특정 주문 상태 (선택)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyOrderHistorySearchCondition(Long orderId, String orderStatus) {

    /**
     * 주문 ID로 전체 이력 조회.
     *
     * @param orderId 주문 ID
     * @return LegacyOrderHistorySearchCondition
     */
    public static LegacyOrderHistorySearchCondition ofOrderId(Long orderId) {
        return new LegacyOrderHistorySearchCondition(orderId, null);
    }

    /**
     * 주문 ID와 상태로 특정 이력 조회.
     *
     * @param orderId 주문 ID
     * @param orderStatus 주문 상태
     * @return LegacyOrderHistorySearchCondition
     */
    public static LegacyOrderHistorySearchCondition of(Long orderId, String orderStatus) {
        return new LegacyOrderHistorySearchCondition(orderId, orderStatus);
    }

    /**
     * 주문 상태 필터 존재 여부.
     *
     * @return 주문 상태가 있으면 true
     */
    public boolean hasOrderStatus() {
        return orderStatus != null && !orderStatus.isBlank();
    }
}
