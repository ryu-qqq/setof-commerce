package com.ryuqq.setof.application.banner.port.in.query;

import com.ryuqq.setof.application.banner.dto.query.BannerGroupPageResult;
import com.ryuqq.setof.application.banner.dto.query.BannerGroupSearchParams;

/**
 * SearchBannerGroupsUseCase - 배너 그룹 목록 검색 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface SearchBannerGroupsUseCase {

    /**
     * 검색 조건으로 배너 그룹 목록을 페이징 조회합니다.
     *
     * @param params 검색 파라미터
     * @return 배너 그룹 페이지 결과
     */
    BannerGroupPageResult execute(BannerGroupSearchParams params);
}
