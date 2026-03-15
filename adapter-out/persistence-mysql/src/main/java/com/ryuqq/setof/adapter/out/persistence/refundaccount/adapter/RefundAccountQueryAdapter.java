package com.ryuqq.setof.adapter.out.persistence.refundaccount.adapter;

import com.ryuqq.setof.adapter.out.persistence.refundaccount.mapper.RefundAccountJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.refundaccount.repository.RefundAccountQueryDslRepository;
import com.ryuqq.setof.application.refundaccount.port.out.query.RefundAccountQueryPort;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * RefundAccountQueryAdapter - 환불 계좌 Query 어댑터.
 *
 * <p>RefundAccountQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * <p>NOTE: RefundAccountJpaEntity는 memberId(Long) 컬럼을 보유합니다.
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
public class RefundAccountQueryAdapter implements RefundAccountQueryPort {

    private final RefundAccountQueryDslRepository queryDslRepository;
    private final RefundAccountJpaEntityMapper mapper;

    public RefundAccountQueryAdapter(
            RefundAccountQueryDslRepository queryDslRepository,
            RefundAccountJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * userId로 활성 환불 계좌 조회.
     *
     * @param userId 사용자 ID
     * @return RefundAccount 도메인 객체 Optional
     */
    @Override
    public Optional<RefundAccount> fetchRefundAccount(long userId) {
        return queryDslRepository.findByMemberId(userId).map(mapper::toDomain);
    }

    /**
     * userId + refundAccountId로 환불 계좌 조회 (삭제 여부 무관).
     *
     * @param userId 사용자 ID
     * @param refundAccountId 환불 계좌 ID
     * @return RefundAccount 도메인 객체 Optional
     */
    @Override
    public Optional<RefundAccount> findByUserIdAndId(long userId, long refundAccountId) {
        return queryDslRepository
                .findByMemberIdAndId(userId, refundAccountId)
                .map(mapper::toDomain);
    }
}
