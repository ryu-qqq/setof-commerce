package com.ryuqq.setof.application.banner.port.out.command;

import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import java.util.List;

/**
 * BannerGroupCommandPort - 배너 그룹 저장 Port-Out.
 *
 * <p>APP-PRT-001: Port-Out은 interface이며, Adapter-Out이 구현합니다. Application 레이어가 정의하고, Adapter-Out이
 * 의존성 역전(DIP)으로 구현합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface BannerGroupCommandPort {

    /**
     * 배너 그룹을 저장하고 생성된 ID를 반환합니다.
     *
     * @param bannerGroup 저장할 배너 그룹
     * @return 생성된 배너 그룹 ID
     */
    Long persist(BannerGroup bannerGroup);

    /**
     * 배너 그룹에 속한 슬라이드 목록을 저장합니다.
     *
     * @param bannerGroupId 배너 그룹 ID
     * @param slides 저장할 슬라이드 목록
     */
    void persistSlides(long bannerGroupId, List<BannerSlide> slides);

    /**
     * 기존 슬라이드의 상태를 갱신합니다 (retained + removed).
     *
     * @param bannerGroupId 배너 그룹 ID
     * @param slides 갱신할 슬라이드 목록
     */
    void updateSlides(long bannerGroupId, List<BannerSlide> slides);
}
