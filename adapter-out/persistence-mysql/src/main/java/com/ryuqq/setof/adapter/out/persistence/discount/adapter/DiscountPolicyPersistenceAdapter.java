package com.ryuqq.setof.adapter.out.persistence.discount.adapter;

import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discount.mapper.DiscountPolicyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.discount.repository.DiscountPolicyJpaRepository;
import com.ryuqq.setof.application.discount.port.out.command.DiscountPolicyPersistencePort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import org.springframework.stereotype.Component;

/**
 * DiscountPolicyPersistenceAdapter - DiscountPolicy Persistence Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, DiscountPolicy 저장 요청을 JpaRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class DiscountPolicyPersistenceAdapter implements DiscountPolicyPersistencePort {

    private final DiscountPolicyJpaRepository jpaRepository;
    private final DiscountPolicyJpaEntityMapper mapper;

    public DiscountPolicyPersistenceAdapter(
            DiscountPolicyJpaRepository jpaRepository, DiscountPolicyJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * DiscountPolicy 저장 (신규 생성 또는 수정)
     *
     * <p>Domain의 ID 유무와 관계없이 JPA의 save()를 통해 처리합니다.
     */
    @Override
    public DiscountPolicyId persist(DiscountPolicy discountPolicy) {
        DiscountPolicyJpaEntity entity = mapper.toEntity(discountPolicy);
        DiscountPolicyJpaEntity savedEntity = jpaRepository.save(entity);
        return DiscountPolicyId.of(savedEntity.getId());
    }
}
