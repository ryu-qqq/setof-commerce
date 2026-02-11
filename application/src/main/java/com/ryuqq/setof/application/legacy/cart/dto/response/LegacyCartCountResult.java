package com.ryuqq.setof.application.legacy.cart.dto.response;

/**
 * LegacyCartCountResult - 레거시 장바구니 개수 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param userId 사용자 ID
 * @param cartQuantity 장바구니 아이템 개수
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyCartCountResult(long userId, long cartQuantity) {

    /**
     * 팩토리 메서드.
     *
     * @param userId 사용자 ID
     * @param cartQuantity 장바구니 아이템 개수
     * @return LegacyCartCountResult
     */
    public static LegacyCartCountResult of(long userId, long cartQuantity) {
        return new LegacyCartCountResult(userId, cartQuantity);
    }
}
