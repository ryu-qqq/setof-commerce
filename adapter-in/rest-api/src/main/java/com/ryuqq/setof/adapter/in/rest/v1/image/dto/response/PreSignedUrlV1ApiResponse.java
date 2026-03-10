package com.ryuqq.setof.adapter.in.rest.v1.image.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Presigned URL 발급 V1 응답.
 *
 * <p>레거시 PreSignedUrlResponse와 동일한 필드 구조입니다.
 *
 * @param sessionId 업로드 세션 ID
 * @param preSignedUrl Presigned URL
 * @param objectKey S3 Object Key
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "Presigned URL 발급 응답")
public record PreSignedUrlV1ApiResponse(
        @Schema(description = "업로드 세션 ID", example = "sess-abc123") String sessionId,
        @Schema(description = "Presigned URL") String preSignedUrl,
        @Schema(description = "S3 Object Key", example = "product/2026-03-09/image.jpg")
                String objectKey) {}
