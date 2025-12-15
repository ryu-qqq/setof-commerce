package com.ryuqq.setof.application.refundpolicy.service.command;

import com.ryuqq.setof.application.refundpolicy.dto.command.SetDefaultRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.manager.command.RefundPolicyPersistenceManager;
import com.ryuqq.setof.application.refundpolicy.manager.query.RefundPolicyReadManager;
import com.ryuqq.setof.application.refundpolicy.port.in.command.SetDefaultRefundPolicyUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 기본 환불 정책 설정 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>기존 기본 정책 조회 및 해제
 *   <li>새로운 기본 정책 설정
 *   <li>변경된 정책들 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class SetDefaultRefundPolicyService implements SetDefaultRefundPolicyUseCase {

    private final RefundPolicyReadManager refundPolicyReadManager;
    private final RefundPolicyPersistenceManager refundPolicyPersistenceManager;
    private final ClockHolder clockHolder;

    public SetDefaultRefundPolicyService(
            RefundPolicyReadManager refundPolicyReadManager,
            RefundPolicyPersistenceManager refundPolicyPersistenceManager,
            ClockHolder clockHolder) {
        this.refundPolicyReadManager = refundPolicyReadManager;
        this.refundPolicyPersistenceManager = refundPolicyPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(SetDefaultRefundPolicyCommand command) {
        Instant now = Instant.now(clockHolder.getClock());

        // 기존 기본 정책 해제
        RefundPolicy currentDefault =
                refundPolicyReadManager.findDefaultBySellerId(command.sellerId());
        if (currentDefault != null
                && !currentDefault.getIdValue().equals(command.refundPolicyId())) {
            RefundPolicy unsetPolicy = currentDefault.unsetDefault(now);
            refundPolicyPersistenceManager.persist(unsetPolicy);
        }

        // 새로운 기본 정책 설정
        RefundPolicy targetPolicy = refundPolicyReadManager.findById(command.refundPolicyId());
        if (!targetPolicy.isDefault()) {
            RefundPolicy defaultPolicy = targetPolicy.setAsDefault(now);
            refundPolicyPersistenceManager.persist(defaultPolicy);
        }
    }
}
