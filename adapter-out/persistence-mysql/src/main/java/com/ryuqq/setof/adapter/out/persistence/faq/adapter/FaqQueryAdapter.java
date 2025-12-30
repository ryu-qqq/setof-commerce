package com.ryuqq.setof.adapter.out.persistence.faq.adapter;

import com.ryuqq.setof.adapter.out.persistence.faq.mapper.FaqJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.faq.repository.FaqQueryDslRepository;
import com.ryuqq.setof.application.faq.port.out.query.FaqQueryPort;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import com.ryuqq.setof.domain.faq.vo.FaqId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * FaqQueryAdapter - FAQ 조회 Adapter (Query)
 *
 * <p>FaqQueryPort를 구현하여 FAQ의 조회 작업을 처리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class FaqQueryAdapter implements FaqQueryPort {

    private final FaqQueryDslRepository queryDslRepository;
    private final FaqJpaEntityMapper mapper;

    public FaqQueryAdapter(FaqQueryDslRepository queryDslRepository, FaqJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Faq> findById(FaqId faqId) {
        return queryDslRepository.findById(faqId.value()).map(mapper::toDomain);
    }

    @Override
    public List<Faq> findByCriteria(FaqSearchCriteria criteria) {
        return queryDslRepository.findByCriteria(criteria).stream().map(mapper::toDomain).toList();
    }

    @Override
    public long countByCriteria(FaqSearchCriteria criteria) {
        return queryDslRepository.countByCriteria(criteria);
    }

    @Override
    public boolean existsById(FaqId faqId) {
        return queryDslRepository.existsById(faqId.value());
    }
}
