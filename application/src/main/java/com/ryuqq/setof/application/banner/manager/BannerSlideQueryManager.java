package com.ryuqq.setof.application.banner.manager;

import com.ryuqq.setof.application.banner.port.out.BannerSlideQueryPort;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * BannerSlideQueryManager - 배너 슬라이드 조회 매니저.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class BannerSlideQueryManager {

    private final BannerSlideQueryPort queryPort;

    public BannerSlideQueryManager(BannerSlideQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    /**
     * 배너 타입별 전시 중인 슬라이드 목록 조회.
     *
     * @param bannerType 배너 타입
     * @return BannerSlide 목록
     */
    @Transactional(readOnly = true)
    public List<BannerSlide> fetchDisplayBannerSlides(BannerType bannerType) {
        return queryPort.fetchDisplayBannerSlides(bannerType);
    }
}
