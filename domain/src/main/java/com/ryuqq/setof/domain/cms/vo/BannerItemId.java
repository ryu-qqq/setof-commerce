package com.ryuqq.setof.domain.cms.vo;

/**
 * BannerItem ID Value Object
 *
 * <p>DB 전략: MySQL AUTO_INCREMENT
 *
 * @param value ID 값 (null 허용: 신규 생성 시)
 * @author development-team
 * @since 1.0.0
 */
public record BannerItemId(Long value) {

    /** Compact Constructor */
    public BannerItemId {
        if (value != null && value <= 0) {
            throw new IllegalArgumentException("BannerItemId는 양수여야 합니다: " + value);
        }
    }

    /**
     * 신규 생성 - DB AUTO_INCREMENT가 ID 할당 예정
     *
     * @return null 값을 가진 BannerItemId
     */
    public static BannerItemId forNew() {
        return new BannerItemId(null);
    }

    /**
     * 기존 ID 참조 - null 금지
     *
     * @param value ID 값
     * @return BannerItemId 인스턴스
     * @throws IllegalArgumentException value가 null인 경우
     */
    public static BannerItemId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("기존 BannerItemId는 null일 수 없습니다");
        }
        return new BannerItemId(value);
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
