package com.ryuqq.setof.adapter.in.rest.v1.image.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Presigned URL 발급 V1 요청.
 *
 * <p>레거시 PreSignedUrlRequest와 동일한 필드 구조입니다.
 *
 * @param fileName 파일명 (확장자 포함)
 * @param imagePath 이미지 경로 (PRODUCT, DESCRIPTION, QNA 등)
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "Presigned URL 발급 요청")
public record PreSignedUrlV1ApiRequest(
        @Schema(description = "파일명", example = "product-image.jpg") @NotBlank String fileName,
        @Schema(description = "이미지 경로", example = "PRODUCT") @NotNull String imagePath) {}
