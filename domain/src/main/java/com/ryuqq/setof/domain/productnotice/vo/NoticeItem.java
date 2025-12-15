package com.ryuqq.setof.domain.productnotice.vo;

/**
 * 상품고시 항목 Value Object
 *
 * <p>상품에 입력된 고시 데이터를 나타냅니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Self-validation - Compact Constructor에서 검증
 * </ul>
 *
 * @param fieldKey 필드 키 (템플릿의 NoticeField.key와 매핑)
 * @param fieldValue 필드 값 (사용자 입력)
 * @param displayOrder 표시 순서
 */
public record NoticeItem(String fieldKey, String fieldValue, int displayOrder) {

    private static final int MAX_KEY_LENGTH = 50;
    private static final int MAX_VALUE_LENGTH = 2000;

    /** Compact Constructor - 검증 로직 */
    public NoticeItem {
        validate(fieldKey, fieldValue, displayOrder);
    }

    /**
     * Static Factory Method - 일반 생성
     *
     * @param fieldKey 필드 키
     * @param fieldValue 필드 값
     * @param displayOrder 표시 순서
     * @return NoticeItem 인스턴스
     */
    public static NoticeItem of(String fieldKey, String fieldValue, int displayOrder) {
        return new NoticeItem(fieldKey, fieldValue, displayOrder);
    }

    /**
     * 값 존재 여부 확인
     *
     * @return 값이 비어있지 않으면 true
     */
    public boolean hasValue() {
        return fieldValue != null && !fieldValue.isBlank();
    }

    /**
     * 필드 값 업데이트하여 새 인스턴스 반환
     *
     * @param newValue 새 필드 값
     * @return 값이 업데이트된 새 NoticeItem 인스턴스
     */
    public NoticeItem updateValue(String newValue) {
        return new NoticeItem(this.fieldKey, newValue, this.displayOrder);
    }

    private static void validate(String fieldKey, String fieldValue, int displayOrder) {
        if (fieldKey == null || fieldKey.isBlank()) {
            throw new IllegalArgumentException("필드 키는 필수입니다");
        }
        if (fieldKey.length() > MAX_KEY_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("필드 키 길이가 %d자를 초과합니다: %d", MAX_KEY_LENGTH, fieldKey.length()));
        }
        if (fieldValue != null && fieldValue.length() > MAX_VALUE_LENGTH) {
            throw new IllegalArgumentException(
                    String.format(
                            "필드 값 길이가 %d자를 초과합니다: %d", MAX_VALUE_LENGTH, fieldValue.length()));
        }
        if (displayOrder <= 0) {
            throw new IllegalArgumentException("표시 순서는 1 이상이어야 합니다: " + displayOrder);
        }
    }
}
