package com.ryuqq.setof.domain.shippingpolicy.vo;

import com.ryuqq.setof.domain.shippingpolicy.exception.InvalidDisplayOrderException;

/**
 * 표시 순서 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>0 이상의 정수 (음수 불가)
 * </ul>
 *
 * @param value 표시 순서 값
 */
public record DisplayOrder(Integer value) {

    /** Compact Constructor - 검증 로직 */
    public DisplayOrder {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 표시 순서 값
     * @return DisplayOrder 인스턴스
     * @throws InvalidDisplayOrderException value가 null이거나 음수인 경우
     */
    public static DisplayOrder of(Integer value) {
        return new DisplayOrder(value);
    }

    /**
     * 기본값 생성
     *
     * @return 순서 0인 DisplayOrder 인스턴스
     */
    public static DisplayOrder defaultOrder() {
        return new DisplayOrder(0);
    }

    private static void validate(Integer value) {
        if (value == null) {
            throw new InvalidDisplayOrderException(null, "표시 순서는 필수입니다.");
        }
        if (value < 0) {
            throw new InvalidDisplayOrderException(value, "표시 순서는 0 이상이어야 합니다.");
        }
    }
}
