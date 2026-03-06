package com.ryuqq.setof.domain.contentpage.id;

/**
 * DisplayComponentId - 디스플레이 컴포넌트 식별자 VO.
 *
 * <p>DOM-ID-001: record 기반, of() 팩터리.
 *
 * <p>DOM-ID-002: forNew() (새 생성), isNew() (영속 전 여부).
 *
 * @param value 디스플레이 컴포넌트 ID 값 (null = 미영속)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DisplayComponentId(Long value) {

    /**
     * 기존 ID로 생성.
     *
     * @param value 디스플레이 컴포넌트 ID
     * @return DisplayComponentId
     */
    public static DisplayComponentId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("DisplayComponentId must be positive");
        }
        return new DisplayComponentId(value);
    }

    /**
     * 신규 생성용 ID (DB 할당 전).
     *
     * @return DisplayComponentId (value = null)
     */
    public static DisplayComponentId forNew() {
        return new DisplayComponentId(null);
    }

    /**
     * 아직 영속되지 않은 새 객체 여부.
     *
     * @return true if not yet persisted
     */
    public boolean isNew() {
        return value == null;
    }
}
