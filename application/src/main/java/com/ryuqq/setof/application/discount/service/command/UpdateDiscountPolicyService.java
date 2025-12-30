package com.ryuqq.setof.application.discount.service.command;

import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.manager.command.DiscountPolicyPersistenceManager;
import com.ryuqq.setof.application.discount.manager.query.DiscountPolicyReadManager;
import com.ryuqq.setof.application.discount.port.in.command.UpdateDiscountPolicyUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.CostShare;
import com.ryuqq.setof.domain.discount.vo.MaximumDiscountAmount;
import com.ryuqq.setof.domain.discount.vo.MinimumOrderAmount;
import com.ryuqq.setof.domain.discount.vo.PolicyName;
import com.ryuqq.setof.domain.discount.vo.Priority;
import com.ryuqq.setof.domain.discount.vo.UsageLimit;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 할인 정책 수정 서비스
 *
 * <p>수정 가능한 필드:
 *
 * <ul>
 *   <li>정책명
 *   <li>최대/최소 금액
 *   <li>유효 기간 연장
 *   <li>사용 횟수 제한
 *   <li>비용 분담 비율
 *   <li>우선순위
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateDiscountPolicyService implements UpdateDiscountPolicyUseCase {

    private final DiscountPolicyReadManager discountPolicyReadManager;
    private final DiscountPolicyPersistenceManager discountPolicyPersistenceManager;
    private final ClockHolder clockHolder;

    public UpdateDiscountPolicyService(
            DiscountPolicyReadManager discountPolicyReadManager,
            DiscountPolicyPersistenceManager discountPolicyPersistenceManager,
            ClockHolder clockHolder) {
        this.discountPolicyReadManager = discountPolicyReadManager;
        this.discountPolicyPersistenceManager = discountPolicyPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(UpdateDiscountPolicyCommand command) {
        DiscountPolicy discountPolicy =
                discountPolicyReadManager.findById(command.discountPolicyId());

        Instant now = Instant.now(clockHolder.getClock());
        DiscountPolicy updated = applyUpdates(discountPolicy, command, now);

        discountPolicyPersistenceManager.persist(updated);
    }

    private DiscountPolicy applyUpdates(
            DiscountPolicy discountPolicy, UpdateDiscountPolicyCommand command, Instant now) {
        DiscountPolicy result = discountPolicy;

        if (command.policyName() != null) {
            result = result.changePolicyName(PolicyName.of(command.policyName()), now);
        }

        if (command.maximumDiscountAmount() != null) {
            result =
                    result.changeMaximumDiscountAmount(
                            MaximumDiscountAmount.of(command.maximumDiscountAmount()), now);
        }

        if (command.minimumOrderAmount() != null) {
            result =
                    result.changeMinimumOrderAmount(
                            MinimumOrderAmount.of(command.minimumOrderAmount()), now);
        }

        if (command.validEndAt() != null) {
            result = result.extendValidPeriod(command.validEndAt(), now);
        }

        if (command.maxUsagePerCustomer() != null || command.maxTotalUsage() != null) {
            UsageLimit newUsageLimit =
                    UsageLimit.of(command.maxUsagePerCustomer(), command.maxTotalUsage());
            result = result.changeUsageLimit(newUsageLimit, now);
        }

        if (command.platformCostShareRatio() != null && command.sellerCostShareRatio() != null) {
            CostShare newCostShare =
                    CostShare.of(command.platformCostShareRatio(), command.sellerCostShareRatio());
            result = result.changeCostShare(newCostShare, now);
        }

        if (command.priority() != null) {
            result = result.changePriority(Priority.of(command.priority()), now);
        }

        return result;
    }
}
