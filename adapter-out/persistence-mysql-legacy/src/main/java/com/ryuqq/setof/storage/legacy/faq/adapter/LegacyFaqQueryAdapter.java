package com.ryuqq.setof.storage.legacy.faq.adapter;

import com.ryuqq.setof.application.faq.port.out.query.FaqQueryPort;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import com.ryuqq.setof.storage.legacy.faq.entity.LegacyFaqEntity;
import com.ryuqq.setof.storage.legacy.faq.mapper.LegacyFaqEntityMapper;
import com.ryuqq.setof.storage.legacy.faq.repository.LegacyFaqQueryDslRepository;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * LegacyFaqQueryAdapter - 레거시 FAQ Query 어댑터.
 *
 * <p>FaqQueryPort를 구현하여 레거시 DB와 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>활성화 조건: persistence.legacy.faq.enabled=true
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.faq.enabled", havingValue = "true")
public class LegacyFaqQueryAdapter implements FaqQueryPort {

    private final LegacyFaqQueryDslRepository queryDslRepository;
    private final LegacyFaqEntityMapper mapper;

    public LegacyFaqQueryAdapter(
            LegacyFaqQueryDslRepository queryDslRepository, LegacyFaqEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Faq> findByCriteria(FaqSearchCriteria criteria) {
        List<LegacyFaqEntity> entities = queryDslRepository.findByCriteria(criteria);
        return entities.stream().map(mapper::toDomain).toList();
    }
}
