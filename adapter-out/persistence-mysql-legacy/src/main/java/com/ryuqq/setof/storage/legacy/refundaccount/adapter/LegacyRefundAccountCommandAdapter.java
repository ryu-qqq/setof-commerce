package com.ryuqq.setof.storage.legacy.refundaccount.adapter;

import com.ryuqq.setof.application.refundaccount.port.out.command.RefundAccountCommandPort;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.storage.legacy.refundaccount.entity.LegacyRefundAccountEntity;
import com.ryuqq.setof.storage.legacy.refundaccount.mapper.LegacyRefundAccountEntityMapper;
import com.ryuqq.setof.storage.legacy.refundaccount.repository.LegacyRefundAccountJpaRepository;
import org.springframework.stereotype.Component;

/**
 * LegacyRefundAccountCommandAdapter - 레거시 환불 계좌 명령 Adapter.
 *
 * <p>Application Layer의 RefundAccountCommandPort를 구현하는 Adapter입니다. 도메인 객체를 Entity로 변환 후 JPA save로
 * persist합니다. 하이버네이트 더티체킹으로 등록/수정/삭제 상태가 자동 반영됩니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyRefundAccountCommandAdapter implements RefundAccountCommandPort {

    private final LegacyRefundAccountJpaRepository jpaRepository;
    private final LegacyRefundAccountEntityMapper mapper;

    public LegacyRefundAccountCommandAdapter(
            LegacyRefundAccountJpaRepository jpaRepository,
            LegacyRefundAccountEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Long persist(RefundAccount refundAccount) {
        LegacyRefundAccountEntity entity = mapper.toEntity(refundAccount);
        LegacyRefundAccountEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }
}
