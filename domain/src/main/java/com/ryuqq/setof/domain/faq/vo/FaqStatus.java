package com.ryuqq.setof.domain.faq.vo;

/**
 * FAQ 상태 Enum
 *
 * <p>FAQ의 노출 상태를 나타냅니다.
 *
 * <p>상태 전이:
 *
 * <ul>
 *   <li>DRAFT → PUBLISHED (publish)
 *   <li>PUBLISHED → HIDDEN (hide)
 *   <li>HIDDEN → PUBLISHED (publish)
 * </ul>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>Spring 의존성 금지
 * </ul>
 */
public enum FaqStatus {

    /** 작성 중 - 아직 게시되지 않음 */
    DRAFT("작성중"),

    /** 게시됨 - 사용자에게 노출 가능 */
    PUBLISHED("게시됨"),

    /** 숨김 - 관리자가 숨김 처리 */
    HIDDEN("숨김");

    private final String displayName;

    FaqStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 표시용 이름 반환
     *
     * @return 한글 표시 이름
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 게시 가능 여부 확인
     *
     * @return 게시 가능하면 true
     */
    public boolean canPublish() {
        return this == DRAFT || this == HIDDEN;
    }

    /**
     * 숨김 가능 여부 확인
     *
     * @return 숨김 가능하면 true
     */
    public boolean canHide() {
        return this == PUBLISHED;
    }

    /**
     * 사용자에게 노출 가능한 상태인지 확인
     *
     * @return 노출 가능하면 true
     */
    public boolean isDisplayable() {
        return this == PUBLISHED;
    }

    /**
     * 문자열로부터 FaqStatus 변환
     *
     * @param value 상태 문자열
     * @return FaqStatus
     * @throws IllegalArgumentException 유효하지 않은 값일 때
     */
    public static FaqStatus from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("FaqStatus value cannot be null or blank");
        }
        try {
            return FaqStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid FaqStatus: " + value);
        }
    }
}
