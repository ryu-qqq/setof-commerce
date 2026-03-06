package com.ryuqq.setof.domain.banner.id;

/**
 * BannerSlideId - 배너 슬라이드 식별자 VO.
 *
 * <p>DOM-ID-001: record 기반, of() 팩터리.
 *
 * <p>DOM-ID-002: forNew() (새 생성), isNew() (영속 전 여부).
 *
 * @param value 배너 슬라이드 ID 값 (null = 미영속)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BannerSlideId(Long value) {

    /**
     * 기존 ID로 생성.
     *
     * @param value 배너 슬라이드 ID
     * @return BannerSlideId
     */
    public static BannerSlideId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("BannerSlideId must be positive");
        }
        return new BannerSlideId(value);
    }

    /**
     * 신규 생성용 ID (DB 할당 전).
     *
     * @return BannerSlideId (value = null)
     */
    public static BannerSlideId forNew() {
        return new BannerSlideId(null);
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
