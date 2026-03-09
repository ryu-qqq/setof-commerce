package com.ryuqq.setof.adapter.out.persistence.discountpolicy.adapter;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountTargetJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.mapper.DiscountPolicyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountTargetJpaRepository;
import com.ryuqq.setof.application.discount.port.out.command.DiscountTargetCommandPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * DiscountTargetCommandAdapter - 할인 적용 대상 Command 어댑터.
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-003: Domain -> Entity 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountTargetCommandAdapter implements DiscountTargetCommandPort {

    private final DiscountTargetJpaRepository jpaRepository;
    private final DiscountPolicyJpaEntityMapper mapper;

    public DiscountTargetCommandAdapter(
            DiscountTargetJpaRepository jpaRepository, DiscountPolicyJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void persistAll(long discountPolicyId, List<DiscountTarget> targets) {
        if (targets.isEmpty()) {
            return;
        }
        Instant now = Instant.now();
        List<DiscountTargetJpaEntity> entities =
                targets.stream()
                        .map(target -> mapper.toTargetEntity(target, discountPolicyId, now))
                        .toList();
        jpaRepository.saveAll(entities);
    }
}
