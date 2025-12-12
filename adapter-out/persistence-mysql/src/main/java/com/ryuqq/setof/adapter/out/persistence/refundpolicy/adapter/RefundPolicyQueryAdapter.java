package com.ryuqq.setof.adapter.out.persistence.refundpolicy.adapter;

import com.ryuqq.setof.adapter.out.persistence.refundpolicy.mapper.RefundPolicyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.refundpolicy.repository.RefundPolicyQueryDslRepository;
import com.ryuqq.setof.application.refundpolicy.port.out.query.RefundPolicyQueryPort;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * RefundPolicyQueryAdapter - RefundPolicy Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, RefundPolicy 조회 요청을 QueryDslRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefundPolicyQueryAdapter implements RefundPolicyQueryPort {

    private final RefundPolicyQueryDslRepository queryDslRepository;
    private final RefundPolicyJpaEntityMapper mapper;

    public RefundPolicyQueryAdapter(
            RefundPolicyQueryDslRepository queryDslRepository, RefundPolicyJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /** ID로 RefundPolicy 단건 조회 */
    @Override
    public Optional<RefundPolicy> findById(RefundPolicyId id) {
        return queryDslRepository.findById(id.value()).map(mapper::toDomain);
    }

    /** 셀러 ID로 RefundPolicy 목록 조회 */
    @Override
    public List<RefundPolicy> findBySellerId(Long sellerId, boolean includeDeleted) {
        return queryDslRepository.findBySellerId(sellerId, includeDeleted).stream()
                .map(mapper::toDomain)
                .toList();
    }

    /** 셀러의 기본 정책 조회 */
    @Override
    public Optional<RefundPolicy> findDefaultBySellerId(Long sellerId) {
        return queryDslRepository.findDefaultBySellerId(sellerId).map(mapper::toDomain);
    }

    /** 셀러의 정책 개수 조회 */
    @Override
    public long countBySellerId(Long sellerId, boolean includeDeleted) {
        return queryDslRepository.countBySellerId(sellerId, includeDeleted);
    }

    /** ID로 존재 여부 확인 */
    @Override
    public boolean existsById(RefundPolicyId id) {
        return queryDslRepository.existsById(id.value());
    }
}
