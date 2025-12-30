package com.ryuqq.setof.domain.shipment.vo;

import com.ryuqq.setof.domain.shipment.exception.InvalidShipmentIdException;

/**
 * 운송장 ID Value Object
 *
 * <p>운송장의 고유 식별자입니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 * </ul>
 *
 * @param value 운송장 ID 값
 */
public record ShipmentId(Long value) {

    /** Compact Constructor - 검증 로직 */
    public ShipmentId {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value ID 값
     * @return ShipmentId 인스턴스
     * @throws InvalidShipmentIdException value가 유효하지 않은 경우
     */
    public static ShipmentId of(Long value) {
        return new ShipmentId(value);
    }

    private static void validate(Long value) {
        if (value == null || value <= 0) {
            throw new InvalidShipmentIdException(value);
        }
    }
}
