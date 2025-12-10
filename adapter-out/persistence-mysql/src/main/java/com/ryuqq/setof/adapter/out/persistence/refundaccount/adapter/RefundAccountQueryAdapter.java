package com.ryuqq.setof.adapter.out.persistence.refundaccount.adapter;

import com.ryuqq.setof.adapter.out.persistence.refundaccount.mapper.RefundAccountJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.refundaccount.repository.RefundAccountQueryDslRepository;
import com.ryuqq.setof.application.refundaccount.port.out.query.RefundAccountQueryPort;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.vo.RefundAccountId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * RefundAccountQueryAdapter - RefundAccount Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, RefundAccount 조회 요청을 QueryDslRepository에 위임하고 Mapper를 통해
 * Domain으로 변환하여 반환합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>ID로 단건 조회 (findById)
 *   <li>회원별 환불계좌 조회 (findByMemberId)
 *   <li>회원별 환불계좌 존재 여부 (existsByMemberId)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefundAccountQueryAdapter implements RefundAccountQueryPort {

    private final RefundAccountQueryDslRepository queryDslRepository;
    private final RefundAccountJpaEntityMapper refundAccountJpaEntityMapper;

    public RefundAccountQueryAdapter(
            RefundAccountQueryDslRepository queryDslRepository,
            RefundAccountJpaEntityMapper refundAccountJpaEntityMapper) {
        this.queryDslRepository = queryDslRepository;
        this.refundAccountJpaEntityMapper = refundAccountJpaEntityMapper;
    }

    /**
     * ID로 RefundAccount 단건 조회
     *
     * @param id RefundAccount ID (Value Object)
     * @return RefundAccount Domain (Optional)
     */
    @Override
    public Optional<RefundAccount> findById(RefundAccountId id) {
        return queryDslRepository
                .findById(id.value())
                .map(refundAccountJpaEntityMapper::toDomain);
    }

    /**
     * 회원별 환불계좌 조회
     *
     * @param memberId 회원 ID (UUID)
     * @return RefundAccount (Optional)
     */
    @Override
    public Optional<RefundAccount> findByMemberId(UUID memberId) {
        return queryDslRepository
                .findByMemberId(memberId.toString())
                .map(refundAccountJpaEntityMapper::toDomain);
    }

    /**
     * 회원별 환불계좌 존재 여부 확인
     *
     * @param memberId 회원 ID (UUID)
     * @return 존재 여부
     */
    @Override
    public boolean existsByMemberId(UUID memberId) {
        return queryDslRepository.existsByMemberId(memberId.toString());
    }
}
