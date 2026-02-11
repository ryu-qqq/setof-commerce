package com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * CartCountV1ApiResponse - 장바구니 아이템 개수 응답 DTO.
 *
 * <p>레거시 CartCountDto 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param cartQuantity 장바구니에 담긴 아이템 개수
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.cart.dto.CartCountDto
 */
@Schema(description = "장바구니 아이템 개수 응답")
public record CartCountV1ApiResponse(
        @Schema(description = "장바구니 아이템 개수", example = "5") long cartQuantity) {}
