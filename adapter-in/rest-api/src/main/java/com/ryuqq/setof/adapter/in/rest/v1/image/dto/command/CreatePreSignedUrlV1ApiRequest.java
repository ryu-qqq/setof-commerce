package com.ryuqq.setof.adapter.in.rest.v1.image.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Pre-Signed URL 요청 Request
 *
 * <p>S3 이미지 업로드를 위한 Pre-Signed URL을 요청할 때 사용하는 DTO입니다. 파일명과 이미지 저장 경로를 지정합니다.
 *
 * @param fileName 파일명
 * @param imagePath 이미지 저장 경로
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "Pre-Signed URL 요청 (S3 이미지 업로드용)")
public record CreatePreSignedUrlV1ApiRequest(
        @Schema(
                        description = "파일명",
                        example = "product_image.jpg",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "파일명은 필수입니다.")
                String fileName,
        @Schema(
                        description = "이미지 저장 경로",
                        example = "PRODUCT",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "이미지 저장 경로는 필수입니다.")
        String imagePath) {}
