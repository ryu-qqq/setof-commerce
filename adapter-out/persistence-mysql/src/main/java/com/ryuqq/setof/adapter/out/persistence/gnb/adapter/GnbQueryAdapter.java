package com.ryuqq.setof.adapter.out.persistence.gnb.adapter;

import com.ryuqq.setof.adapter.out.persistence.gnb.mapper.GnbJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.gnb.repository.GnbQueryDslRepository;
import com.ryuqq.setof.application.gnb.port.out.query.GnbQueryPort;
import com.ryuqq.setof.domain.cms.aggregate.gnb.Gnb;
import com.ryuqq.setof.domain.cms.vo.GnbId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * GnbQueryAdapter - GNB 조회 Adapter (Query)
 *
 * <p>GnbQueryPort를 구현하여 GNB의 조회 작업을 처리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class GnbQueryAdapter implements GnbQueryPort {

    private final GnbQueryDslRepository queryDslRepository;
    private final GnbJpaEntityMapper mapper;

    public GnbQueryAdapter(GnbQueryDslRepository queryDslRepository, GnbJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Gnb> findById(GnbId gnbId) {
        return queryDslRepository.findById(gnbId.value()).map(mapper::toDomain);
    }

    @Override
    public List<Gnb> findAllActive() {
        return queryDslRepository.findAllActive().stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsById(GnbId gnbId) {
        return queryDslRepository.existsById(gnbId.value());
    }
}
