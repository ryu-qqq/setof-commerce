package com.ryuqq.setof.adapter.in.rest.v1.faq;

/**
 * FaqV1Endpoints - FAQ V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>레거시 NewsController 경로 호환: /api/v1/faq
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class FaqV1Endpoints {

    private FaqV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    /** FAQ 목록 조회 경로 (GET /api/v1/faq) */
    public static final String FAQS = BASE_V1 + "/faq";
}
