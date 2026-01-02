package com.ryuqq.setof.application.cart.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Enriched 장바구니 응답 DTO
 *
 * <p>상품 상세 정보가 포함된 장바구니 응답입니다. V1 Legacy API 호환성을 위해 생성되었습니다.
 *
 * @param cartId 장바구니 ID
 * @param memberId 회원 ID
 * @param items Enriched 아이템 목록
 * @param totalAmount 전체 총액
 * @param selectedTotalAmount 선택된 아이템 총액
 * @param itemCount 전체 아이템 수
 * @param selectedItemCount 선택된 아이템 수
 * @param totalQuantity 전체 수량
 * @param createdAt 생성 시각
 * @param updatedAt 수정 시각
 * @author development-team
 * @since 1.0.0
 */
public record EnrichedCartResponse(
        Long cartId,
        UUID memberId,
        List<EnrichedCartItemResponse> items,
        BigDecimal totalAmount,
        BigDecimal selectedTotalAmount,
        int itemCount,
        int selectedItemCount,
        int totalQuantity,
        Instant createdAt,
        Instant updatedAt) {

    /**
     * 기본 CartResponse와 Enriched 아이템 목록으로부터 생성
     *
     * @param cartResponse 기본 장바구니 응답
     * @param enrichedItems Enriched 아이템 목록
     * @return EnrichedCartResponse
     */
    public static EnrichedCartResponse from(
            CartResponse cartResponse, List<EnrichedCartItemResponse> enrichedItems) {
        return new EnrichedCartResponse(
                cartResponse.cartId(),
                cartResponse.memberId(),
                enrichedItems,
                cartResponse.totalAmount(),
                cartResponse.selectedTotalAmount(),
                cartResponse.itemCount(),
                cartResponse.selectedItemCount(),
                cartResponse.totalQuantity(),
                cartResponse.createdAt(),
                cartResponse.updatedAt());
    }
}
