package com.ryuqq.setof.application.banner.service.query;

import com.ryuqq.setof.application.banner.dto.query.BannerGroupPageResult;
import com.ryuqq.setof.application.banner.dto.query.BannerGroupSearchParams;
import com.ryuqq.setof.application.banner.factory.BannerGroupQueryFactory;
import com.ryuqq.setof.application.banner.manager.BannerGroupReadManager;
import com.ryuqq.setof.application.banner.port.in.query.SearchBannerGroupsUseCase;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.query.BannerGroupSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * SearchBannerGroupsService - 배너 그룹 목록 검색 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class SearchBannerGroupsService implements SearchBannerGroupsUseCase {

    private final BannerGroupReadManager readManager;
    private final BannerGroupQueryFactory queryFactory;

    public SearchBannerGroupsService(
            BannerGroupReadManager readManager, BannerGroupQueryFactory queryFactory) {
        this.readManager = readManager;
        this.queryFactory = queryFactory;
    }

    @Override
    public BannerGroupPageResult execute(BannerGroupSearchParams params) {
        BannerGroupSearchCriteria criteria = queryFactory.create(params);
        List<BannerGroup> groups = readManager.findByCriteria(criteria);
        long totalCount = readManager.countByCriteria(criteria);

        Long lastDomainId = groups.isEmpty() ? null : groups.get(groups.size() - 1).idValue();

        return BannerGroupPageResult.of(
                groups, totalCount, criteria.page(), criteria.size(), lastDomainId);
    }
}
