package com.ryuqq.setof.adapter.in.rest.v1.image;

/**
 * ImageV1Endpoints - 이미지 업로드 V1 Public API 엔드포인트 상수.
 *
 * <p>레거시 ImageController 경로 호환:
 *
 * <ul>
 *   <li>POST /api/v1/image/presigned
 *   <li>POST /api/v1/image/complete
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ImageV1Endpoints {

    private ImageV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static final String BASE_V1 = "/api/v1";

    /** Presigned URL 발급 (POST /api/v1/image/presigned) */
    public static final String IMAGE_PRESIGNED = BASE_V1 + "/image/presigned";

    /** 업로드 완료 (POST /api/v1/image/complete) */
    public static final String IMAGE_COMPLETE = BASE_V1 + "/image/complete";
}
