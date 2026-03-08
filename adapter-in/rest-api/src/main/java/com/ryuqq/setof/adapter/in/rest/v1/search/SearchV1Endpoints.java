package com.ryuqq.setof.adapter.in.rest.v1.search;

/**
 * SearchV1Endpoints - 상품 검색 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>레거시 SearchController 경로 호환: /api/v1/search
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class SearchV1Endpoints {

    private SearchV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    /** 검색 API 경로 (GET /api/v1/search) */
    public static final String SEARCH = BASE_V1 + "/search";
}
