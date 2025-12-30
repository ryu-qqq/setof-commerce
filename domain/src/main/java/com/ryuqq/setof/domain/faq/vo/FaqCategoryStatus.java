package com.ryuqq.setof.domain.faq.vo;

/**
 * FAQ 카테고리 상태 Enum
 *
 * <p>FAQ 카테고리의 활성화 상태를 정의합니다.
 *
 * <p>상태 전이:
 *
 * <pre>
 * ACTIVE ↔ INACTIVE
 * </pre>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지
 *   <li>상태 전이 규칙 캡슐화
 * </ul>
 */
public enum FaqCategoryStatus {

    /** 활성 상태 - FAQ 조회 시 표시됨 */
    ACTIVE("활성"),

    /** 비활성 상태 - FAQ 조회 시 표시되지 않음 */
    INACTIVE("비활성");

    private final String displayName;

    FaqCategoryStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 표시용 이름 반환
     *
     * @return 한글 표시명
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 활성화 가능 여부 (비활성 상태에서만 가능)
     *
     * @return 활성화 가능하면 true
     */
    public boolean canActivate() {
        return this == INACTIVE;
    }

    /**
     * 비활성화 가능 여부 (활성 상태에서만 가능)
     *
     * @return 비활성화 가능하면 true
     */
    public boolean canDeactivate() {
        return this == ACTIVE;
    }

    /**
     * 표시 가능 상태인지 확인
     *
     * @return 활성 상태이면 true
     */
    public boolean isDisplayable() {
        return this == ACTIVE;
    }
}
