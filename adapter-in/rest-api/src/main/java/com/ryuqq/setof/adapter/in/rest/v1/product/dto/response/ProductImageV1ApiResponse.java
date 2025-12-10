package com.ryuqq.setof.adapter.in.rest.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 상품 이미지 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 이미지 응답")
public record ProductImageV1ApiResponse(
        @Schema(description = "상품 그룹 이미지 타입", example = "MAIN") String productGroupImageType,
        @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
                String imageUrl) {}
