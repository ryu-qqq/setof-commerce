package com.ryuqq.setof.domain.productstock.vo;

import com.ryuqq.setof.domain.productstock.exception.InvalidProductStockIdException;

/**
 * 재고 식별자 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>0 이하 값 허용 안함
 * </ul>
 *
 * @param value 재고 ID 값
 */
public record ProductStockId(Long value) {

    /** Compact Constructor - 검증 로직 */
    public ProductStockId {
        validate(value);
    }

    /**
     * Static Factory Method - 기존 ID로 생성
     *
     * @param value Long 값
     * @return ProductStockId 인스턴스
     * @throws InvalidProductStockIdException value가 null이거나 유효하지 않은 경우
     */
    public static ProductStockId of(Long value) {
        return new ProductStockId(value);
    }

    /**
     * Static Factory Method - 신규 생성용 (ID 없음)
     *
     * @return null 값을 가진 ProductStockId 인스턴스
     */
    public static ProductStockId forNew() {
        return new ProductStockId(null);
    }

    /**
     * 신규 생성 여부 확인
     *
     * @return ID가 null이면 true (아직 영속화되지 않은 신규 엔티티)
     */
    public boolean isNew() {
        return value == null;
    }

    private static void validate(Long value) {
        if (value != null && value <= 0) {
            throw new InvalidProductStockIdException(value);
        }
    }
}
