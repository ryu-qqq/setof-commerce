package com.ryuqq.setof.domain.cart.vo;

import com.ryuqq.setof.domain.cart.exception.InvalidCartItemIdException;

/**
 * CartItemId Value Object
 *
 * <p>장바구니 아이템의 고유 식별자입니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Long 기반 생성 (DB auto-increment 매핑)
 * </ul>
 *
 * @param value Long 값
 */
public record CartItemId(Long value) {

    /** Compact Constructor - 검증 로직 */
    public CartItemId {
        if (value == null || value <= 0) {
            throw new InvalidCartItemIdException("CartItemId는 양수여야 합니다");
        }
    }

    /**
     * Static Factory Method - 기존 ID 복원
     *
     * @param value Long 값
     * @return CartItemId 인스턴스
     */
    public static CartItemId of(Long value) {
        return new CartItemId(value);
    }
}
