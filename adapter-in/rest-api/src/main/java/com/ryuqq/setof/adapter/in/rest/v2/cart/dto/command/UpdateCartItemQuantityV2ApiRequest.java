package com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 장바구니 아이템 수량 변경 요청 DTO
 *
 * @param quantity 새 수량
 */
@Schema(description = "장바구니 아이템 수량 변경 요청")
public record UpdateCartItemQuantityV2ApiRequest(
        @Schema(description = "새 수량", example = "2")
                @NotNull(message = "수량은 필수입니다")
                @Min(value = 1, message = "수량은 1 이상이어야 합니다")
                @Max(value = 99, message = "수량은 99 이하여야 합니다")
                Integer quantity) {}
