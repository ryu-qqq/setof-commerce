package com.ryuqq.setof.application.banner.service;

import com.ryuqq.setof.application.banner.manager.BannerSlideQueryManager;
import com.ryuqq.setof.application.banner.port.in.BannerSlideQueryUseCase;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * BannerSlideQueryService - 배너 슬라이드 조회 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class BannerSlideQueryService implements BannerSlideQueryUseCase {

    private final BannerSlideQueryManager queryManager;

    public BannerSlideQueryService(BannerSlideQueryManager queryManager) {
        this.queryManager = queryManager;
    }

    @Override
    public List<BannerSlide> fetchDisplayBannerSlides(BannerType bannerType) {
        return queryManager.fetchDisplayBannerSlides(bannerType);
    }
}
