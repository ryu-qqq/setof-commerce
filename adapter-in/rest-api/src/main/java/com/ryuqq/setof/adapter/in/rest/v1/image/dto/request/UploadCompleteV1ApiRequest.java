package com.ryuqq.setof.adapter.in.rest.v1.image.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 업로드 완료 V1 요청.
 *
 * <p>레거시 UploadCompleteRequest와 동일한 필드 구조입니다.
 *
 * @param sessionId FileFlow 세션 ID
 * @param etag S3 업로드 후 반환된 ETag
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "업로드 완료 요청")
public record UploadCompleteV1ApiRequest(
        @Schema(description = "업로드 세션 ID", example = "sess-abc123") @NotBlank String sessionId,
        @Schema(description = "S3 ETag", example = "\"d41d8cd98f00b204e9800998ecf8427e\"") @NotBlank
                String etag) {}
