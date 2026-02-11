package com.ryuqq.setof.storage.legacy.composite.web.banner.adapter;

import com.ryuqq.setof.application.legacy.banner.dto.response.LegacyBannerItemResult;
import com.ryuqq.setof.domain.legacy.banner.dto.query.LegacyBannerSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.banner.dto.LegacyWebBannerItemQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.banner.mapper.LegacyWebBannerMapper;
import com.ryuqq.setof.storage.legacy.composite.web.banner.repository.LegacyWebBannerCompositeQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebBannerCompositeQueryAdapter - 레거시 Web 배너 Composite 조회 Adapter.
 *
 * <p>TODO: Application Layer의 LegacyBannerCompositeQueryPort implements 추가
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebBannerCompositeQueryAdapter {

    private final LegacyWebBannerCompositeQueryDslRepository repository;
    private final LegacyWebBannerMapper mapper;

    public LegacyWebBannerCompositeQueryAdapter(
            LegacyWebBannerCompositeQueryDslRepository repository, LegacyWebBannerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 배너 타입별 배너 아이템 조회.
     *
     * @param condition 검색 조건
     * @return LegacyBannerItemResult 목록
     */
    public List<LegacyBannerItemResult> fetchBannerItems(LegacyBannerSearchCondition condition) {
        List<LegacyWebBannerItemQueryDto> dtos = repository.fetchBannerItems(condition);
        return mapper.toResults(dtos);
    }
}
