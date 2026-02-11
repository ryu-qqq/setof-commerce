package com.ryuqq.setof.domain.productnotice.vo;

/**
 * 상품고시 항목 필드값 Value Object.
 *
 * <p>상품고시의 개별 항목(필드명, 필드값)을 표현합니다.
 *
 * @param fieldName 필드명 (예: "소재", "세탁방법", "제조국")
 * @param fieldValue 필드값 (예: "면 100%", "손세탁", "대한민국")
 */
public record NoticeFieldValue(String fieldName, String fieldValue) {

    private static final int FIELD_NAME_MAX_LENGTH = 100;
    private static final int FIELD_VALUE_MAX_LENGTH = 500;

    public NoticeFieldValue {
        if (fieldName == null || fieldName.isBlank()) {
            throw new IllegalArgumentException("고시 항목 필드명은 필수입니다");
        }
        fieldName = fieldName.trim();
        if (fieldName.length() > FIELD_NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "고시 항목 필드명은 " + FIELD_NAME_MAX_LENGTH + "자를 초과할 수 없습니다");
        }
        if (fieldValue != null) {
            fieldValue = fieldValue.trim();
            if (fieldValue.length() > FIELD_VALUE_MAX_LENGTH) {
                throw new IllegalArgumentException(
                        "고시 항목 필드값은 " + FIELD_VALUE_MAX_LENGTH + "자를 초과할 수 없습니다");
            }
            if (fieldValue.isEmpty()) {
                fieldValue = null;
            }
        }
    }

    public static NoticeFieldValue of(String fieldName, String fieldValue) {
        return new NoticeFieldValue(fieldName, fieldValue);
    }

    public boolean hasValue() {
        return fieldValue != null && !fieldValue.isBlank();
    }
}
