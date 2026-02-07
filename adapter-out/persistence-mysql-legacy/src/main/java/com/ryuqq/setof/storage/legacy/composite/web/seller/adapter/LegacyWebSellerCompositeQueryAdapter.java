package com.ryuqq.setof.storage.legacy.composite.web.seller.adapter;

import com.ryuqq.setof.application.legacy.seller.dto.response.LegacySellerResult;
import com.ryuqq.setof.domain.legacy.seller.dto.query.LegacySellerSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.seller.mapper.LegacyWebSellerMapper;
import com.ryuqq.setof.storage.legacy.composite.web.seller.repository.LegacyWebSellerCompositeQueryDslRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * LegacyWebSellerCompositeQueryAdapter - 레거시 Web 판매자 Composite 조회 Adapter.
 *
 * <p>Persistence Layer의 진입점입니다.
 *
 * <p>TODO: Application Layer의 LegacySellerCompositeQueryPort implements 추가.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebSellerCompositeQueryAdapter {

    private final LegacyWebSellerCompositeQueryDslRepository repository;
    private final LegacyWebSellerMapper mapper;

    public LegacyWebSellerCompositeQueryAdapter(
            LegacyWebSellerCompositeQueryDslRepository repository, LegacyWebSellerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 판매자 단건 조회 (ID).
     *
     * @param condition 검색 조건
     * @return 판매자 Optional
     */
    public Optional<LegacySellerResult> fetchSeller(LegacySellerSearchCondition condition) {
        Optional<com.ryuqq.setof.storage.legacy.composite.web.seller.dto.LegacyWebSellerQueryDto>
                dto = repository.fetchSeller(condition);
        return mapper.toResult(dto);
    }
}
