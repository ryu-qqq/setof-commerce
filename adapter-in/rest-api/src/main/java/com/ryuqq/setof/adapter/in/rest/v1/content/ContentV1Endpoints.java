package com.ryuqq.setof.adapter.in.rest.v1.content;

/**
 * ContentV1Endpoints - 콘텐츠 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>레거시 ContentController 경로 호환: /api/v1/content
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ContentV1Endpoints {

    private ContentV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    /** 콘텐츠 기본 경로 */
    public static final String CONTENT = BASE_V1 + "/content";
}
