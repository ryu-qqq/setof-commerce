package com.ryuqq.setof.application.refundpolicy.manager.query;

import com.ryuqq.setof.application.refundpolicy.port.out.query.RefundPolicyQueryPort;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.exception.RefundPolicyNotFoundException;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 환불 정책 Read Manager
 *
 * <p>읽기 전용 트랜잭션으로 Port-Out을 통해 조회
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
@Transactional(readOnly = true)
public class RefundPolicyReadManager {

    private final RefundPolicyQueryPort refundPolicyQueryPort;

    public RefundPolicyReadManager(RefundPolicyQueryPort refundPolicyQueryPort) {
        this.refundPolicyQueryPort = refundPolicyQueryPort;
    }

    /**
     * ID로 환불 정책 조회
     *
     * @param refundPolicyId 환불 정책 ID
     * @return 환불 정책 도메인
     * @throws RefundPolicyNotFoundException 환불 정책이 없는 경우
     */
    public RefundPolicy findById(Long refundPolicyId) {
        return refundPolicyQueryPort
                .findById(RefundPolicyId.of(refundPolicyId))
                .orElseThrow(() -> new RefundPolicyNotFoundException(refundPolicyId));
    }

    /**
     * 셀러 ID로 환불 정책 목록 조회
     *
     * @param sellerId 셀러 ID
     * @param includeDeleted 삭제된 정책 포함 여부
     * @return 환불 정책 목록
     */
    public List<RefundPolicy> findBySellerId(Long sellerId, boolean includeDeleted) {
        return refundPolicyQueryPort.findBySellerId(sellerId, includeDeleted);
    }

    /**
     * 셀러의 기본 환불 정책 조회
     *
     * @param sellerId 셀러 ID
     * @return 기본 환불 정책 (Optional)
     */
    public RefundPolicy findDefaultBySellerId(Long sellerId) {
        return refundPolicyQueryPort.findDefaultBySellerId(sellerId).orElse(null);
    }

    /**
     * 셀러의 환불 정책 개수 조회
     *
     * @param sellerId 셀러 ID
     * @param includeDeleted 삭제된 정책 포함 여부
     * @return 환불 정책 개수
     */
    public long countBySellerId(Long sellerId, boolean includeDeleted) {
        return refundPolicyQueryPort.countBySellerId(sellerId, includeDeleted);
    }
}
