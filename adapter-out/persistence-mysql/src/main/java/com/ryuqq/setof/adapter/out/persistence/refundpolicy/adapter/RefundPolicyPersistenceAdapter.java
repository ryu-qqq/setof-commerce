package com.ryuqq.setof.adapter.out.persistence.refundpolicy.adapter;

import com.ryuqq.setof.adapter.out.persistence.refundpolicy.entity.RefundPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.refundpolicy.mapper.RefundPolicyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.refundpolicy.repository.RefundPolicyJpaRepository;
import com.ryuqq.setof.application.refundpolicy.port.out.command.RefundPolicyPersistencePort;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import org.springframework.stereotype.Component;

/**
 * RefundPolicyPersistenceAdapter - RefundPolicy Persistence Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, RefundPolicy 저장 요청을 JpaRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefundPolicyPersistenceAdapter implements RefundPolicyPersistencePort {

    private final RefundPolicyJpaRepository jpaRepository;
    private final RefundPolicyJpaEntityMapper mapper;

    public RefundPolicyPersistenceAdapter(
            RefundPolicyJpaRepository jpaRepository, RefundPolicyJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /** RefundPolicy 저장 (생성/수정) */
    @Override
    public RefundPolicyId persist(RefundPolicy refundPolicy) {
        RefundPolicyJpaEntity entity = mapper.toEntity(refundPolicy);
        RefundPolicyJpaEntity savedEntity = jpaRepository.save(entity);
        return RefundPolicyId.of(savedEntity.getId());
    }
}
