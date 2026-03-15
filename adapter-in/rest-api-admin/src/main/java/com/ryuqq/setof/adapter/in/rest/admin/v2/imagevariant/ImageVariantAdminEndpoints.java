package com.ryuqq.setof.adapter.in.rest.admin.v2.imagevariant;

/**
 * ImageVariant Admin API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class
 *
 * <p>API-END-002: static final 상수
 *
 * <p>API-END-003: Path Variable 상수
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ImageVariantAdminEndpoints {

    private ImageVariantAdminEndpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** 이미지 Variant 기본 경로 */
    public static final String BASE = "/api/v2/admin/image-variants";

    /** 동기화 경로 */
    public static final String SYNC = "/sync";
}
