package com.ryuqq.setof.application.banner.factory;

import com.ryuqq.setof.application.banner.dto.query.BannerGroupSearchParams;
import com.ryuqq.setof.domain.banner.query.BannerGroupSearchCriteria;
import com.ryuqq.setof.domain.banner.query.BannerGroupSortKey;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import org.springframework.stereotype.Component;

/**
 * BannerGroupQueryFactory - 배너 그룹 검색 조건 변환 Factory.
 *
 * <p>{@link BannerGroupSearchParams}를 도메인 레이어의 {@link BannerGroupSearchCriteria}로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class BannerGroupQueryFactory {

    /**
     * SearchParams → SearchCriteria 변환.
     *
     * @param params 외부 검색 파라미터
     * @return 도메인 검색 조건
     */
    public BannerGroupSearchCriteria create(BannerGroupSearchParams params) {
        BannerType bannerType = parseBannerType(params.bannerType());
        PageRequest pageRequest = PageRequest.of(params.page(), params.size());
        QueryContext<BannerGroupSortKey> queryContext =
                QueryContext.of(BannerGroupSortKey.defaultKey(), SortDirection.DESC, pageRequest);

        return BannerGroupSearchCriteria.of(
                bannerType,
                params.active(),
                params.displayPeriodStart(),
                params.displayPeriodEnd(),
                params.titleKeyword(),
                params.lastDomainId(),
                queryContext);
    }

    private BannerType parseBannerType(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return BannerType.valueOf(value.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
