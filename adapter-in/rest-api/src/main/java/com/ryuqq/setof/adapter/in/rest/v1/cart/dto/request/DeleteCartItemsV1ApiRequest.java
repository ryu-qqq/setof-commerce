package com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DeleteCartItemsV1ApiRequest - 장바구니 항목 삭제 요청 DTO.
 *
 * <p>레거시 CartDeleteRequestDto(record) 기반 변환.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-007: @Parameter 어노테이션 (Query Parameters, @ModelAttribute 바인딩).
 *
 * <p>바인딩 방식: DELETE /api/v1/carts?cartId={cartId} (@ModelAttribute)
 *
 * <p>비즈니스 규칙: 소프트 딜리트 방식(delete_yn = 'Y')으로 처리. SecurityUtils.currentUserId()로 본인 소유 검증이 서비스 레이어에서
 * 수행됩니다.
 *
 * @param cartId 삭제할 장바구니 항목 ID (필수)
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.cart.dto.CartDeleteRequestDto
 */
@Schema(description = "장바구니 항목 삭제 요청")
public record DeleteCartItemsV1ApiRequest(
        @Parameter(description = "삭제할 장바구니 항목 ID", example = "42", required = true) long cartId) {}
