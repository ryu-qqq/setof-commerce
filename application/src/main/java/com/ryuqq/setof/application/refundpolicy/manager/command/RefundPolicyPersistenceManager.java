package com.ryuqq.setof.application.refundpolicy.manager.command;

import com.ryuqq.setof.application.refundpolicy.port.out.command.RefundPolicyPersistencePort;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 환불 정책 Persistence Manager
 *
 * <p>Transaction 경계를 관리하며, Port-Out을 통해 영속성 계층에 접근
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefundPolicyPersistenceManager {

    private final RefundPolicyPersistencePort refundPolicyPersistencePort;

    public RefundPolicyPersistenceManager(RefundPolicyPersistencePort refundPolicyPersistencePort) {
        this.refundPolicyPersistencePort = refundPolicyPersistencePort;
    }

    /**
     * 환불 정책 저장
     *
     * @param refundPolicy 환불 정책 도메인
     * @return 저장된 환불 정책 ID
     */
    @Transactional
    public RefundPolicyId persist(RefundPolicy refundPolicy) {
        return refundPolicyPersistencePort.persist(refundPolicy);
    }
}
