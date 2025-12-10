package com.ryuqq.setof.adapter.in.rest.admin.v1.image.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 PreSigned URL Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "PreSigned URL 응답")
public record PreSignedUrlV1ApiResponse(
        @Schema(description = "PreSigned URL",
                example = "https://s3.amazonaws.com/...") String preSignedUrl,
        @Schema(description = "Object Key", example = "product/image.jpg") String objectKey) {
}
