package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 가격 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "가격 응답")
public record PriceV1ApiResponse(
        @Schema(description = "정가", example = "50000") Long regularPrice,
        @Schema(description = "현재가", example = "40000") Long currentPrice,
        @Schema(description = "판매가", example = "39000") Long salePrice,
        @Schema(description = "직접 할인율", example = "10") Integer directDiscountRate,
        @Schema(description = "직접 할인가", example = "1000") Long directDiscountPrice,
        @Schema(description = "할인율", example = "22") Integer discountRate) {}
