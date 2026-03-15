package com.ryuqq.setof.adapter.out.persistence.discountpolicy.adapter;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.mapper.DiscountPolicyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountPolicyQueryDslRepository;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountTargetQueryDslRepository;
import com.ryuqq.setof.application.discount.port.out.query.DiscountPolicyQueryPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.query.DiscountPolicySearchCriteria;
import com.ryuqq.setof.domain.discount.vo.ApplicationType;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.PublisherType;
import com.ryuqq.setof.domain.discount.vo.StackingGroup;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final DiscountTargetQueryDslRepository targetQueryDslRepository;
    private final DiscountPolicyJpaEntityMapper mapper;

    public DiscountPolicyQueryAdapter(
            DiscountPolicyQueryDslRepository queryDslRepository,
            DiscountTargetQueryDslRepository targetQueryDslRepository,
            DiscountPolicyJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.targetQueryDslRepository = targetQueryDslRepository;
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
                targetQueryDslRepository.findByPolicyIds(policyIds);

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

    @Override
    public Optional<DiscountPolicy> findById(long id) {
        return queryDslRepository
                .findById(id)
                .map(
                        entity -> {
                            List<DiscountTargetJpaEntity> targets =
                                    targetQueryDslRepository.findByPolicyId(id);
                            return mapper.toDomain(entity, targets);
                        });
    }

    @Override
    public List<DiscountPolicy> findByCriteria(DiscountPolicySearchCriteria criteria) {
        List<DiscountPolicyJpaEntity> entities =
                queryDslRepository.findByCriteria(
                        toEntityApplicationType(criteria.applicationType()),
                        toEntityPublisherType(criteria.publisherType()),
                        toEntityStackingGroup(criteria.stackingGroup()),
                        criteria.sellerIdValue(),
                        criteria.activeOnly(),
                        criteria.queryContext().sortKey(),
                        criteria.queryContext().isAscending(),
                        criteria.offset(),
                        criteria.size());

        if (entities.isEmpty()) {
            return List.of();
        }

        List<Long> policyIds = entities.stream().map(DiscountPolicyJpaEntity::getId).toList();
        List<DiscountTargetJpaEntity> allTargets =
                targetQueryDslRepository.findByPolicyIds(policyIds);

        Map<Long, List<DiscountTargetJpaEntity>> targetsByPolicyId =
                allTargets.stream()
                        .collect(
                                Collectors.groupingBy(
                                        DiscountTargetJpaEntity::getDiscountPolicyId));

        return entities.stream()
                .map(
                        policy ->
                                mapper.toDomain(
                                        policy,
                                        targetsByPolicyId.getOrDefault(policy.getId(), List.of())))
                .toList();
    }

    @Override
    public long countByCriteria(DiscountPolicySearchCriteria criteria) {
        return queryDslRepository.countByCriteria(
                toEntityApplicationType(criteria.applicationType()),
                toEntityPublisherType(criteria.publisherType()),
                toEntityStackingGroup(criteria.stackingGroup()),
                criteria.sellerIdValue(),
                criteria.activeOnly());
    }

    private DiscountTargetJpaEntity.TargetType toEntityTargetType(DiscountTargetType domain) {
        return DiscountTargetJpaEntity.TargetType.valueOf(domain.name());
    }

    private DiscountPolicyJpaEntity.ApplicationType toEntityApplicationType(
            ApplicationType domain) {
        if (domain == null) {
            return null;
        }
        return switch (domain) {
            case INSTANT -> DiscountPolicyJpaEntity.ApplicationType.IMMEDIATE;
            case COUPON -> DiscountPolicyJpaEntity.ApplicationType.COUPON;
        };
    }

    private DiscountPolicyJpaEntity.PublisherType toEntityPublisherType(PublisherType domain) {
        return domain != null ? DiscountPolicyJpaEntity.PublisherType.valueOf(domain.name()) : null;
    }

    private DiscountPolicyJpaEntity.StackingGroup toEntityStackingGroup(StackingGroup domain) {
        return domain != null ? DiscountPolicyJpaEntity.StackingGroup.valueOf(domain.name()) : null;
    }
}
