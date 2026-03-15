package com.ryuqq.setof.application.cart.dto.command;

/**
 * 장바구니 항목 수량 수정 Command DTO.
 *
 * <p>레거시 PUT /api/v1/cart/{cartId} 기반. cartId + userId 소유권 검증 포함.
 *
 * @param cartId 수정할 장바구니 항목 ID
 * @param memberId 회원 ID (Long PK, SecurityContext에서 추출)
 * @param userId 레거시 사용자 ID (SecurityContext에서 추출)
 * @param newQuantity 변경할 수량
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ModifyCartItemCommand(Long cartId, Long memberId, Long userId, int newQuantity) {

    public static ModifyCartItemCommand of(
            Long cartId, Long memberId, Long userId, int newQuantity) {
        return new ModifyCartItemCommand(cartId, memberId, userId, newQuantity);
    }
}
