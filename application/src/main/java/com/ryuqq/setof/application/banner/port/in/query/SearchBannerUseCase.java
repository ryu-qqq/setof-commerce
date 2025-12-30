package com.ryuqq.setof.application.banner.port.in.query;

import com.ryuqq.setof.application.banner.dto.query.SearchBannerQuery;
import com.ryuqq.setof.application.banner.dto.response.BannerResponse;
import java.util.List;

/**
 * Banner 검색 UseCase (Query)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface SearchBannerUseCase {

    /**
     * 배너 검색
     *
     * @param query 검색 조건
     * @return 배너 목록
     */
    List<BannerResponse> execute(SearchBannerQuery query);
}
