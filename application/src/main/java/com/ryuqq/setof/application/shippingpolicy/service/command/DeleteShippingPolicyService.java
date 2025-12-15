package com.ryuqq.setof.application.shippingpolicy.service.command;

import com.ryuqq.setof.application.shippingpolicy.dto.command.DeleteShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.manager.command.ShippingPolicyPersistenceManager;
import com.ryuqq.setof.application.shippingpolicy.manager.query.ShippingPolicyReadManager;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.DeleteShippingPolicyUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import com.ryuqq.setof.domain.shippingpolicy.exception.LastShippingPolicyCannotBeDeletedException;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 배송 정책 삭제 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>삭제 가능 여부 확인 (마지막 정책인지)
 *   <li>ShippingPolicyReadManager로 기존 배송 정책 조회
 *   <li>delete 메서드 호출 (Soft Delete)
 *   <li>기본 정책이었다면 다른 정책으로 승격
 *   <li>ShippingPolicyPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeleteShippingPolicyService implements DeleteShippingPolicyUseCase {

    private final ShippingPolicyReadManager shippingPolicyReadManager;
    private final ShippingPolicyPersistenceManager shippingPolicyPersistenceManager;
    private final ClockHolder clockHolder;

    public DeleteShippingPolicyService(
            ShippingPolicyReadManager shippingPolicyReadManager,
            ShippingPolicyPersistenceManager shippingPolicyPersistenceManager,
            ClockHolder clockHolder) {
        this.shippingPolicyReadManager = shippingPolicyReadManager;
        this.shippingPolicyPersistenceManager = shippingPolicyPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(DeleteShippingPolicyCommand command) {
        // 마지막 정책인지 확인
        long policyCount = shippingPolicyReadManager.countBySellerId(command.sellerId(), false);
        if (policyCount <= 1) {
            throw new LastShippingPolicyCannotBeDeletedException(
                    command.sellerId(), command.shippingPolicyId());
        }

        Instant now = Instant.now(clockHolder.getClock());

        ShippingPolicy targetPolicy =
                shippingPolicyReadManager.findById(command.shippingPolicyId());
        boolean wasDefault = targetPolicy.isDefault();

        // Soft Delete
        ShippingPolicy deletedPolicy = targetPolicy.delete(now);
        shippingPolicyPersistenceManager.persist(deletedPolicy);

        // 기본 정책이었다면 다른 정책으로 승격
        if (wasDefault) {
            promoteNextDefaultPolicy(command.sellerId(), command.shippingPolicyId(), now);
        }
    }

    private void promoteNextDefaultPolicy(Long sellerId, Long excludePolicyId, Instant now) {
        List<ShippingPolicy> remainingPolicies =
                shippingPolicyReadManager.findBySellerId(sellerId, false);

        remainingPolicies.stream()
                .filter(policy -> !policy.getIdValue().equals(excludePolicyId))
                .findFirst()
                .ifPresent(
                        policy -> {
                            ShippingPolicy newDefault = policy.setAsDefault(now);
                            shippingPolicyPersistenceManager.persist(newDefault);
                        });
    }
}
