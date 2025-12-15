package com.ryuqq.setof.domain.productnotice.vo;

import com.ryuqq.setof.domain.productnotice.exception.InvalidProductNoticeIdException;

/**
 * 상품고시 식별자 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Long 기반 ID - Auto-increment Primary Key
 * </ul>
 *
 * @param value 상품고시 ID 값
 */
public record ProductNoticeId(Long value) {

    /** Compact Constructor - 검증 로직 */
    public ProductNoticeId {
        validate(value);
    }

    /**
     * Static Factory Method - 기존 ID로 생성
     *
     * @param value Long 값
     * @return ProductNoticeId 인스턴스
     * @throws InvalidProductNoticeIdException value가 유효하지 않은 경우
     */
    public static ProductNoticeId of(Long value) {
        return new ProductNoticeId(value);
    }

    /**
     * Static Factory Method - 신규 생성용 (ID 없음)
     *
     * @return null 값을 가진 ProductNoticeId 인스턴스
     */
    public static ProductNoticeId forNew() {
        return new ProductNoticeId(null);
    }

    /**
     * 신규 생성 여부 확인
     *
     * @return ID가 null이면 true
     */
    public boolean isNew() {
        return value == null;
    }

    private static void validate(Long value) {
        if (value != null && value <= 0) {
            throw new InvalidProductNoticeIdException(value);
        }
    }
}
