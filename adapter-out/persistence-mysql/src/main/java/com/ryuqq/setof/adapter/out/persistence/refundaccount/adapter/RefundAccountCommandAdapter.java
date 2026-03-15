package com.ryuqq.setof.adapter.out.persistence.refundaccount.adapter;

import com.ryuqq.setof.adapter.out.persistence.refundaccount.entity.RefundAccountJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.refundaccount.mapper.RefundAccountJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.refundaccount.repository.RefundAccountJpaRepository;
import com.ryuqq.setof.application.refundaccount.port.out.command.RefundAccountCommandPort;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * RefundAccountCommandAdapter - 환불 계좌 Command 어댑터.
 *
 * <p>RefundAccountCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-003: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>활성화 조건: persistence.refundaccount.enabled=false
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "persistence.refundaccount.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class RefundAccountCommandAdapter implements RefundAccountCommandPort {

    private final RefundAccountJpaRepository jpaRepository;
    private final RefundAccountJpaEntityMapper mapper;

    public RefundAccountCommandAdapter(
            RefundAccountJpaRepository jpaRepository, RefundAccountJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 환불 계좌 저장.
     *
     * @param refundAccount 환불 계좌 도메인 객체
     * @return 저장된 환불 계좌 ID
     */
    @Override
    public Long persist(RefundAccount refundAccount) {
        RefundAccountJpaEntity entity = mapper.toEntity(refundAccount);
        RefundAccountJpaEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }
}
