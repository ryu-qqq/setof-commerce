package com.ryuqq.setof.application.refundaccount.manager.query;

import com.ryuqq.setof.application.refundaccount.port.out.query.RefundAccountQueryPort;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.exception.RefundAccountNotFoundException;
import com.ryuqq.setof.domain.refundaccount.vo.RefundAccountId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * RefundAccount Read Manager
 *
 * <p>단일 Query Port 조회를 담당하는 Read Manager
 *
 * <p>읽기 전용 트랜잭션 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefundAccountReadManager {

    private final RefundAccountQueryPort refundAccountQueryPort;

    public RefundAccountReadManager(RefundAccountQueryPort refundAccountQueryPort) {
        this.refundAccountQueryPort = refundAccountQueryPort;
    }

    /**
     * 환불계좌 ID로 조회 (필수)
     *
     * @param refundAccountId 환불계좌 ID
     * @return 조회된 RefundAccount
     * @throws RefundAccountNotFoundException 환불계좌를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public RefundAccount findById(Long refundAccountId) {
        RefundAccountId id = RefundAccountId.of(refundAccountId);
        return refundAccountQueryPort
                .findById(id)
                .orElseThrow(() -> new RefundAccountNotFoundException(refundAccountId));
    }

    /**
     * 회원 ID로 환불계좌 조회 (선택)
     *
     * @param memberId 회원 ID
     * @return 조회된 RefundAccount (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<RefundAccount> findByMemberId(UUID memberId) {
        return refundAccountQueryPort.findByMemberId(memberId);
    }

    /**
     * 회원의 환불계좌 존재 여부 확인
     *
     * @param memberId 회원 ID
     * @return 존재하면 true
     */
    @Transactional(readOnly = true)
    public boolean existsByMemberId(UUID memberId) {
        return refundAccountQueryPort.existsByMemberId(memberId);
    }
}
