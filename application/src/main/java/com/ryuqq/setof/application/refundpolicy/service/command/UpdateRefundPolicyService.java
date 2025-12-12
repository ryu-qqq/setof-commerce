package com.ryuqq.setof.application.refundpolicy.service.command;

import com.ryuqq.setof.application.refundpolicy.dto.command.UpdateRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.manager.command.RefundPolicyPersistenceManager;
import com.ryuqq.setof.application.refundpolicy.manager.query.RefundPolicyReadManager;
import com.ryuqq.setof.application.refundpolicy.port.in.command.UpdateRefundPolicyUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.vo.PolicyName;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundDeliveryCost;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundGuide;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPeriodDays;
import com.ryuqq.setof.domain.refundpolicy.vo.ReturnAddress;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 환불 정책 수정 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>RefundPolicyReadManager로 기존 환불 정책 조회
 *   <li>VO 생성 및 update 메서드 호출
 *   <li>RefundPolicyPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateRefundPolicyService implements UpdateRefundPolicyUseCase {

    private final RefundPolicyReadManager refundPolicyReadManager;
    private final RefundPolicyPersistenceManager refundPolicyPersistenceManager;
    private final ClockHolder clockHolder;

    public UpdateRefundPolicyService(
            RefundPolicyReadManager refundPolicyReadManager,
            RefundPolicyPersistenceManager refundPolicyPersistenceManager,
            ClockHolder clockHolder) {
        this.refundPolicyReadManager = refundPolicyReadManager;
        this.refundPolicyPersistenceManager = refundPolicyPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(UpdateRefundPolicyCommand command) {
        RefundPolicy existingPolicy = refundPolicyReadManager.findById(command.refundPolicyId());

        PolicyName policyName = PolicyName.of(command.policyName());
        ReturnAddress returnAddress =
                ReturnAddress.of(
                        command.returnAddressLine1(),
                        command.returnAddressLine2(),
                        command.returnZipCode());
        RefundPeriodDays refundPeriodDays = RefundPeriodDays.of(command.refundPeriodDays());
        RefundDeliveryCost refundDeliveryCost = RefundDeliveryCost.of(command.refundDeliveryCost());
        RefundGuide refundGuide =
                command.refundGuide() != null ? RefundGuide.of(command.refundGuide()) : null;

        RefundPolicy updatedPolicy =
                existingPolicy.update(
                        policyName,
                        returnAddress,
                        refundPeriodDays,
                        refundDeliveryCost,
                        refundGuide,
                        Instant.now(clockHolder.getClock()));

        refundPolicyPersistenceManager.persist(updatedPolicy);
    }
}
