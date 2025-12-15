package com.ryuqq.setof.adapter.in.rest.v2.product.dto.response;

import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

/**
 * 상품그룹 상세 V2 응답
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품그룹 상세 응답")
public record ProductGroupV2ApiResponse(
        @Schema(description = "상품그룹 ID", example = "1") Long productGroupId,
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "카테고리 ID", example = "100") Long categoryId,
        @Schema(description = "브랜드 ID", example = "10") Long brandId,
        @Schema(description = "상품그룹명", example = "기본 반팔 티셔츠") String name,
        @Schema(description = "옵션 타입", example = "SINGLE") String optionType,
        @Schema(description = "정가", example = "29000") BigDecimal regularPrice,
        @Schema(description = "판매가", example = "19900") BigDecimal currentPrice,
        @Schema(description = "상태", example = "ACTIVE") String status,
        @Schema(description = "배송 정책 ID") Long shippingPolicyId,
        @Schema(description = "환불 정책 ID") Long refundPolicyId,
        @Schema(description = "상품(SKU) 목록") List<ProductV2ApiResponse> products) {

    /**
     * Application Response로부터 변환
     *
     * @param response Application 응답
     * @return API 응답
     */
    public static ProductGroupV2ApiResponse from(ProductGroupResponse response) {
        List<ProductV2ApiResponse> products =
                response.products() != null
                        ? response.products().stream().map(ProductV2ApiResponse::from).toList()
                        : List.of();

        return new ProductGroupV2ApiResponse(
                response.productGroupId(),
                response.sellerId(),
                response.categoryId(),
                response.brandId(),
                response.name(),
                response.optionType(),
                response.regularPrice(),
                response.currentPrice(),
                response.status(),
                response.shippingPolicyId(),
                response.refundPolicyId(),
                products);
    }
}
