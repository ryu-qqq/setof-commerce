package com.ryuqq.setof.domain.payment.vo;

import com.ryuqq.setof.domain.payment.exception.InvalidPaymentIdException;
import java.util.UUID;

/**
 * PaymentId Value Object
 *
 * <p>결제의 고유 식별자입니다.
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
public record PaymentId(UUID value) {

    /** Compact Constructor - 검증 로직 */
    public PaymentId {
        if (value == null) {
            throw new InvalidPaymentIdException("PaymentId는 필수입니다");
        }
    }

    /**
     * Static Factory Method - 신규 생성
     *
     * @return 새로운 PaymentId 인스턴스
     */
    public static PaymentId forNew() {
        return new PaymentId(UUID.randomUUID());
    }

    /**
     * Static Factory Method - 기존 ID 복원
     *
     * @param value UUID 값
     * @return PaymentId 인스턴스
     */
    public static PaymentId of(UUID value) {
        return new PaymentId(value);
    }

    /**
     * Static Factory Method - 문자열로부터 생성
     *
     * @param value UUID 문자열
     * @return PaymentId 인스턴스
     * @throws InvalidPaymentIdException 잘못된 UUID 형식인 경우
     */
    public static PaymentId fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidPaymentIdException("PaymentId 문자열은 필수입니다");
        }
        try {
            return new PaymentId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new InvalidPaymentIdException("잘못된 PaymentId 형식입니다: " + value);
        }
    }
}
