package com.ryuqq.setof.domain.product.vo;

/**
 * 상품그룹 상태 Value Object (Enum)
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Enum 사용
 *   <li>불변성 보장 - Java Enum 특성
 * </ul>
 *
 * <p>상품그룹 상태:
 *
 * <ul>
 *   <li>ACTIVE: 활성 상태 (판매 중)
 *   <li>INACTIVE: 비활성 상태 (판매 중지)
 *   <li>DELETED: 삭제됨 (Soft Delete)
 * </ul>
 */
public enum ProductGroupStatus {
    ACTIVE("활성"),
    INACTIVE("비활성"),
    DELETED("삭제됨");

    private final String displayName;

    ProductGroupStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 표시명 반환
     *
     * @return 상태 표시명
     */
    public String displayName() {
        return displayName;
    }

    /**
     * 활성 상태 여부 확인
     *
     * @return 활성 상태이면 true
     */
    public boolean isActive() {
        return this == ACTIVE;
    }

    /**
     * 비활성 상태 여부 확인
     *
     * @return 비활성 상태이면 true
     */
    public boolean isInactive() {
        return this == INACTIVE;
    }

    /**
     * 삭제 상태 여부 확인
     *
     * @return 삭제 상태이면 true
     */
    public boolean isDeleted() {
        return this == DELETED;
    }

    /**
     * 판매 가능 상태 여부 확인
     *
     * @return 판매 가능 상태이면 true (활성만 해당)
     */
    public boolean isSellable() {
        return this == ACTIVE;
    }

    /**
     * 수정 가능 상태 여부 확인
     *
     * @return 삭제되지 않은 상태이면 true
     */
    public boolean isEditable() {
        return this != DELETED;
    }
}
