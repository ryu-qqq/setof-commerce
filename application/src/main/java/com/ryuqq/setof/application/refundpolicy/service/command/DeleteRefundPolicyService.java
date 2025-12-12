package com.ryuqq.setof.application.refundpolicy.service.command;

import com.ryuqq.setof.application.refundpolicy.dto.command.DeleteRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.manager.command.RefundPolicyPersistenceManager;
import com.ryuqq.setof.application.refundpolicy.manager.query.RefundPolicyReadManager;
import com.ryuqq.setof.application.refundpolicy.port.in.command.DeleteRefundPolicyUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.exception.LastRefundPolicyCannotBeDeletedException;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 환불 정책 삭제 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>삭제 가능 여부 확인 (마지막 정책인지)
 *   <li>RefundPolicyReadManager로 기존 환불 정책 조회
 *   <li>delete 메서드 호출 (Soft Delete)
 *   <li>기본 정책이었다면 다른 정책으로 승격
 *   <li>RefundPolicyPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeleteRefundPolicyService implements DeleteRefundPolicyUseCase {

    private final RefundPolicyReadManager refundPolicyReadManager;
    private final RefundPolicyPersistenceManager refundPolicyPersistenceManager;
    private final ClockHolder clockHolder;

    public DeleteRefundPolicyService(
            RefundPolicyReadManager refundPolicyReadManager,
            RefundPolicyPersistenceManager refundPolicyPersistenceManager,
            ClockHolder clockHolder) {
        this.refundPolicyReadManager = refundPolicyReadManager;
        this.refundPolicyPersistenceManager = refundPolicyPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(DeleteRefundPolicyCommand command) {
        // 마지막 정책인지 확인
        long policyCount = refundPolicyReadManager.countBySellerId(command.sellerId(), false);
        if (policyCount <= 1) {
            throw new LastRefundPolicyCannotBeDeletedException(
                    command.sellerId(), command.refundPolicyId());
        }

        Instant now = Instant.now(clockHolder.getClock());

        RefundPolicy targetPolicy = refundPolicyReadManager.findById(command.refundPolicyId());
        boolean wasDefault = targetPolicy.isDefault();

        // Soft Delete
        RefundPolicy deletedPolicy = targetPolicy.delete(now);
        refundPolicyPersistenceManager.persist(deletedPolicy);

        // 기본 정책이었다면 다른 정책으로 승격
        if (wasDefault) {
            promoteNextDefaultPolicy(command.sellerId(), command.refundPolicyId(), now);
        }
    }

    private void promoteNextDefaultPolicy(Long sellerId, Long excludePolicyId, Instant now) {
        List<RefundPolicy> remainingPolicies =
                refundPolicyReadManager.findBySellerId(sellerId, false);

        remainingPolicies.stream()
                .filter(policy -> !policy.getIdValue().equals(excludePolicyId))
                .findFirst()
                .ifPresent(
                        policy -> {
                            RefundPolicy newDefault = policy.setAsDefault(now);
                            refundPolicyPersistenceManager.persist(newDefault);
                        });
    }
}
