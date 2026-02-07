package com.ryuqq.setof.storage.legacy.composite.web.faq.adapter;

import com.ryuqq.setof.application.legacy.faq.dto.response.LegacyFaqResult;
import com.ryuqq.setof.domain.legacy.faq.dto.query.LegacyFaqSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.faq.dto.LegacyWebFaqQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.faq.mapper.LegacyWebFaqMapper;
import com.ryuqq.setof.storage.legacy.composite.web.faq.repository.LegacyWebFaqCompositeQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebFaqCompositeQueryAdapter - 레거시 Web FAQ Composite 조회 Adapter.
 *
 * <p>Persistence Layer의 진입점입니다.
 *
 * <p>TODO: Application Layer의 LegacyFaqCompositeQueryPort implements 추가.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebFaqCompositeQueryAdapter {

    private final LegacyWebFaqCompositeQueryDslRepository repository;
    private final LegacyWebFaqMapper mapper;

    public LegacyWebFaqCompositeQueryAdapter(
            LegacyWebFaqCompositeQueryDslRepository repository, LegacyWebFaqMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * FAQ 목록 조회.
     *
     * @param condition 검색 조건
     * @return FAQ 목록
     */
    public List<LegacyFaqResult> fetchFaqs(LegacyFaqSearchCondition condition) {
        List<LegacyWebFaqQueryDto> dtos = repository.fetchFaqs(condition);
        return mapper.toResults(dtos);
    }
}
