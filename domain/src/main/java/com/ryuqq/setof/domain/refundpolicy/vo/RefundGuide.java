package com.ryuqq.setof.domain.refundpolicy.vo;

import com.ryuqq.setof.domain.refundpolicy.exception.InvalidRefundGuideException;

/**
 * 반품 안내 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>nullable 허용 - 선택적 필드
 * </ul>
 *
 * @param value 반품 안내 문구 (null 허용)
 */
public record RefundGuide(String value) {

    private static final int MAX_LENGTH = 2000;

    /** Compact Constructor - 검증 로직 */
    public RefundGuide {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 반품 안내 문구 (null 허용)
     * @return RefundGuide 인스턴스
     * @throws InvalidRefundGuideException value가 최대 길이를 초과한 경우
     */
    public static RefundGuide of(String value) {
        return new RefundGuide(value);
    }

    /**
     * 값 존재 여부 확인
     *
     * @return 반품 안내가 있으면 true
     */
    public boolean hasValue() {
        return value != null && !value.isBlank();
    }

    private static void validate(String value) {
        if (value == null) {
            return;
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidRefundGuideException(value);
        }
    }
}
