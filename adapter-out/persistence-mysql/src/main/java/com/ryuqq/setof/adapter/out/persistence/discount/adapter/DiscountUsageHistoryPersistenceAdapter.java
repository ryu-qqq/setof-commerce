package com.ryuqq.setof.adapter.out.persistence.discount.adapter;

import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountUsageHistoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discount.mapper.DiscountUsageHistoryJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.discount.repository.DiscountUsageHistoryJpaRepository;
import com.ryuqq.setof.application.discountusagehistory.port.out.command.DiscountUsageHistoryPersistencePort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountUsageHistory;
import com.ryuqq.setof.domain.discount.vo.DiscountUsageHistoryId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * DiscountUsageHistoryPersistenceAdapter - 할인 사용 이력 Persistence Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, DiscountUsageHistory 저장 요청을 JpaRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class DiscountUsageHistoryPersistenceAdapter implements DiscountUsageHistoryPersistencePort {

    private final DiscountUsageHistoryJpaRepository jpaRepository;
    private final DiscountUsageHistoryJpaEntityMapper mapper;

    public DiscountUsageHistoryPersistenceAdapter(
            DiscountUsageHistoryJpaRepository jpaRepository,
            DiscountUsageHistoryJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /** 할인 사용 이력 저장 (단건) */
    @Override
    public DiscountUsageHistoryId persist(DiscountUsageHistory history) {
        DiscountUsageHistoryJpaEntity entity = mapper.toEntity(history);
        DiscountUsageHistoryJpaEntity savedEntity = jpaRepository.save(entity);
        return DiscountUsageHistoryId.of(savedEntity.getId());
    }

    /** 할인 사용 이력 저장 (일괄) */
    @Override
    public List<DiscountUsageHistoryId> persistAll(List<DiscountUsageHistory> histories) {
        List<DiscountUsageHistoryJpaEntity> entities =
                histories.stream().map(mapper::toEntity).toList();
        List<DiscountUsageHistoryJpaEntity> savedEntities = jpaRepository.saveAll(entities);
        return savedEntities.stream()
                .map(entity -> DiscountUsageHistoryId.of(entity.getId()))
                .toList();
    }
}
