package com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 장바구니 아이템 삭제 요청 DTO
 *
 * @param cartItemIds 삭제할 아이템 ID 목록
 */
@Schema(description = "장바구니 아이템 삭제 요청")
public record RemoveCartItemsV2ApiRequest(
        @Schema(description = "삭제할 아이템 ID 목록", example = "[1, 2, 3]")
                @NotEmpty(message = "아이템 ID 목록은 비어있을 수 없습니다")
                List<Long> cartItemIds) {}
