package com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * AddCartItemV1ApiRequest - 장바구니 항목 추가 요청 DTO.
 *
 * <p>레거시 CartDetails(@Embeddable) 기반 변환.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-003: @Schema 어노테이션 (Request Body).
 *
 * <p>레거시 요청은 List&lt;CartDetails&gt; 배열 형태였으나, 신규 아키텍처에서는 Controller에서
 * List&lt;AddCartItemV1ApiRequest&gt;로 수신합니다.
 *
 * <p>비즈니스 규칙: 동일 productId의 항목이 이미 존재하면 수량을 덮어씁니다 (Upsert).
 *
 * @param productGroupId 상품 그룹 ID (필수)
 * @param productId 상품(SKU) ID (필수)
 * @param quantity 수량 (1~999, 필수)
 * @param sellerId 판매자 ID (필수)
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.cart.entity.CartDetails
 */
@Schema(description = "장바구니 항목 추가 요청")
public record AddCartItemV1ApiRequest(
        @Schema(description = "상품 그룹 ID", example = "101") @NotNull(message = "상품 그룹 ID는 필수입니다")
                Long productGroupId,
        @Schema(description = "상품(SKU) ID", example = "1001") @NotNull(message = "상품 ID는 필수입니다")
                Long productId,
        @Schema(description = "수량 (1~999)", example = "2")
                @Min(value = 1, message = "수량은 1 이상이어야 합니다")
                @Max(value = 999, message = "수량은 999 이하여야 합니다")
                int quantity,
        @Schema(description = "판매자 ID", example = "10") @NotNull(message = "판매자 ID는 필수입니다")
                Long sellerId) {}
