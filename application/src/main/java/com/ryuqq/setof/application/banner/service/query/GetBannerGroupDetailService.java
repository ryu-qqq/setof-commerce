package com.ryuqq.setof.application.banner.service.query;

import com.ryuqq.setof.application.banner.manager.BannerGroupReadManager;
import com.ryuqq.setof.application.banner.port.in.query.GetBannerGroupDetailUseCase;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import org.springframework.stereotype.Service;

/**
 * GetBannerGroupDetailService - 배너 그룹 상세 조회 Service.
 *
 * <p>ReadManager로 배너 그룹을 조회하여 반환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetBannerGroupDetailService implements GetBannerGroupDetailUseCase {

    private final BannerGroupReadManager readManager;

    public GetBannerGroupDetailService(BannerGroupReadManager readManager) {
        this.readManager = readManager;
    }

    @Override
    public BannerGroup execute(long bannerGroupId) {
        return readManager.getById(bannerGroupId);
    }
}
