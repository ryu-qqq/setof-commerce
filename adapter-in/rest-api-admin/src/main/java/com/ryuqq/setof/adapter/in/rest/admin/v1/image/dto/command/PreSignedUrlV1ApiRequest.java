package com.ryuqq.setof.adapter.in.rest.admin.v1.image.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 PreSigned URL Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "PreSigned URL 요청")
public record PreSignedUrlV1ApiRequest(
        @Schema(description = "파일명", example = "image.jpg",
                requiredMode = Schema.RequiredMode.REQUIRED) String fileName,
        @Schema(description = "이미지 경로", example = "PRODUCT",
                requiredMode = Schema.RequiredMode.REQUIRED) String imagePath) {
}
