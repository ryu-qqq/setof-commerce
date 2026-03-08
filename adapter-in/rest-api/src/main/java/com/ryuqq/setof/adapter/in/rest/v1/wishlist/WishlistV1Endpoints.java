package com.ryuqq.setof.adapter.in.rest.v1.wishlist;

/**
 * WishlistV1Endpoints - 찜 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>API-END-003: Path Variable 상수.
 *
 * <p>레거시 UserController 경로 호환:
 *
 * <ul>
 *   <li>GET /api/v1/user/my-favorites
 *   <li>POST /api/v1/user/my-favorite
 *   <li>DELETE /api/v1/user/my-favorite/{productGroupId}
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class WishlistV1Endpoints {

    private WishlistV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    /** 찜 목록 조회 경로 (GET /api/v1/user/my-favorites) */
    public static final String MY_FAVORITES = BASE_V1 + "/user/my-favorites";

    /** 찜 단건 경로 (POST /api/v1/user/my-favorite) */
    public static final String MY_FAVORITE = BASE_V1 + "/user/my-favorite";

    /** 찜 단건 Path Variable 세그먼트 */
    public static final String PRODUCT_GROUP_ID = "/{productGroupId}";

    /** 찜 단건 전체 경로 (DELETE /api/v1/user/my-favorite/{productGroupId}) */
    public static final String MY_FAVORITE_BY_PRODUCT_GROUP_ID = MY_FAVORITE + PRODUCT_GROUP_ID;

    /** productGroupId Path Variable 이름 */
    public static final String PATH_PRODUCT_GROUP_ID = "productGroupId";
}
