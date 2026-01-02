package com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 장바구니 아이템 선택 상태 변경 요청 DTO
 *
 * @param cartItemIds 변경할 아이템 ID 목록
 * @param selected 선택 상태
 */
@Schema(description = "장바구니 아이템 선택 상태 변경 요청")
public record UpdateCartItemSelectedV2ApiRequest(
        @Schema(description = "변경할 아이템 ID 목록", example = "[1, 2, 3]")
                @NotEmpty(message = "아이템 ID 목록은 비어있을 수 없습니다")
                List<Long> cartItemIds,
        @Schema(description = "선택 상태", example = "true") @NotNull(message = "선택 상태는 필수입니다")
                Boolean selected) {}
