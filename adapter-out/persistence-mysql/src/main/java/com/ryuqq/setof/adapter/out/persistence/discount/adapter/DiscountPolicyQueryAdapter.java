package com.ryuqq.setof.adapter.out.persistence.discount.adapter;

import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity.DiscountGroupType;
import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity.DiscountTargetTypeEnum;
import com.ryuqq.setof.adapter.out.persistence.discount.mapper.DiscountPolicyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.discount.repository.DiscountPolicyQueryDslRepository;
import com.ryuqq.setof.application.discount.port.out.query.DiscountPolicyQueryPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * DiscountPolicyQueryAdapter - DiscountPolicy Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, DiscountPolicy 조회 요청을 QueryDslRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
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

    /** ID로 DiscountPolicy 단건 조회 */
    @Override
    public Optional<DiscountPolicy> findById(DiscountPolicyId id) {
        return queryDslRepository.findById(id.value()).map(mapper::toDomain);
    }

    /** 셀러 ID로 DiscountPolicy 목록 조회 */
    @Override
    public List<DiscountPolicy> findBySellerId(Long sellerId, boolean includeDeleted) {
        return queryDslRepository.findBySellerId(sellerId, includeDeleted).stream()
                .map(mapper::toDomain)
                .toList();
    }

    /** 셀러 ID와 그룹으로 DiscountPolicy 목록 조회 */
    @Override
    public List<DiscountPolicy> findBySellerIdAndGroup(
            Long sellerId, DiscountGroup discountGroup, boolean activeOnly) {
        DiscountGroupType entityGroup = DiscountGroupType.valueOf(discountGroup.name());
        return queryDslRepository.findBySellerIdAndGroup(sellerId, entityGroup, activeOnly).stream()
                .map(mapper::toDomain)
                .toList();
    }

    /** 적용 대상 타입과 ID로 DiscountPolicy 목록 조회 */
    @Override
    public List<DiscountPolicy> findByTargetTypeAndTargetId(
            DiscountTargetType targetType, Long targetId, boolean activeOnly) {
        DiscountTargetTypeEnum entityTargetType = DiscountTargetTypeEnum.valueOf(targetType.name());
        return queryDslRepository
                .findByTargetTypeAndTargetId(entityTargetType, targetId, activeOnly)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    /** 현재 유효한 할인 정책 목록 조회 */
    @Override
    public List<DiscountPolicy> findValidPolicies(Long sellerId) {
        return queryDslRepository.findValidPolicies(sellerId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    /** 셀러의 정책 개수 조회 */
    @Override
    public long countBySellerId(Long sellerId, boolean includeDeleted) {
        return queryDslRepository.countBySellerId(sellerId, includeDeleted);
    }

    /** ID로 존재 여부 확인 */
    @Override
    public boolean existsById(DiscountPolicyId id) {
        return queryDslRepository.existsById(id.value());
    }
}
