package com.ryuqq.setof.adapter.out.persistence.faq.adapter;

import com.ryuqq.setof.adapter.out.persistence.faq.entity.FaqJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.faq.mapper.FaqJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.faq.repository.FaqQueryDslRepository;
import com.ryuqq.setof.application.faq.port.out.FaqQueryPort;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * FaqQueryAdapter - FAQ Query 어댑터.
 *
 * <p>FaqQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * <p>활성화 조건: persistence.legacy.faq.enabled=false
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.faq.enabled", havingValue = "false")
public class FaqQueryAdapter implements FaqQueryPort {

    private final FaqQueryDslRepository queryDslRepository;
    private final FaqJpaEntityMapper mapper;

    public FaqQueryAdapter(FaqQueryDslRepository queryDslRepository, FaqJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Faq> findByCriteria(FaqSearchCriteria criteria) {
        List<FaqJpaEntity> entities = queryDslRepository.findByCriteria(criteria);
        return entities.stream().map(mapper::toDomain).toList();
    }
}
