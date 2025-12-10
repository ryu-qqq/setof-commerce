package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

/**
 * V1 상품 그룹 생성 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 그룹 생성 응답")
public record CreateProductGroupV1ApiResponse(
        @Schema(description = "상품 그룹 ID", example = "12345") Long productGroupId,
        @Schema(description = "셀러 ID", example = "100") Long sellerId,
        @Schema(description = "상품 목록") Set<ProductFetchV1ApiResponse> products) {
}
