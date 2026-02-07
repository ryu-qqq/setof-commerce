package com.ryuqq.setof.application.legacy.order.dto.response;

/**
 * LegacyOrderCountResult - 레거시 주문 상태별 건수 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param orderStatus 주문 상태
 * @param count 해당 상태의 건수
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyOrderCountResult(String orderStatus, long count) {

    /**
     * 정적 팩토리 메서드.
     *
     * @param orderStatus 주문 상태
     * @param count 건수
     * @return LegacyOrderCountResult
     */
    public static LegacyOrderCountResult of(String orderStatus, long count) {
        return new LegacyOrderCountResult(orderStatus, count);
    }
}
