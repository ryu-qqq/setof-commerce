package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * CreateOrderInCartV1ApiRequest - 장바구니 주문 항목 요청 DTO.
 *
 * <p>장바구니 결제 요청 시 포함되는 개별 주문 항목입니다. cartId가 추가됩니다.
 *
 * @param cartId 장바구니 항목 ID
 * @param productGroupId 상품 그룹 ID
 * @param productId 상품(옵션) ID
 * @param sellerId 판매자 ID
 * @param quantity 주문 수량
 * @param orderAmount 주문 총 금액
 * @param orderStatus 주문 상태
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "장바구니 주문 항목")
public record CreateOrderInCartV1ApiRequest(
        @Schema(description = "장바구니 항목 ID", example = "101") @NotNull Long cartId,
        @Schema(description = "상품 그룹 ID", example = "1001") @NotNull Long productGroupId,
        @Schema(description = "상품(옵션) ID", example = "2001") @NotNull Long productId,
        @Schema(description = "판매자 ID", example = "301") @NotNull Long sellerId,
        @Schema(description = "주문 수량", example = "1") @Min(1) int quantity,
        @Schema(description = "주문 총 금액", example = "19800") @Min(100) long orderAmount,
        @Schema(description = "주문 상태", example = "ORDER_COMPLETED") @NotNull String orderStatus) {}
