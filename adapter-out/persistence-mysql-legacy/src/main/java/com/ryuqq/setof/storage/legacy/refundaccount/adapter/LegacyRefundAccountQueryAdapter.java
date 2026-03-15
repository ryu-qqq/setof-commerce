package com.ryuqq.setof.storage.legacy.refundaccount.adapter;

import com.ryuqq.setof.application.refundaccount.port.out.query.RefundAccountQueryPort;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.storage.legacy.refundaccount.mapper.LegacyRefundAccountEntityMapper;
import com.ryuqq.setof.storage.legacy.refundaccount.repository.LegacyRefundAccountQueryDslRepository;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * LegacyRefundAccountQueryAdapter - 레거시 환불 계좌 조회 Adapter.
 *
 * <p>Application Layer의 RefundAccountQueryPort를 구현하는 Adapter입니다. 도메인 객체만 반환하며, Result DTO 변환은
 * Application 레이어의 Assembler가 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.member.enabled", havingValue = "true")
public class LegacyRefundAccountQueryAdapter implements RefundAccountQueryPort {

    private final LegacyRefundAccountQueryDslRepository repository;
    private final LegacyRefundAccountEntityMapper mapper;

    public LegacyRefundAccountQueryAdapter(
            LegacyRefundAccountQueryDslRepository repository,
            LegacyRefundAccountEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<RefundAccount> fetchRefundAccount(long userId) {
        return repository.findActiveByUserId(userId).map(mapper::toDomain);
    }

    @Override
    public Optional<RefundAccount> findByUserIdAndId(long userId, long refundAccountId) {
        return repository.findByUserIdAndId(userId, refundAccountId).map(mapper::toDomain);
    }
}
