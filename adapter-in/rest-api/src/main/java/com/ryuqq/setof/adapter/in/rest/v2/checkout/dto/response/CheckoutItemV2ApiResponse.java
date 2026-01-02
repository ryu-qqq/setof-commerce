package com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.response;

import com.ryuqq.setof.application.checkout.dto.response.CheckoutItemResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * 체크아웃 아이템 API 응답 DTO
 *
 * @param productStockId 상품 재고 ID
 * @param productId 상품 ID
 * @param sellerId 판매자 ID
 * @param quantity 수량
 * @param unitPrice 단가
 * @param totalPrice 합계 금액
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "체크아웃 아이템 응답")
public record CheckoutItemV2ApiResponse(
        @Schema(description = "상품 재고 ID", example = "1001") Long productStockId,
        @Schema(description = "상품 ID", example = "100") Long productId,
        @Schema(description = "판매자 ID", example = "10") Long sellerId,
        @Schema(description = "수량", example = "2") int quantity,
        @Schema(description = "단가", example = "29900") BigDecimal unitPrice,
        @Schema(description = "합계 금액", example = "59800") BigDecimal totalPrice) {

    /**
     * Application Response → API Response 변환
     *
     * @param response Application 응답
     * @return API 응답
     */
    public static CheckoutItemV2ApiResponse from(CheckoutItemResponse response) {
        return new CheckoutItemV2ApiResponse(
                response.productStockId(),
                response.productId(),
                response.sellerId(),
                response.quantity(),
                response.unitPrice(),
                response.totalPrice());
    }
}
