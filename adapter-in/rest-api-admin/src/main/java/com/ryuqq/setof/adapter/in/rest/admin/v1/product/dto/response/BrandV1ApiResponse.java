package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 브랜드 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "브랜드 응답")
public record BrandV1ApiResponse(@Schema(description = "브랜드 ID", example = "1") Long brandId,
        @Schema(description = "브랜드명", example = "Nike") String brandName) {
}
