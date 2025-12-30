package com.ryuqq.setof.domain.checkout.vo;

import com.ryuqq.setof.domain.checkout.exception.InvalidCheckoutIdException;
import java.util.UUID;

/**
 * CheckoutId Value Object
 *
 * <p>결제 세션의 고유 식별자입니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>UUID 기반 생성 (충돌 방지)
 * </ul>
 *
 * @param value UUID 값
 */
public record CheckoutId(UUID value) {

    /** Compact Constructor - 검증 로직 */
    public CheckoutId {
        if (value == null) {
            throw new InvalidCheckoutIdException("CheckoutId는 필수입니다");
        }
    }

    /**
     * Static Factory Method - 신규 생성
     *
     * @return 새로운 CheckoutId 인스턴스
     */
    public static CheckoutId forNew() {
        return new CheckoutId(UUID.randomUUID());
    }

    /**
     * Static Factory Method - 기존 ID 복원
     *
     * @param value UUID 값
     * @return CheckoutId 인스턴스
     */
    public static CheckoutId of(UUID value) {
        return new CheckoutId(value);
    }

    /**
     * Static Factory Method - 문자열로부터 생성
     *
     * @param value UUID 문자열
     * @return CheckoutId 인스턴스
     * @throws InvalidCheckoutIdException 잘못된 UUID 형식인 경우
     */
    public static CheckoutId fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidCheckoutIdException("CheckoutId 문자열은 필수입니다");
        }
        try {
            return new CheckoutId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new InvalidCheckoutIdException("잘못된 CheckoutId 형식입니다: " + value);
        }
    }
}
