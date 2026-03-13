package com.setof.commerce.domain.banner;

import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroupUpdateData;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.id.BannerGroupId;
import com.ryuqq.setof.domain.banner.id.BannerSlideId;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import java.time.Instant;
import java.util.List;

/**
 * Banner 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 Banner 관련 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class BannerFixtures {

    private BannerFixtures() {}

    // ===== 상수 =====
    public static final String DEFAULT_TITLE = "테스트 배너 그룹";
    public static final BannerType DEFAULT_BANNER_TYPE = BannerType.RECOMMEND;
    public static final String DEFAULT_SLIDE_TITLE = "테스트 슬라이드";
    public static final String DEFAULT_IMAGE_URL = "https://example.com/banner.png";
    public static final String DEFAULT_LINK_URL = "https://example.com/link";

    // ===== BannerGroupId Fixtures =====
    public static BannerGroupId defaultBannerGroupId() {
        return BannerGroupId.of(1L);
    }

    public static BannerGroupId bannerGroupId(Long value) {
        return BannerGroupId.of(value);
    }

    public static BannerGroupId newBannerGroupId() {
        return BannerGroupId.forNew();
    }

    // ===== BannerSlideId Fixtures =====
    public static BannerSlideId defaultBannerSlideId() {
        return BannerSlideId.of(1L);
    }

    public static BannerSlideId bannerSlideId(Long value) {
        return BannerSlideId.of(value);
    }

    public static BannerSlideId newBannerSlideId() {
        return BannerSlideId.forNew();
    }

    // ===== DisplayPeriod Fixtures =====
    public static DisplayPeriod defaultDisplayPeriod() {
        Instant now = CommonVoFixtures.now();
        return DisplayPeriod.of(now.minusSeconds(3600), now.plusSeconds(86400));
    }

    public static DisplayPeriod expiredDisplayPeriod() {
        Instant yesterday = CommonVoFixtures.yesterday();
        return DisplayPeriod.of(yesterday.minusSeconds(86400), yesterday);
    }

    public static DisplayPeriod futureDisplayPeriod() {
        Instant tomorrow = CommonVoFixtures.tomorrow();
        return DisplayPeriod.of(tomorrow, tomorrow.plusSeconds(86400));
    }

    // ===== BannerSlide Entity Fixtures =====
    public static BannerSlide newBannerSlide() {
        return BannerSlide.forNew(
                DEFAULT_SLIDE_TITLE,
                DEFAULT_IMAGE_URL,
                DEFAULT_LINK_URL,
                1,
                defaultDisplayPeriod(),
                true,
                CommonVoFixtures.now());
    }

    public static BannerSlide newBannerSlide(String title, int displayOrder) {
        return BannerSlide.forNew(
                title,
                DEFAULT_IMAGE_URL,
                DEFAULT_LINK_URL,
                displayOrder,
                defaultDisplayPeriod(),
                true,
                CommonVoFixtures.now());
    }

    public static BannerSlide activeBannerSlide() {
        return BannerSlide.reconstitute(
                BannerSlideId.of(1L),
                DEFAULT_SLIDE_TITLE,
                DEFAULT_IMAGE_URL,
                DEFAULT_LINK_URL,
                1,
                defaultDisplayPeriod(),
                true,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static BannerSlide activeBannerSlide(Long id) {
        return BannerSlide.reconstitute(
                BannerSlideId.of(id),
                DEFAULT_SLIDE_TITLE,
                DEFAULT_IMAGE_URL,
                DEFAULT_LINK_URL,
                1,
                defaultDisplayPeriod(),
                true,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static BannerSlide inactiveBannerSlide() {
        return BannerSlide.reconstitute(
                BannerSlideId.of(2L),
                DEFAULT_SLIDE_TITLE,
                DEFAULT_IMAGE_URL,
                DEFAULT_LINK_URL,
                2,
                defaultDisplayPeriod(),
                false,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static BannerSlide deletedBannerSlide() {
        Instant deletedAt = CommonVoFixtures.yesterday();
        return BannerSlide.reconstitute(
                BannerSlideId.of(3L),
                DEFAULT_SLIDE_TITLE,
                DEFAULT_IMAGE_URL,
                DEFAULT_LINK_URL,
                3,
                defaultDisplayPeriod(),
                false,
                DeletionStatus.deletedAt(deletedAt),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static List<BannerSlide> defaultSlides() {
        return List.of(activeBannerSlide());
    }

    public static List<BannerSlide> emptySlides() {
        return List.of();
    }

    // ===== BannerGroup Aggregate Fixtures =====
    public static BannerGroup newBannerGroup() {
        return BannerGroup.forNew(
                DEFAULT_TITLE,
                DEFAULT_BANNER_TYPE,
                defaultDisplayPeriod(),
                true,
                defaultSlides(),
                CommonVoFixtures.now());
    }

    public static BannerGroup newBannerGroup(String title, BannerType bannerType) {
        return BannerGroup.forNew(
                title,
                bannerType,
                defaultDisplayPeriod(),
                true,
                defaultSlides(),
                CommonVoFixtures.now());
    }

    public static BannerGroup activeBannerGroup() {
        return BannerGroup.reconstitute(
                BannerGroupId.of(1L),
                DEFAULT_TITLE,
                DEFAULT_BANNER_TYPE,
                defaultDisplayPeriod(),
                true,
                DeletionStatus.active(),
                defaultSlides(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static BannerGroup activeBannerGroup(Long id) {
        return BannerGroup.reconstitute(
                BannerGroupId.of(id),
                DEFAULT_TITLE,
                DEFAULT_BANNER_TYPE,
                defaultDisplayPeriod(),
                true,
                DeletionStatus.active(),
                defaultSlides(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static BannerGroup inactiveBannerGroup() {
        return BannerGroup.reconstitute(
                BannerGroupId.of(2L),
                DEFAULT_TITLE,
                DEFAULT_BANNER_TYPE,
                defaultDisplayPeriod(),
                false,
                DeletionStatus.active(),
                defaultSlides(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static BannerGroup deletedBannerGroup() {
        Instant deletedAt = CommonVoFixtures.yesterday();
        return BannerGroup.reconstitute(
                BannerGroupId.of(3L),
                DEFAULT_TITLE,
                DEFAULT_BANNER_TYPE,
                defaultDisplayPeriod(),
                false,
                DeletionStatus.deletedAt(deletedAt),
                defaultSlides(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static BannerGroup expiredBannerGroup() {
        return BannerGroup.reconstitute(
                BannerGroupId.of(4L),
                DEFAULT_TITLE,
                DEFAULT_BANNER_TYPE,
                expiredDisplayPeriod(),
                true,
                DeletionStatus.active(),
                defaultSlides(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== BannerGroupUpdateData Fixtures =====
    public static BannerGroupUpdateData defaultBannerGroupUpdateData() {
        return new BannerGroupUpdateData(
                "수정된 배너 그룹",
                BannerType.CATEGORY,
                defaultDisplayPeriod(),
                false,
                defaultSlides(),
                CommonVoFixtures.now());
    }

    public static BannerGroupUpdateData bannerGroupUpdateData(
            String title, BannerType bannerType, boolean active) {
        return new BannerGroupUpdateData(
                title,
                bannerType,
                defaultDisplayPeriod(),
                active,
                defaultSlides(),
                CommonVoFixtures.now());
    }
}
