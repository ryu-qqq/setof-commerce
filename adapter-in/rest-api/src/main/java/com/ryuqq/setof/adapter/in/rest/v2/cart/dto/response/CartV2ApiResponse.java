package com.ryuqq.setof.adapter.in.rest.v2.cart.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * 장바구니 응답 DTO
 *
 * @param cartId 장바구니 ID
 * @param memberId 회원 ID (UUID)
 * @param items 장바구니 아이템 목록
 * @param totalAmount 총 금액 (모든 아이템)
 * @param selectedTotalAmount 선택된 아이템 총 금액
 * @param itemCount 아이템 개수
 * @param selectedItemCount 선택된 아이템 개수
 * @param totalQuantity 총 수량
 * @param createdAt 생성 시각
 * @param updatedAt 수정 시각
 */
@Schema(description = "장바구니 응답")
public record CartV2ApiResponse(
        @Schema(description = "장바구니 ID", example = "1") Long cartId,
        @Schema(description = "회원 ID (UUID)", example = "019538ab-faac-7ab6-9ab0-17e7f91a51c7")
                UUID memberId,
        @Schema(description = "장바구니 아이템 목록") List<CartItemV2ApiResponse> items,
        @Schema(description = "총 금액 (모든 아이템)", example = "119600") BigDecimal totalAmount,
        @Schema(description = "선택된 아이템 총 금액", example = "59800") BigDecimal selectedTotalAmount,
        @Schema(description = "아이템 개수", example = "3") int itemCount,
        @Schema(description = "선택된 아이템 개수", example = "2") int selectedItemCount,
        @Schema(description = "총 수량", example = "5") int totalQuantity,
        @Schema(description = "생성 시각") Instant createdAt,
        @Schema(description = "수정 시각") Instant updatedAt) {}
