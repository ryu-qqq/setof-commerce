package com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 재고 설정 API 요청 DTO
 *
 * <p>단일 상품의 재고 수량 설정 요청
 *
 * @param quantity 설정할 재고 수량
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "재고 설정 요청")
public record SetStockV2ApiRequest(
        @Schema(description = "설정할 재고 수량", example = "100", minimum = "0")
                @NotNull(message = "재고 수량은 필수입니다")
                @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다")
                Integer quantity) {}
