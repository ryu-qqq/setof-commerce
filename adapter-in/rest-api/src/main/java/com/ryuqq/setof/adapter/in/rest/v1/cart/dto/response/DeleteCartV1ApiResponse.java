package com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 장바구니 삭제 응답 DTO
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "장바구니 삭제 응답")
public record DeleteCartV1ApiResponse(
        @Schema(description = "장바구니 ID", example = "1") long cartId) {}
