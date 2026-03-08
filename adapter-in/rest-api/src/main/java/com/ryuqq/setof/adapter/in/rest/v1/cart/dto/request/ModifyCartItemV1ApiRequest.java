package com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * ModifyCartItemV1ApiRequest - 장바구니 항목 수정 요청 DTO.
 *
 * <p>레거시 CartDetails(@Embeddable) 기반 변환.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-003: @Schema 어노테이션 (Request Body).
 *
 * <p>비즈니스 규칙: PUT /api/v1/cart/{cartId} - cartId는 PathVariable로 수신하며, 실제 업데이트 대상 필드는 quantity만입니다.
 * productGroupId, productId, sellerId는 구조 호환성을 위해 포함되지만 서비스 레이어에서 무시됩니다.
 *
 * @param quantity 변경할 수량 (1~999, 필수)
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.cart.entity.CartDetails
 */
@Schema(description = "장바구니 항목 수정 요청")
public record ModifyCartItemV1ApiRequest(
        @Schema(description = "변경할 수량 (1~999)", example = "3")
                @Min(value = 1, message = "수량은 1 이상이어야 합니다")
                @Max(value = 999, message = "수량은 999 이하여야 합니다")
                int quantity) {}
