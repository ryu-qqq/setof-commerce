package com.ryuqq.setof.domain.cms.vo;

/**
 * Banner ID Value Object
 *
 * <p>DB 전략: MySQL AUTO_INCREMENT
 *
 * @param value ID 값 (null 허용: 신규 생성 시)
 * @author development-team
 * @since 1.0.0
 */
public record BannerId(Long value) {

    /** Compact Constructor */
    public BannerId {
        if (value != null && value <= 0) {
            throw new IllegalArgumentException("BannerId는 양수여야 합니다: " + value);
        }
    }

    /**
     * 신규 생성 - DB AUTO_INCREMENT가 ID 할당 예정
     *
     * @return null 값을 가진 BannerId
     */
    public static BannerId forNew() {
        return new BannerId(null);
    }

    /**
     * 기존 ID 참조 - null 금지
     *
     * @param value ID 값
     * @return BannerId 인스턴스
     * @throws IllegalArgumentException value가 null인 경우
     */
    public static BannerId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("기존 BannerId는 null일 수 없습니다");
        }
        return new BannerId(value);
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
