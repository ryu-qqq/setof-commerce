package com.ryuqq.setof.adapter.in.rest.v1.product.dto.response;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 상품 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 응답")
public record ProductV1ApiResponse(@Schema(description = "상품 ID", example = "123") Long productId,
        @Schema(description = "재고 수량", example = "100") Integer stockQuantity,
        @Schema(description = "상품 상태", example = "ON_SALE") String productStatus,
        @Schema(description = "옵션",
                example = "빨강 M") @JsonInclude(JsonInclude.Include.NON_DEFAULT) String option,
        @Schema(description = "옵션 목록") @JsonInclude(JsonInclude.Include.NON_EMPTY) Set<OptionV1ApiResponse> options) {

    @Schema(description = "옵션 응답")
    public record OptionV1ApiResponse(
            @Schema(description = "옵션 그룹 ID", example = "1") Long optionGroupId,
            @Schema(description = "옵션 상세 ID", example = "1") Long optionDetailId,
            @Schema(description = "옵션명", example = "SIZE") String optionName,
            @Schema(description = "옵션 값", example = "M") String optionValue) {
    }
}
