package com.ryuqq.setof.adapter.in.rest.v1.wishlist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * AddWishlistItemV1ApiRequest - 찜 항목 추가 요청 DTO.
 *
 * <p>레거시 CreateUserFavorite 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-003: @Schema 어노테이션 (Request Body).
 *
 * @param productGroupId 찜할 상품 그룹 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "찜 항목 추가 요청")
public record AddWishlistItemV1ApiRequest(
        @Schema(description = "찜할 상품 그룹 ID", example = "1001") @NotNull(message = "상품 그룹 ID는 필수입니다")
                Long productGroupId) {}
