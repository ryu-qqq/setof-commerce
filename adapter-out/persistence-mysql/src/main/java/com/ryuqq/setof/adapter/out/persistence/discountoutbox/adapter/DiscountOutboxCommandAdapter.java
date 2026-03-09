package com.ryuqq.setof.adapter.out.persistence.discountoutbox.adapter;

import com.ryuqq.setof.adapter.out.persistence.discountoutbox.entity.DiscountOutboxJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountoutbox.mapper.DiscountOutboxJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.discountoutbox.repository.DiscountOutboxJpaRepository;
import com.ryuqq.setof.application.discount.port.out.command.DiscountOutboxCommandPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import org.springframework.stereotype.Component;

/**
 * DiscountOutboxCommandAdapter - 할인 아웃박스 Command 어댑터.
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountOutboxCommandAdapter implements DiscountOutboxCommandPort {

    private final DiscountOutboxJpaRepository jpaRepository;
    private final DiscountOutboxJpaEntityMapper mapper;

    public DiscountOutboxCommandAdapter(
            DiscountOutboxJpaRepository jpaRepository, DiscountOutboxJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public long persist(DiscountOutbox discountOutbox) {
        DiscountOutboxJpaEntity entity = mapper.toEntity(discountOutbox);
        DiscountOutboxJpaEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }
}
