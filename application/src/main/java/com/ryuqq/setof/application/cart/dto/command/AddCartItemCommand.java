package com.ryuqq.setof.application.cart.dto.command;

import java.util.List;

/**
 * 장바구니 항목 추가 Command DTO.
 *
 * <p>레거시 POST /api/v1/cart 기반. 복수 항목 동시 추가 가능.
 *
 * @param memberId 회원 ID (UUIDv7, SecurityContext에서 추출)
 * @param userId 레거시 사용자 ID (SecurityContext에서 추출)
 * @param items 추가할 장바구니 항목 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record AddCartItemCommand(String memberId, Long userId, List<CartItemDetail> items) {

    public static AddCartItemCommand of(String memberId, Long userId, List<CartItemDetail> items) {
        return new AddCartItemCommand(memberId, userId, items);
    }

    /**
     * 장바구니 항목 상세 정보.
     *
     * @param productGroupId 상품 그룹 ID
     * @param productId 상품(SKU) ID
     * @param quantity 수량
     * @param sellerId 판매자 ID
     */
    public record CartItemDetail(
            long productGroupId, long productId, int quantity, long sellerId) {}
}
