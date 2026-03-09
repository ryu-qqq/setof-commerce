package com.ryuqq.setof.adapter.out.persistence.discountpolicy.adapter;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.mapper.DiscountPolicyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountPolicyJpaRepository;
import com.ryuqq.setof.application.discount.port.out.command.DiscountPolicyCommandPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import org.springframework.stereotype.Component;

/**
 * DiscountPolicyCommandAdapter - 할인 정책 Command 어댑터.
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-003: Domain -> Entity 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountPolicyCommandAdapter implements DiscountPolicyCommandPort {

    private final DiscountPolicyJpaRepository jpaRepository;
    private final DiscountPolicyJpaEntityMapper mapper;

    public DiscountPolicyCommandAdapter(
            DiscountPolicyJpaRepository jpaRepository, DiscountPolicyJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public long persist(DiscountPolicy discountPolicy) {
        DiscountPolicyJpaEntity entity = mapper.toEntity(discountPolicy);
        DiscountPolicyJpaEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }
}
