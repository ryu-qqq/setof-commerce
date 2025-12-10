package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 의류 상세 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "의류 상세 응답")
public record ClothesDetailV1ApiResponse(
        @Schema(description = "상품 상태", example = "NEW") String productCondition,
        @Schema(description = "원산지", example = "한국") String origin,
        @Schema(description = "스타일 코드", example = "STYLE001") String styleCode) {
}
