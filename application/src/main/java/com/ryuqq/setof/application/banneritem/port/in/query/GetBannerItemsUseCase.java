package com.ryuqq.setof.application.banneritem.port.in.query;

import com.ryuqq.setof.application.banneritem.dto.response.BannerItemResponse;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import java.util.List;

/**
 * BannerItem 목록 조회 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetBannerItemsUseCase {

    /**
     * Banner ID로 활성 아이템 목록 조회
     *
     * @param bannerId Banner ID
     * @return BannerItem 응답 목록
     */
    List<BannerItemResponse> getActiveByBannerId(BannerId bannerId);

    /**
     * 여러 Banner ID로 활성 아이템 목록 조회
     *
     * @param bannerIds Banner ID 목록
     * @return BannerItem 응답 목록
     */
    List<BannerItemResponse> getActiveByBannerIds(List<BannerId> bannerIds);
}
