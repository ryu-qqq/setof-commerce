package com.ryuqq.setof.adapter.out.persistence.discountoutbox.adapter;

import com.ryuqq.setof.adapter.out.persistence.discountoutbox.entity.DiscountOutboxJpaEntity.Status;
import com.ryuqq.setof.adapter.out.persistence.discountoutbox.mapper.DiscountOutboxJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.discountoutbox.repository.DiscountOutboxQueryDslRepository;
import com.ryuqq.setof.application.discount.port.out.query.DiscountOutboxQueryPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.OutboxStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * DiscountOutboxQueryAdapter - 할인 아웃박스 Query 어댑터.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountOutboxQueryAdapter implements DiscountOutboxQueryPort {

    private final DiscountOutboxQueryDslRepository queryDslRepository;
    private final DiscountOutboxJpaEntityMapper mapper;

    public DiscountOutboxQueryAdapter(
            DiscountOutboxQueryDslRepository queryDslRepository,
            DiscountOutboxJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<DiscountOutbox> findById(long id) {
        return queryDslRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<DiscountOutbox> findByStatus(OutboxStatus status, int limit) {
        return queryDslRepository.findByStatus(toEntityStatus(status), limit).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<DiscountOutbox> findStuckPublished(Instant timeoutBefore, int limit) {
        return queryDslRepository.findStuckPublished(timeoutBefore, limit).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByTargetAndStatus(
            DiscountTargetType targetType, long targetId, OutboxStatus status) {
        return queryDslRepository.existsByTargetAndStatus(
                targetType.name(), targetId, toEntityStatus(status));
    }

    private Status toEntityStatus(OutboxStatus domainStatus) {
        return Status.valueOf(domainStatus.name());
    }
}
