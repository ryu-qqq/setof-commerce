package com.ryuqq.setof.domain.product.vo;

import com.ryuqq.setof.domain.product.exception.InvalidProductGroupIdException;

/**
 * 상품그룹 식별자 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Long 기반 ID - Auto-increment Primary Key
 * </ul>
 *
 * @param value 상품그룹 ID 값
 */
public record ProductGroupId(Long value) {

    /** Compact Constructor - 검증 로직 */
    public ProductGroupId {
        validate(value);
    }

    /**
     * Static Factory Method - 기존 ID로 생성
     *
     * @param value Long 값
     * @return ProductGroupId 인스턴스
     * @throws InvalidProductGroupIdException value가 null이거나 유효하지 않은 경우
     */
    public static ProductGroupId of(Long value) {
        if (value == null) {
            throw new InvalidProductGroupIdException(null);
        }
        return new ProductGroupId(value);
    }

    /**
     * Static Factory Method - 신규 생성용 (ID 없음)
     *
     * @return null 값을 가진 ProductGroupId 인스턴스
     */
    public static ProductGroupId forNew() {
        return new ProductGroupId(null);
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
            throw new InvalidProductGroupIdException(value);
        }
    }
}
