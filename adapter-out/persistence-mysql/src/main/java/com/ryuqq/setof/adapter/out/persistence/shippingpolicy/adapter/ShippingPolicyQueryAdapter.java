package com.ryuqq.setof.adapter.out.persistence.shippingpolicy.adapter;

import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.mapper.ShippingPolicyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.repository.ShippingPolicyQueryDslRepository;
import com.ryuqq.setof.application.shippingpolicy.port.out.query.ShippingPolicyQueryPort;
import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * ShippingPolicyQueryAdapter - ShippingPolicy Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, ShippingPolicy 조회 요청을 QueryDslRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShippingPolicyQueryAdapter implements ShippingPolicyQueryPort {

    private final ShippingPolicyQueryDslRepository queryDslRepository;
    private final ShippingPolicyJpaEntityMapper mapper;

    public ShippingPolicyQueryAdapter(
            ShippingPolicyQueryDslRepository queryDslRepository,
            ShippingPolicyJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /** ID로 ShippingPolicy 단건 조회 */
    @Override
    public Optional<ShippingPolicy> findById(ShippingPolicyId id) {
        return queryDslRepository.findById(id.value()).map(mapper::toDomain);
    }

    /** 셀러 ID로 ShippingPolicy 목록 조회 */
    @Override
    public List<ShippingPolicy> findBySellerId(Long sellerId, boolean includeDeleted) {
        return queryDslRepository.findBySellerId(sellerId, includeDeleted).stream()
                .map(mapper::toDomain)
                .toList();
    }

    /** 셀러의 기본 정책 조회 */
    @Override
    public Optional<ShippingPolicy> findDefaultBySellerId(Long sellerId) {
        return queryDslRepository.findDefaultBySellerId(sellerId).map(mapper::toDomain);
    }

    /** 셀러의 정책 개수 조회 */
    @Override
    public long countBySellerId(Long sellerId, boolean includeDeleted) {
        return queryDslRepository.countBySellerId(sellerId, includeDeleted);
    }

    /** ID로 존재 여부 확인 */
    @Override
    public boolean existsById(ShippingPolicyId id) {
        return queryDslRepository.existsById(id.value());
    }
}
