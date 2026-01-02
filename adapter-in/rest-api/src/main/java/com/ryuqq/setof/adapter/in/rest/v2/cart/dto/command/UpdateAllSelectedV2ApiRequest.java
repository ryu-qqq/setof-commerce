package com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 장바구니 전체 선택/해제 요청 DTO
 *
 * @param selected 선택 상태
 */
@Schema(description = "장바구니 전체 선택/해제 요청")
public record UpdateAllSelectedV2ApiRequest(
        @Schema(description = "선택 상태", example = "true") @NotNull(message = "선택 상태는 필수입니다")
                Boolean selected) {}
