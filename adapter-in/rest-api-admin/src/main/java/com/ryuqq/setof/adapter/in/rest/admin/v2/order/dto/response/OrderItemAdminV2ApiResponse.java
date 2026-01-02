package com.ryuqq.setof.adapter.in.rest.admin.v2.order.dto.response;

import com.ryuqq.setof.application.order.dto.response.OrderItemResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * 주문 상품 Admin API 응답 DTO
 *
 * @param orderItemId 주문 상품 ID (UUID)
 * @param productId 상품 ID
 * @param productStockId 상품 재고 ID
 * @param orderedQuantity 주문 수량
 * @param cancelledQuantity 취소 수량
 * @param refundedQuantity 환불 수량
 * @param effectiveQuantity 유효 수량
 * @param unitPrice 개당 가격
 * @param totalPrice 총 가격
 * @param status 상품 상태
 * @param productName 상품명
 * @param productImage 상품 이미지 URL
 * @param optionName 옵션명
 * @param brandName 브랜드명
 * @param sellerName 판매자명
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "주문 상품 응답")
public record OrderItemAdminV2ApiResponse(
        @Schema(description = "주문 상품 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
                String orderItemId,
        @Schema(description = "상품 ID", example = "100") Long productId,
        @Schema(description = "상품 재고 ID", example = "1001") Long productStockId,
        @Schema(description = "주문 수량", example = "2") int orderedQuantity,
        @Schema(description = "취소 수량", example = "0") int cancelledQuantity,
        @Schema(description = "환불 수량", example = "0") int refundedQuantity,
        @Schema(description = "유효 수량", example = "2") int effectiveQuantity,
        @Schema(description = "개당 가격", example = "29900") BigDecimal unitPrice,
        @Schema(description = "총 가격", example = "59800") BigDecimal totalPrice,
        @Schema(description = "상품 상태", example = "ORDERED") String status,
        @Schema(description = "상품명", example = "스프링 부트 3.0 실전 가이드") String productName,
        @Schema(description = "상품 이미지 URL", example = "https://example.com/image.jpg")
                String productImage,
        @Schema(description = "옵션명", example = "블랙 / XL") String optionName,
        @Schema(description = "브랜드명", example = "Nike") String brandName,
        @Schema(description = "판매자명", example = "공식스토어") String sellerName) {

    /**
     * Application Response → API Response 변환
     *
     * @param response Application 응답
     * @return API 응답
     */
    public static OrderItemAdminV2ApiResponse from(OrderItemResponse response) {
        return new OrderItemAdminV2ApiResponse(
                response.orderItemId(),
                response.productId(),
                response.productStockId(),
                response.orderedQuantity(),
                response.cancelledQuantity(),
                response.refundedQuantity(),
                response.effectiveQuantity(),
                response.unitPrice(),
                response.totalPrice(),
                response.status(),
                response.productName(),
                response.productImage(),
                response.optionName(),
                response.brandName(),
                response.sellerName());
    }
}
