package com.ryuqq.setof.application.banner.port.out;

import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import java.util.List;

/**
 * BannerSlideQueryPort - 배너 슬라이드 조회 Port.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface BannerSlideQueryPort {

    /**
     * 배너 타입별 전시 중인 슬라이드 목록 조회.
     *
     * @param bannerType 배너 타입
     * @return BannerSlide 목록
     */
    List<BannerSlide> fetchDisplayBannerSlides(BannerType bannerType);
}
