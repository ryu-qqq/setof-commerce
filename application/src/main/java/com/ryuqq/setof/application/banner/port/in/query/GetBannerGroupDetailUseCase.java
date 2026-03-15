package com.ryuqq.setof.application.banner.port.in.query;

import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;

/**
 * GetBannerGroupDetailUseCase - 배너 그룹 상세 조회 UseCase.
 *
 * <p>배너 그룹 ID로 그룹 정보와 슬라이드 목록을 함께 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetBannerGroupDetailUseCase {

    /**
     * 배너 그룹 상세를 조회합니다.
     *
     * @param bannerGroupId 배너 그룹 ID
     * @return 배너 그룹 도메인 객체 (슬라이드 포함)
     */
    BannerGroup execute(long bannerGroupId);
}
