package com.ryuqq.setof.adapter.in.rest.v1.image.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Pre-signed URL Response
 *
 * <p>S3 Pre-signed URL 발급 결과를 반환하는 응답 DTO입니다.
 *
 * @param preSignedUrl Pre-signed URL
 * @param objectKey S3 파일 키
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "Pre-signed URL 응답")
public record CreatePreSignedUrlV1ApiResponse(
        @Schema(description = "Pre-signed URL", example = "https://bucket.s3.amazonaws.com/...")
                String preSignedUrl,
        @Schema(description = "S3 파일 키", example = "images/review/2025/01/uuid.jpg")
                String objectKey) {}
