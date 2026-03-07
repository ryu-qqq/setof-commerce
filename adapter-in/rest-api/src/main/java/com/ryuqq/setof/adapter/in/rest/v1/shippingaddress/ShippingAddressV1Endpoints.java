package com.ryuqq.setof.adapter.in.rest.v1.shippingaddress;

/**
 * ShippingAddressV1Endpoints - 배송지 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>API-END-003: Path Variable 상수.
 *
 * <p>레거시 UserController 경로 호환: /api/v1/user/address-book
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ShippingAddressV1Endpoints {

    private ShippingAddressV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    /** 배송지 목록 경로 (GET /api/v1/user/address-book) */
    public static final String ADDRESS_BOOK = BASE_V1 + "/user/address-book";

    /** 배송지 단건 조회 하위 경로 */
    public static final String ADDRESS_BOOK_ID = "/{shippingAddressId}";

    /** 배송지 단건 전체 경로 */
    public static final String ADDRESS_BOOK_BY_ID = ADDRESS_BOOK + ADDRESS_BOOK_ID;

    /** ShippingAddress ID Path Variable 이름 */
    public static final String PATH_SHIPPING_ADDRESS_ID = "shippingAddressId";
}
