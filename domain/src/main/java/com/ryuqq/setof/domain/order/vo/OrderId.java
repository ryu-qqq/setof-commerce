package com.ryuqq.setof.domain.order.vo;

import com.ryuqq.setof.domain.order.exception.InvalidOrderIdException;
import java.util.UUID;

/**
 * OrderId Value Object
 *
 * <p>주문의 고유 식별자입니다.
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
public record OrderId(UUID value) {

    /** Compact Constructor - 검증 로직 */
    public OrderId {
        if (value == null) {
            throw new InvalidOrderIdException("OrderId는 필수입니다");
        }
    }

    /**
     * Static Factory Method - 신규 생성
     *
     * @return 새로운 OrderId 인스턴스
     */
    public static OrderId forNew() {
        return new OrderId(UUID.randomUUID());
    }

    /**
     * Static Factory Method - 기존 ID 복원
     *
     * @param value UUID 값
     * @return OrderId 인스턴스
     */
    public static OrderId of(UUID value) {
        return new OrderId(value);
    }

    /**
     * Static Factory Method - 문자열로부터 생성
     *
     * @param value UUID 문자열
     * @return OrderId 인스턴스
     * @throws InvalidOrderIdException 잘못된 UUID 형식인 경우
     */
    public static OrderId fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidOrderIdException("OrderId 문자열은 필수입니다");
        }
        try {
            return new OrderId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderIdException("잘못된 OrderId 형식입니다: " + value);
        }
    }
}
