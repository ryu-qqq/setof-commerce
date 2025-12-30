package com.ryuqq.setof.application.banner.factory.query;

import com.ryuqq.setof.application.banner.dto.query.SearchBannerQuery;
import com.ryuqq.setof.domain.cms.query.criteria.BannerSearchCriteria;
import com.ryuqq.setof.domain.cms.vo.BannerStatus;
import com.ryuqq.setof.domain.cms.vo.BannerType;
import org.springframework.stereotype.Component;

/**
 * Banner Query Factory
 *
 * <p>Query DTO를 Domain 검색 조건으로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BannerQueryFactory {

    /**
     * SearchBannerQuery → BannerSearchCriteria 변환
     *
     * @param query 검색 쿼리
     * @return 검색 조건
     */
    public BannerSearchCriteria createSearchCriteria(SearchBannerQuery query) {
        BannerType bannerType =
                query.hasBannerType() ? BannerType.valueOf(query.bannerType()) : null;

        BannerStatus status = query.hasStatus() ? BannerStatus.valueOf(query.status()) : null;

        return BannerSearchCriteria.of(
                bannerType, status, query.displayableAt(), query.offset(), query.limit());
    }
}
