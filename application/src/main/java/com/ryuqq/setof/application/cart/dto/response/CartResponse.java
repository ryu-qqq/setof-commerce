package com.ryuqq.setof.application.cart.dto.response;

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
public record CartResponse(
        Long cartId,
        UUID memberId,
        List<CartItemResponse> items,
        BigDecimal totalAmount,
        BigDecimal selectedTotalAmount,
        int itemCount,
        int selectedItemCount,
        int totalQuantity,
        Instant createdAt,
        Instant updatedAt) {}
