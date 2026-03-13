package com.ryuqq.setof.application.banner;

import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.setof.commerce.domain.banner.BannerFixtures;
import java.util.List;

/**
 * Banner Application Query 테스트 Fixtures.
 *
 * <p>BannerSlideQueryService, BannerSlideQueryManager 테스트에서 공통으로 사용하는 객체 생성 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class BannerQueryFixtures {

    private BannerQueryFixtures() {}

    // ===== BannerType =====

    public static BannerType defaultBannerType() {
        return BannerType.RECOMMEND;
    }

    // ===== BannerSlide List =====

    public static List<BannerSlide> activeBannerSlides() {
        return List.of(BannerFixtures.activeBannerSlide(1L), BannerFixtures.activeBannerSlide(2L));
    }

    public static List<BannerSlide> emptyBannerSlides() {
        return List.of();
    }
}
