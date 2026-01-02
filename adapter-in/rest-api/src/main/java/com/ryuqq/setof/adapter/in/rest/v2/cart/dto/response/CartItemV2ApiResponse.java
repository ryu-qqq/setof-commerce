package com.ryuqq.setof.adapter.in.rest.v2.cart.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * 장바구니 아이템 응답 DTO
 *
 * @param cartItemId 장바구니 아이템 ID
 * @param productStockId 상품 재고 ID (SKU)
 * @param productId 상품 ID
 * @param productGroupId 상품 그룹 ID
 * @param sellerId 판매자 ID
 * @param quantity 수량
 * @param unitPrice 단가
 * @param totalPrice 총 가격 (수량 * 단가)
 * @param selected 선택 여부
 * @param addedAt 추가 시각
 */
@Schema(description = "장바구니 아이템 응답")
public record CartItemV2ApiResponse(
        @Schema(description = "장바구니 아이템 ID", example = "1") Long cartItemId,
        @Schema(description = "상품 재고 ID (SKU)", example = "1001") Long productStockId,
        @Schema(description = "상품 ID", example = "100") Long productId,
        @Schema(description = "상품 그룹 ID", example = "10") Long productGroupId,
        @Schema(description = "판매자 ID", example = "1") Long sellerId,
        @Schema(description = "수량", example = "2") int quantity,
        @Schema(description = "단가", example = "29900") BigDecimal unitPrice,
        @Schema(description = "총 가격 (수량 * 단가)", example = "59800") BigDecimal totalPrice,
        @Schema(description = "선택 여부", example = "true") boolean selected,
        @Schema(description = "추가 시각") Instant addedAt) {}
