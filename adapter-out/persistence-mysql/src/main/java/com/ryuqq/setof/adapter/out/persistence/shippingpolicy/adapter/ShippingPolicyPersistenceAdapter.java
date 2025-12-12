package com.ryuqq.setof.adapter.out.persistence.shippingpolicy.adapter;

import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.entity.ShippingPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.mapper.ShippingPolicyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.repository.ShippingPolicyJpaRepository;
import com.ryuqq.setof.application.shippingpolicy.port.out.command.ShippingPolicyPersistencePort;
import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import org.springframework.stereotype.Component;

/**
 * ShippingPolicyPersistenceAdapter - ShippingPolicy Persistence Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, ShippingPolicy 저장 요청을 JpaRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShippingPolicyPersistenceAdapter implements ShippingPolicyPersistencePort {

    private final ShippingPolicyJpaRepository jpaRepository;
    private final ShippingPolicyJpaEntityMapper mapper;

    public ShippingPolicyPersistenceAdapter(
            ShippingPolicyJpaRepository jpaRepository, ShippingPolicyJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /** ShippingPolicy 저장 (생성/수정) */
    @Override
    public ShippingPolicyId persist(ShippingPolicy shippingPolicy) {
        ShippingPolicyJpaEntity entity = mapper.toEntity(shippingPolicy);
        ShippingPolicyJpaEntity savedEntity = jpaRepository.save(entity);
        return ShippingPolicyId.of(savedEntity.getId());
    }
}
