package com.ryuqq.setof.adapter.in.rest.v1.cart;

/**
 * CartV1Endpoints - 장바구니 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>API-END-003: Path Variable 상수.
 *
 * <p>레거시 CartController 경로 호환:
 *
 * <ul>
 *   <li>GET /api/v1/cart-count
 *   <li>GET /api/v1/carts
 *   <li>POST /api/v1/cart
 *   <li>PUT /api/v1/cart/{cartId}
 *   <li>DELETE /api/v1/carts
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class CartV1Endpoints {

    private CartV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    /** 장바구니 개수 조회 경로 (GET /api/v1/cart-count) */
    public static final String CART_COUNT = BASE_V1 + "/cart-count";

    /** 장바구니 목록 조회 / 삭제 경로 (GET|DELETE /api/v1/carts) */
    public static final String CARTS = BASE_V1 + "/carts";

    /** 장바구니 단건 경로 (POST /api/v1/cart) */
    public static final String CART = BASE_V1 + "/cart";

    /** 장바구니 단건 Path Variable 세그먼트 */
    public static final String CART_ID = "/{cartId}";

    /** 장바구니 단건 전체 경로 (PUT /api/v1/cart/{cartId}) */
    public static final String CART_BY_ID = CART + CART_ID;

    /** cartId Path Variable 이름 */
    public static final String PATH_CART_ID = "cartId";
}
