package com.ryuqq.setof.adapter.in.rest.v1.order;

/**
 * OrderV1Endpoints - 주문 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class OrderV1Endpoints {

    private OrderV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    /** 주문 상태별 건수 조회 (GET /api/v1/order/status-counts) */
    public static final String ORDER_STATUS_COUNTS = BASE_V1 + "/order/status-counts";

    /** 주문 이력 조회 (GET /api/v1/order/history/{orderId}) */
    public static final String ORDER_HISTORY = BASE_V1 + "/order/history/{orderId}";

    /** 주문 목록 조회 (GET /api/v1/orders) */
    public static final String ORDERS = BASE_V1 + "/orders";

    /** 주문 상태 변경 (PUT /api/v1/order) */
    public static final String ORDER = BASE_V1 + "/order";
}
