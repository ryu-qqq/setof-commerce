package com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 장바구니 개수 Response
 *
 * <p>장바구니 개수를 반환하는 응답 DTO입니다.
 *
 * @param cartQuantity 장바구니 개수
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "장바구니 개수 응답")
public record CartCountV1ApiResponse(
        @Schema(description = "장바구니 개수", example = "5") Integer cartQuantity) {}
