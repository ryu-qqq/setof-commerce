package com.ryuqq.setof.domain.cms.vo;

/**
 * Component ID Value Object
 *
 * <p>DB 전략: MySQL AUTO_INCREMENT
 *
 * @param value ID 값 (null 허용: 신규 생성 시)
 * @author development-team
 * @since 1.0.0
 */
public record ComponentId(Long value) {

    /** Compact Constructor */
    public ComponentId {
        if (value != null && value <= 0) {
            throw new IllegalArgumentException("ComponentId는 양수여야 합니다: " + value);
        }
    }

    /**
     * 신규 생성 - DB AUTO_INCREMENT가 ID 할당 예정
     *
     * @return null 값을 가진 ComponentId
     */
    public static ComponentId forNew() {
        return new ComponentId(null);
    }

    /**
     * 기존 ID 참조 - null 금지
     *
     * @param value ID 값
     * @return ComponentId 인스턴스
     * @throws IllegalArgumentException value가 null인 경우
     */
    public static ComponentId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("기존 ComponentId는 null일 수 없습니다");
        }
        return new ComponentId(value);
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
