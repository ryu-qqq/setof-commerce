package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response;

import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;
import com.ryuqq.setof.application.product.dto.response.ProductResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

/**
 * 상품그룹 상세 응답 DTO
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
        @Schema(description = "상품그룹명", example = "프리미엄 코튼 티셔츠") String name,
        @Schema(description = "옵션 타입 (SINGLE, ONE_LEVEL, TWO_LEVEL)", example = "TWO_LEVEL")
                String optionType,
        @Schema(description = "정가", example = "50000") BigDecimal regularPrice,
        @Schema(description = "판매가", example = "39000") BigDecimal currentPrice,
        @Schema(
                        description = "상태 (DRAFT, PENDING, APPROVED, REJECTED, DISCONTINUED)",
                        example = "APPROVED")
                String status,
        @Schema(description = "배송 정책 ID", example = "1") Long shippingPolicyId,
        @Schema(description = "환불 정책 ID", example = "1") Long refundPolicyId,
        @Schema(description = "상품(SKU) 목록") List<ProductSkuV2ApiResponse> products) {

    /**
     * Application Response로부터 API Response 생성
     *
     * @param response Application layer 응답
     * @return API 응답 DTO
     */
    public static ProductGroupV2ApiResponse from(ProductGroupResponse response) {
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
                response.products() != null
                        ? response.products().stream().map(ProductSkuV2ApiResponse::from).toList()
                        : List.of());
    }

    /** 상품(SKU) 응답 DTO */
    @Schema(description = "상품(SKU) 응답")
    public record ProductSkuV2ApiResponse(
            @Schema(description = "상품 ID", example = "1001") Long productId,
            @Schema(description = "상품그룹 ID", example = "1") Long productGroupId,
            @Schema(description = "옵션 타입", example = "TWO_LEVEL") String optionType,
            @Schema(description = "옵션1 명", example = "색상") String option1Name,
            @Schema(description = "옵션1 값", example = "블랙") String option1Value,
            @Schema(description = "옵션2 명", example = "사이즈") String option2Name,
            @Schema(description = "옵션2 값", example = "M") String option2Value,
            @Schema(description = "추가금액", example = "0") BigDecimal additionalPrice,
            @Schema(description = "품절 여부", example = "false") boolean soldOut,
            @Schema(description = "노출 여부", example = "true") boolean displayYn) {

        public static ProductSkuV2ApiResponse from(ProductResponse response) {
            return new ProductSkuV2ApiResponse(
                    response.productId(),
                    response.productGroupId(),
                    response.optionType(),
                    response.option1Name(),
                    response.option1Value(),
                    response.option2Name(),
                    response.option2Value(),
                    response.additionalPrice(),
                    response.soldOut(),
                    response.displayYn());
        }
    }
}
