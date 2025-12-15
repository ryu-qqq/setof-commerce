package com.ryuqq.setof.application.refundpolicy.service.command;

import com.ryuqq.setof.application.refundpolicy.dto.command.RegisterRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.factory.command.RefundPolicyCommandFactory;
import com.ryuqq.setof.application.refundpolicy.manager.command.RefundPolicyPersistenceManager;
import com.ryuqq.setof.application.refundpolicy.port.in.command.RegisterRefundPolicyUseCase;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import org.springframework.stereotype.Service;

/**
 * 환불 정책 등록 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>RefundPolicyCommandFactory로 RefundPolicy 도메인 생성 (VO 검증 포함)
 *   <li>RefundPolicyPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RegisterRefundPolicyService implements RegisterRefundPolicyUseCase {

    private final RefundPolicyCommandFactory refundPolicyCommandFactory;
    private final RefundPolicyPersistenceManager refundPolicyPersistenceManager;

    public RegisterRefundPolicyService(
            RefundPolicyCommandFactory refundPolicyCommandFactory,
            RefundPolicyPersistenceManager refundPolicyPersistenceManager) {
        this.refundPolicyCommandFactory = refundPolicyCommandFactory;
        this.refundPolicyPersistenceManager = refundPolicyPersistenceManager;
    }

    @Override
    public Long execute(RegisterRefundPolicyCommand command) {
        RefundPolicy refundPolicy = refundPolicyCommandFactory.create(command);
        RefundPolicyId refundPolicyId = refundPolicyPersistenceManager.persist(refundPolicy);
        return refundPolicyId.value();
    }
}
