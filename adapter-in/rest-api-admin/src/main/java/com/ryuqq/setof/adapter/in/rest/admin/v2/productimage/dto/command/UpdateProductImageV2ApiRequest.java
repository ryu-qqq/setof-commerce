package com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * 상품이미지 수정 요청
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품이미지 수정 요청")
public record UpdateProductImageV2ApiRequest(
        @Schema(description = "이미지 타입 (MAIN, SUB, DETAIL)", example = "MAIN")
                @NotBlank(message = "이미지 타입은 필수입니다")
                String imageType,
        @Schema(description = "CDN URL", example = "https://cdn.example.com/image.jpg")
                String cdnUrl,
        @Schema(description = "표시 순서", example = "1", minimum = "0")
                @Min(value = 0, message = "표시 순서는 0 이상이어야 합니다")
                int displayOrder) {}
