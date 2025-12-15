package com.ryuqq.setof.domain.productnotice.vo;

/**
 * 상품고시 필드 Value Object
 *
 * <p>상품고시 템플릿에 포함되는 필드 정의를 나타냅니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Self-validation - Compact Constructor에서 검증
 * </ul>
 *
 * @param key 필드 키 (예: "origin", "material")
 * @param description 필드 설명 (입력 가이드)
 * @param required 필수 여부
 * @param displayOrder 표시 순서
 */
public record NoticeField(String key, String description, boolean required, int displayOrder) {

    private static final int MAX_KEY_LENGTH = 50;
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    /** Compact Constructor - 검증 로직 */
    public NoticeField {
        validate(key, description, displayOrder);
    }

    /**
     * Static Factory Method - 필수 필드 생성
     *
     * @param key 필드 키
     * @param description 필드 설명
     * @param displayOrder 표시 순서
     * @return NoticeField 인스턴스
     */
    public static NoticeField required(String key, String description, int displayOrder) {
        return new NoticeField(key, description, true, displayOrder);
    }

    /**
     * Static Factory Method - 선택 필드 생성
     *
     * @param key 필드 키
     * @param description 필드 설명
     * @param displayOrder 표시 순서
     * @return NoticeField 인스턴스
     */
    public static NoticeField optional(String key, String description, int displayOrder) {
        return new NoticeField(key, description, false, displayOrder);
    }

    /**
     * Static Factory Method - 일반 생성
     *
     * @param key 필드 키
     * @param description 필드 설명
     * @param required 필수 여부
     * @param displayOrder 표시 순서
     * @return NoticeField 인스턴스
     */
    public static NoticeField of(
            String key, String description, boolean required, int displayOrder) {
        return new NoticeField(key, description, required, displayOrder);
    }

    /**
     * 필수 여부 확인 (명시적 메서드명)
     *
     * @return 필수 필드면 true
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * 선택 여부 확인
     *
     * @return 선택 필드면 true
     */
    public boolean isOptional() {
        return !required;
    }

    private static void validate(String key, String description, int displayOrder) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("필드 키는 필수입니다");
        }
        if (key.length() > MAX_KEY_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("필드 키 길이가 %d자를 초과합니다: %d", MAX_KEY_LENGTH, key.length()));
        }
        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException(
                    String.format(
                            "필드 설명 길이가 %d자를 초과합니다: %d",
                            MAX_DESCRIPTION_LENGTH, description.length()));
        }
        if (displayOrder <= 0) {
            throw new IllegalArgumentException("표시 순서는 1 이상이어야 합니다: " + displayOrder);
        }
    }
}
