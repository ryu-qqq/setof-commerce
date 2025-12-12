package com.ryuqq.setof.application.refundpolicy.port.out.command;

import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;

/**
 * 환불 정책 Persistence Port (Port-Out)
 *
 * <p>환불 정책 Aggregate를 저장/수정하는 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RefundPolicyPersistencePort {

    /**
     * 환불 정책 저장 또는 수정
     *
     * @param refundPolicy 환불 정책 도메인
     * @return 저장된 환불 정책 ID
     */
    RefundPolicyId persist(RefundPolicy refundPolicy);
}
