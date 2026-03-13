package com.ryuqq.setof.adapter.out.persistence.banner;

import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerSlideJpaEntity;
import java.time.Instant;

/**
 * BannerGroupJpaEntity / BannerSlideJpaEntity 테스트 Fixtures.
 *
 * <p>테스트에서 Banner 관련 객체들을 생성합니다.
 */
public final class BannerJpaEntityFixtures {

    private BannerJpaEntityFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_GROUP_ID = 1L;
    public static final Long DEFAULT_SLIDE_ID = 1L;
    public static final String DEFAULT_TITLE = "테스트 배너 그룹";
    public static final String DEFAULT_BANNER_TYPE = "RECOMMEND";
    public static final String DEFAULT_SLIDE_TITLE = "테스트 슬라이드";
    public static final String DEFAULT_IMAGE_URL = "https://example.com/banner.png";
    public static final String DEFAULT_LINK_URL = "https://example.com/link";
    public static final int DEFAULT_DISPLAY_ORDER = 1;
    public static final Instant DEFAULT_DISPLAY_START_AT = Instant.now().minusSeconds(3600);
    public static final Instant DEFAULT_DISPLAY_END_AT = Instant.now().plusSeconds(86400);

    // ===== BannerGroupJpaEntity Fixtures =====

    /** 활성 상태의 배너 그룹 Entity 생성. */
    public static BannerGroupJpaEntity activeGroupEntity() {
        Instant now = Instant.now();
        return BannerGroupJpaEntity.create(
                DEFAULT_GROUP_ID,
                DEFAULT_TITLE,
                DEFAULT_BANNER_TYPE,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                now,
                now,
                null);
    }

    /** ID를 지정한 활성 배너 그룹 Entity 생성. */
    public static BannerGroupJpaEntity activeGroupEntity(Long id) {
        Instant now = Instant.now();
        return BannerGroupJpaEntity.create(
                id,
                DEFAULT_TITLE,
                DEFAULT_BANNER_TYPE,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                now,
                now,
                null);
    }

    /** 비활성 배너 그룹 Entity 생성. */
    public static BannerGroupJpaEntity inactiveGroupEntity() {
        Instant now = Instant.now();
        return BannerGroupJpaEntity.create(
                2L,
                DEFAULT_TITLE,
                DEFAULT_BANNER_TYPE,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                null);
    }

    /** 삭제된 배너 그룹 Entity 생성. */
    public static BannerGroupJpaEntity deletedGroupEntity() {
        Instant now = Instant.now();
        return BannerGroupJpaEntity.create(
                3L,
                DEFAULT_TITLE,
                DEFAULT_BANNER_TYPE,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                now);
    }

    // ===== BannerSlideJpaEntity Fixtures =====

    /** 활성 상태의 배너 슬라이드 Entity 생성. */
    public static BannerSlideJpaEntity activeSlideEntity() {
        Instant now = Instant.now();
        return BannerSlideJpaEntity.create(
                DEFAULT_SLIDE_ID,
                DEFAULT_GROUP_ID,
                DEFAULT_SLIDE_TITLE,
                DEFAULT_IMAGE_URL,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                now,
                now,
                null);
    }

    /** ID를 지정한 활성 배너 슬라이드 Entity 생성. */
    public static BannerSlideJpaEntity activeSlideEntity(Long id, long bannerGroupId) {
        Instant now = Instant.now();
        return BannerSlideJpaEntity.create(
                id,
                bannerGroupId,
                DEFAULT_SLIDE_TITLE,
                DEFAULT_IMAGE_URL,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                now,
                now,
                null);
    }

    /** 비활성 배너 슬라이드 Entity 생성. */
    public static BannerSlideJpaEntity inactiveSlideEntity() {
        Instant now = Instant.now();
        return BannerSlideJpaEntity.create(
                2L,
                DEFAULT_GROUP_ID,
                DEFAULT_SLIDE_TITLE,
                DEFAULT_IMAGE_URL,
                DEFAULT_LINK_URL,
                2,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                null);
    }

    /** 삭제된 배너 슬라이드 Entity 생성. */
    public static BannerSlideJpaEntity deletedSlideEntity() {
        Instant now = Instant.now();
        return BannerSlideJpaEntity.create(
                3L,
                DEFAULT_GROUP_ID,
                DEFAULT_SLIDE_TITLE,
                DEFAULT_IMAGE_URL,
                DEFAULT_LINK_URL,
                3,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                now);
    }

    /** 새로 생성될 슬라이드 Entity (ID가 null). */
    public static BannerSlideJpaEntity newSlideEntity(long bannerGroupId) {
        Instant now = Instant.now();
        return BannerSlideJpaEntity.create(
                null,
                bannerGroupId,
                DEFAULT_SLIDE_TITLE,
                DEFAULT_IMAGE_URL,
                DEFAULT_LINK_URL,
                DEFAULT_DISPLAY_ORDER,
                DEFAULT_DISPLAY_START_AT,
                DEFAULT_DISPLAY_END_AT,
                true,
                now,
                now,
                null);
    }
}
