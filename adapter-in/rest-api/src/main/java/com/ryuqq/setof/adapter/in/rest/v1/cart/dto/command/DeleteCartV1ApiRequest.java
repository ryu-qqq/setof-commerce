package com.ryuqq.setof.adapter.in.rest.v1.cart.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 장바구니 삭제 Request
 *
 * <p>장바구니 항목을 삭제할 때 사용하는 요청 DTO입니다. 여러 항목을 한 번에 삭제할 수 있습니다.
 *
 * @param cartId 삭제할 장바구니 ID
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "장바구니 삭제 요청")
public record DeleteCartV1ApiRequest(
    @Schema(description = "장바구니 ID", example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED)
    long cartId
) {
}
