package com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 장바구니 아이템 추가 요청 DTO
 *
 * @param productStockId 상품 재고 ID (SKU)
 * @param productId 상품 ID
 * @param productGroupId 상품 그룹 ID
 * @param sellerId 판매자 ID
 * @param quantity 수량
 * @param unitPrice 단가
 */
@Schema(description = "장바구니 아이템 추가 요청")
public record AddCartItemV2ApiRequest(
        @Schema(description = "상품 재고 ID (SKU)", example = "1001")
                @NotNull(message = "상품 재고 ID는 필수입니다")
                Long productStockId,
        @Schema(description = "상품 ID", example = "100") @NotNull(message = "상품 ID는 필수입니다")
                Long productId,
        @Schema(description = "상품 그룹 ID", example = "10") @NotNull(message = "상품 그룹 ID는 필수입니다")
                Long productGroupId,
        @Schema(description = "판매자 ID", example = "1") @NotNull(message = "판매자 ID는 필수입니다")
                Long sellerId,
        @Schema(description = "수량", example = "1")
                @NotNull(message = "수량은 필수입니다")
                @Min(value = 1, message = "수량은 1 이상이어야 합니다")
                Integer quantity,
        @Schema(description = "단가", example = "29900") @NotNull(message = "단가는 필수입니다")
                BigDecimal unitPrice) {}
