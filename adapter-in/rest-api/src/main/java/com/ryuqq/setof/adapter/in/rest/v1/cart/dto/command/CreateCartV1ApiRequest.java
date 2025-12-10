package com.ryuqq.setof.adapter.in.rest.v1.cart.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 장바구니 추가 Request
 *
 * <p>장바구니에 상품을 추가하거나 수정할 때 사용하는 요청 DTO입니다.
 *
 * @param productGroupId 상품 그룹 ID
 * @param productId 상품 ID
 * @param quantity 수량
 * @param sellerId 판매자 ID
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "장바구니 추가/수정 요청")
public record CreateCartV1ApiRequest(
    @Schema(
        description = "상품 그룹 ID",
        example = "12345",
        requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "상품 그룹 아이디는 필수 입니다.")
    Long productGroupId,
    @Schema(
        description = "상품 ID",
        example = "67890",
        requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "상품 아이디는 필수 입니다.")
    Long productId,
    @Schema(
        description = "수량",
        example = "2",
        minimum = "1",
        maximum = "999",
        requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 1, message = "재고의 수량은 0 보다 커야합니다.")
    @Max(value = 999, message = "재고의 수량은 999 보다 작아야 합니다.")
    Integer quantity,
    @Schema(
        description = "판매자 ID",
        example = "100",
        requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "판매자 아이디는 필수 입니다.")
    Long sellerId
) {}
