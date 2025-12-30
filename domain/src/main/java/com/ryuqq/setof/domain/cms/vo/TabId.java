package com.ryuqq.setof.domain.cms.vo;

/**
 * Tab ID Value Object
 *
 * <p>DB 전략: MySQL AUTO_INCREMENT
 *
 * @param value ID 값 (null 허용: 신규 생성 시)
 * @author development-team
 * @since 1.0.0
 */
public record TabId(Long value) {

    /** Compact Constructor */
    public TabId {
        if (value != null && value <= 0) {
            throw new IllegalArgumentException("TabId는 양수여야 합니다: " + value);
        }
    }

    /**
     * 신규 생성 - DB AUTO_INCREMENT가 ID 할당 예정
     *
     * @return null 값을 가진 TabId
     */
    public static TabId forNew() {
        return new TabId(null);
    }

    /**
     * 기존 ID 참조 - null 금지
     *
     * @param value ID 값
     * @return TabId 인스턴스
     * @throws IllegalArgumentException value가 null인 경우
     */
    public static TabId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("기존 TabId는 null일 수 없습니다");
        }
        return new TabId(value);
    }

    /**
     * 신규 엔티티 여부 확인
     *
     * @return 신규이면 true
     */
    public boolean isNew() {
        return value == null;
    }
}
