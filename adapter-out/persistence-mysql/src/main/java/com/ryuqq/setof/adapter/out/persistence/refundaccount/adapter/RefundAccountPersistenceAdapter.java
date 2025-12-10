package com.ryuqq.setof.adapter.out.persistence.refundaccount.adapter;

import com.ryuqq.setof.adapter.out.persistence.refundaccount.entity.RefundAccountJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.refundaccount.mapper.RefundAccountJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.refundaccount.repository.RefundAccountJpaRepository;
import com.ryuqq.setof.application.refundaccount.port.out.command.RefundAccountPersistencePort;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.vo.RefundAccountId;
import org.springframework.stereotype.Component;

/**
 * RefundAccountPersistenceAdapter - RefundAccount Persistence Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, RefundAccount Domain을 영속화하는 Adapter입니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>Domain -> Entity 변환
 *   <li>JpaRepository.save() 호출
 *   <li>저장된 ID 반환
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>조회 로직 금지 (QueryAdapter로 분리)
 *   <li>@Transactional 금지 (Application Layer에서 처리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefundAccountPersistenceAdapter implements RefundAccountPersistencePort {

    private final RefundAccountJpaRepository jpaRepository;
    private final RefundAccountJpaEntityMapper mapper;

    public RefundAccountPersistenceAdapter(
            RefundAccountJpaRepository jpaRepository, RefundAccountJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * RefundAccount 저장 (신규 생성 또는 수정)
     *
     * @param refundAccount 저장할 RefundAccount (Domain Aggregate)
     * @return 저장된 RefundAccount의 ID
     */
    @Override
    public RefundAccountId persist(RefundAccount refundAccount) {
        RefundAccountJpaEntity entity = mapper.toEntity(refundAccount);
        RefundAccountJpaEntity savedEntity = jpaRepository.save(entity);
        return RefundAccountId.of(savedEntity.getId());
    }
}
