package com.ryuqq.setof.adapter.out.persistence.discountpolicy.adapter;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.mapper.DiscountPolicyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountPolicyQueryDslRepository;
import com.ryuqq.setof.application.discount.port.out.query.DiscountPolicyQueryPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * DiscountPolicyQueryAdapter - 할인 정책 Query 어댑터.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountPolicyQueryAdapter implements DiscountPolicyQueryPort {

    private final DiscountPolicyQueryDslRepository queryDslRepository;
    private final DiscountPolicyJpaEntityMapper mapper;

    public DiscountPolicyQueryAdapter(
            DiscountPolicyQueryDslRepository queryDslRepository,
            DiscountPolicyJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public List<DiscountPolicy> findActiveByTarget(DiscountTargetType targetType, long targetId) {
        Instant now = Instant.now();

        List<Long> policyIds =
                queryDslRepository.findActivePolicyIdsByTarget(
                        toEntityTargetType(targetType), targetId, now);

        if (policyIds.isEmpty()) {
            return List.of();
        }

        List<DiscountPolicyJpaEntity> policyEntities = queryDslRepository.findAllByIds(policyIds);

        List<DiscountTargetJpaEntity> allTargets =
                queryDslRepository.findTargetsByPolicyIds(policyIds);

        Map<Long, List<DiscountTargetJpaEntity>> targetsByPolicyId =
                allTargets.stream()
                        .collect(
                                Collectors.groupingBy(
                                        DiscountTargetJpaEntity::getDiscountPolicyId));

        return policyEntities.stream()
                .map(
                        policy ->
                                mapper.toDomain(
                                        policy,
                                        targetsByPolicyId.getOrDefault(policy.getId(), List.of())))
                .toList();
    }

    private DiscountTargetJpaEntity.TargetType toEntityTargetType(DiscountTargetType domain) {
        return DiscountTargetJpaEntity.TargetType.valueOf(domain.name());
    }
}
