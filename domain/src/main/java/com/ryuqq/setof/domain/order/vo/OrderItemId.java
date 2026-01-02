package com.ryuqq.setof.domain.order.vo;

import com.ryuqq.setof.domain.order.exception.InvalidOrderItemIdException;
import java.util.UUID;

/**
 * OrderItemId Value Object
 *
 * <p>주문 상품의 고유 식별자입니다.
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
public record OrderItemId(UUID value) {

    /** Compact Constructor - 검증 로직 */
    public OrderItemId {
        if (value == null) {
            throw new InvalidOrderItemIdException("OrderItemId는 필수입니다");
        }
    }

    /**
     * Static Factory Method - 신규 생성
     *
     * @return 새로운 OrderItemId 인스턴스
     */
    public static OrderItemId forNew() {
        return new OrderItemId(UUID.randomUUID());
    }

    /**
     * Static Factory Method - 기존 ID 복원
     *
     * @param value UUID 값
     * @return OrderItemId 인스턴스
     */
    public static OrderItemId of(UUID value) {
        return new OrderItemId(value);
    }

    /**
     * Static Factory Method - 문자열로부터 생성
     *
     * @param value UUID 문자열
     * @return OrderItemId 인스턴스
     */
    public static OrderItemId fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidOrderItemIdException("OrderItemId 문자열은 필수입니다");
        }
        try {
            return new OrderItemId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderItemIdException("잘못된 OrderItemId 형식입니다: " + value);
        }
    }
}
