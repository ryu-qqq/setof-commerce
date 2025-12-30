package com.ryuqq.setof.application.banner.port.in.query;

import com.ryuqq.setof.application.banner.dto.response.BannerResponse;
import com.ryuqq.setof.domain.cms.vo.BannerId;

/**
 * Banner 단건 조회 UseCase (Query)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetBannerUseCase {

    /**
     * 배너 단건 조회
     *
     * @param bannerId 배너 ID
     * @return 배너 응답
     */
    BannerResponse execute(BannerId bannerId);
}
