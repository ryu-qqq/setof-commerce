package com.ryuqq.setof.adapter.in.rest.admin.v1.productimage.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * V1 상품 이미지 생성 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 이미지 생성 요청")
public record CreateProductImageV1ApiRequest(
        @Schema(
                        description = "이미지 타입",
                        example = "MAIN",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "이미지 타입은 필수입니다.")
                String productImageType,
        @Schema(
                        description = "이미지 URL",
                        example = "https://example.com/image.jpg",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "이미지 URL은 필수입니다.")
                @Length(max = 255, message = "이미지 URL은 255자를 넘을 수 없습니다.")
                String imageUrl,
        @Schema(description = "원본 URL", example = "https://example.com/origin.jpg")
                @Length(max = 255, message = "원본 URL은 255자를 넘을 수 없습니다.")
                String originUrl) {}
