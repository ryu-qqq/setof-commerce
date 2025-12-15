package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response;

import com.ryuqq.setof.application.product.dto.response.ProductGroupSummaryResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * 상품그룹 요약 응답 DTO (목록 조회용)
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품그룹 요약 응답")
public record ProductGroupSummaryV2ApiResponse(
        @Schema(description = "상품그룹 ID", example = "1") Long productGroupId,
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "상품그룹명", example = "프리미엄 코튼 티셔츠") String name,
        @Schema(description = "옵션 타입", example = "TWO_LEVEL") String optionType,
        @Schema(description = "판매가", example = "39000") BigDecimal currentPrice,
        @Schema(description = "상태", example = "APPROVED") String status,
        @Schema(description = "상품(SKU) 개수", example = "10") int productCount) {

    /**
     * Application Response로부터 API Response 생성
     *
     * @param response Application layer 응답
     * @return API 응답 DTO
     */
    public static ProductGroupSummaryV2ApiResponse from(ProductGroupSummaryResponse response) {
        return new ProductGroupSummaryV2ApiResponse(
                response.productGroupId(),
                response.sellerId(),
                response.name(),
                response.optionType(),
                response.currentPrice(),
                response.status(),
                response.productCount());
    }
}
