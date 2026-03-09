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

    /** 리뷰 단건 경로 (POST /api/v1/review) */
    public static final String REVIEW = BASE_V1 + "/review";

    /** reviewId Path Variable 세그먼트 */
    public static final String REVIEW_ID = "/{reviewId}";

    /** 리뷰 단건 전체 경로 (DELETE /api/v1/review/{reviewId}) */
    public static final String REVIEW_BY_ID = REVIEW + REVIEW_ID;

    /** reviewId Path Variable 이름 */
    public static final String PATH_REVIEW_ID = "reviewId";
}
