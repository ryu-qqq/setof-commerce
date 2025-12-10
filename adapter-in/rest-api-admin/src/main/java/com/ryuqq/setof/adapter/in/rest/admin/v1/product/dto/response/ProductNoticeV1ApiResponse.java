package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 상품 고지 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 고지 응답")
public record ProductNoticeV1ApiResponse(
        @Schema(description = "소재", example = "면 100%") String material,
        @Schema(description = "색상", example = "블랙") String color,
        @Schema(description = "사이즈", example = "M") String size,
        @Schema(description = "제조사", example = "제조사명") String maker,
        @Schema(description = "원산지", example = "한국") String origin,
        @Schema(description = "세탁 방법", example = "손세탁") String washingMethod,
        @Schema(description = "제조년월", example = "2024-01") String yearMonth,
        @Schema(description = "품질보증기준", example = "소비자분쟁해결기준") String assuranceStandard,
        @Schema(description = "A/S 전화번호", example = "1588-0000") String asPhone) {
}
