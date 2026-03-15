package com.ryuqq.setof.adapter.out.persistence.discountpolicy.adapter;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.mapper.DiscountPolicyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountTargetQueryDslRepository;
import com.ryuqq.setof.application.discount.port.out.query.DiscountTargetQueryPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * DiscountTargetQueryAdapter - 할인 적용 대상 Query 어댑터.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountTargetQueryAdapter implements DiscountTargetQueryPort {

    private final DiscountTargetQueryDslRepository queryDslRepository;
    private final DiscountPolicyJpaEntityMapper mapper;

    public DiscountTargetQueryAdapter(
            DiscountTargetQueryDslRepository queryDslRepository,
            DiscountPolicyJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public List<DiscountTarget> findByPolicyId(long discountPolicyId) {
        return queryDslRepository.findByPolicyId(discountPolicyId).stream()
                .map(mapper::toTargetDomain)
                .toList();
    }

    @Override
    public List<DiscountTarget> findByPolicyIds(List<Long> policyIds) {
        return queryDslRepository.findByPolicyIds(policyIds).stream()
                .map(mapper::toTargetDomain)
                .toList();
    }

    @Override
    public long countByPolicyId(long discountPolicyId) {
        return queryDslRepository.countByPolicyId(discountPolicyId);
    }
}
