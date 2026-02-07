package com.ryuqq.setof.storage.legacy.composite.web.gnb.adapter;

import com.ryuqq.setof.application.legacy.web.gnb.dto.response.LegacyWebGnbResult;
import com.ryuqq.setof.storage.legacy.composite.web.gnb.dto.LegacyWebGnbQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.gnb.mapper.LegacyWebGnbMapper;
import com.ryuqq.setof.storage.legacy.composite.web.gnb.repository.LegacyWebGnbCompositeQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebGnbCompositeQueryAdapter - 레거시 Web GNB Composite 조회 Adapter.
 *
 * <p>TODO: Application Layer의 LegacyWebGnbCompositeQueryPort implements 추가
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebGnbCompositeQueryAdapter {

    private final LegacyWebGnbCompositeQueryDslRepository repository;
    private final LegacyWebGnbMapper mapper;

    public LegacyWebGnbCompositeQueryAdapter(
            LegacyWebGnbCompositeQueryDslRepository repository, LegacyWebGnbMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 전시 중인 GNB 목록 조회.
     *
     * @return LegacyWebGnbResult 목록
     */
    public List<LegacyWebGnbResult> fetchGnbs() {
        List<LegacyWebGnbQueryDto> dtos = repository.fetchGnbs();
        return mapper.toResults(dtos);
    }
}
