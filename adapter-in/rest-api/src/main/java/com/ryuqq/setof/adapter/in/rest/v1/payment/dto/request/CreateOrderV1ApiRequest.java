package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * CreateOrderV1ApiRequest - 주문 항목 요청 DTO.
 *
 * <p>결제 요청 시 포함되는 개별 주문 항목입니다.
 *
 * @param productGroupId 상품 그룹 ID
 * @param productId 상품(옵션) ID
 * @param sellerId 판매자 ID
 * @param quantity 주문 수량
 * @param orderAmount 주문 총 금액 (단가 x 수량)
 * @param orderStatus 주문 상태 (Legacy 호환, 기본 ORDER_COMPLETED)
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "주문 항목")
public record CreateOrderV1ApiRequest(
        @Schema(description = "상품 그룹 ID", example = "100") @NotNull Long productGroupId,
        @Schema(description = "상품(옵션) ID", example = "201") @NotNull Long productId,
        @Schema(description = "판매자 ID", example = "10") @NotNull Long sellerId,
        @Schema(description = "주문 수량", example = "2") @Min(1) int quantity,
        @Schema(description = "주문 총 금액", example = "29800") @Min(100) long orderAmount,
        @Schema(description = "주문 상태", example = "ORDER_COMPLETED") @NotNull String orderStatus) {}
