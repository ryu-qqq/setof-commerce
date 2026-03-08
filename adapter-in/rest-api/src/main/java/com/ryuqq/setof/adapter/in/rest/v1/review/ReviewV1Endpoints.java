package com.ryuqq.setof.adapter.in.rest.v1.review;

/**
 * ReviewV1Endpoints - 리뷰 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>레거시 ReviewController 경로 호환:
 *
 * <ul>
 *   <li>GET /api/v1/reviews
 *   <li>GET /api/v1/reviews/my-page/written
 *   <li>GET /api/v1/reviews/my-page/available
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ReviewV1Endpoints {

    private ReviewV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    /** 리뷰 기본 경로 */
    public static final String REVIEWS = BASE_V1 + "/reviews";

    /** 내가 작성한 리뷰 경로 (GET /api/v1/reviews/my-page/written) */
    public static final String MY_REVIEWS = REVIEWS + "/my-page/written";

    /** 작성 가능한 리뷰 경로 (GET /api/v1/reviews/my-page/available) */
    public static final String AVAILABLE_REVIEWS = REVIEWS + "/my-page/available";
}
