package com.ryuqq.setof.application.discount.factory.command;

import com.ryuqq.setof.application.discount.dto.command.RegisterDiscountPolicyCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.CostShare;
import com.ryuqq.setof.domain.discount.vo.DiscountAmount;
import com.ryuqq.setof.domain.discount.vo.DiscountRate;
import com.ryuqq.setof.domain.discount.vo.MaximumDiscountAmount;
import com.ryuqq.setof.domain.discount.vo.MinimumOrderAmount;
import com.ryuqq.setof.domain.discount.vo.PolicyName;
import com.ryuqq.setof.domain.discount.vo.Priority;
import com.ryuqq.setof.domain.discount.vo.UsageLimit;
import com.ryuqq.setof.domain.discount.vo.ValidPeriod;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * DiscountPolicy Command Factory
 *
 * <p>Command → Domain 변환 전용 Factory
 *
 * <p>역할:
 *
 * <ul>
 *   <li>Command DTO를 Domain 객체로 변환
 *   <li>도메인 생성 로직 캡슐화
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class DiscountPolicyCommandFactory {

    private final ClockHolder clockHolder;

    public DiscountPolicyCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * 할인 정책 생성
     *
     * @param command 할인 정책 등록 커맨드
     * @return 생성된 DiscountPolicy (저장 전)
     */
    public DiscountPolicy create(RegisterDiscountPolicyCommand command) {
        Instant now = Instant.now(clockHolder.getClock());

        PolicyName policyName = PolicyName.of(command.policyName());
        ValidPeriod validPeriod = ValidPeriod.of(command.validStartAt(), command.validEndAt());
        UsageLimit usageLimit = createUsageLimit(command);
        CostShare costShare =
                CostShare.of(command.platformCostShareRatio(), command.sellerCostShareRatio());
        Priority priority = Priority.of(command.priority());
        MinimumOrderAmount minimumOrderAmount = createMinimumOrderAmount(command);
        List<Long> targetIds = createTargetIds(command);

        if (command.discountType().isRateType()) {
            return createRateDiscount(
                    command,
                    policyName,
                    targetIds,
                    validPeriod,
                    usageLimit,
                    costShare,
                    priority,
                    minimumOrderAmount,
                    now);
        } else {
            return createFixedDiscount(
                    command,
                    policyName,
                    targetIds,
                    validPeriod,
                    usageLimit,
                    costShare,
                    priority,
                    minimumOrderAmount,
                    now);
        }
    }

    private DiscountPolicy createRateDiscount(
            RegisterDiscountPolicyCommand command,
            PolicyName policyName,
            List<Long> targetIds,
            ValidPeriod validPeriod,
            UsageLimit usageLimit,
            CostShare costShare,
            Priority priority,
            MinimumOrderAmount minimumOrderAmount,
            Instant now) {

        DiscountRate discountRate = DiscountRate.of(command.discountRate());
        MaximumDiscountAmount maximumDiscountAmount = createMaximumDiscountAmount(command);

        return DiscountPolicy.forNewRateDiscount(
                command.sellerId(),
                policyName,
                command.discountGroup(),
                command.discountTargetType(),
                targetIds,
                discountRate,
                maximumDiscountAmount,
                minimumOrderAmount,
                validPeriod,
                usageLimit,
                costShare,
                priority,
                now);
    }

    private DiscountPolicy createFixedDiscount(
            RegisterDiscountPolicyCommand command,
            PolicyName policyName,
            List<Long> targetIds,
            ValidPeriod validPeriod,
            UsageLimit usageLimit,
            CostShare costShare,
            Priority priority,
            MinimumOrderAmount minimumOrderAmount,
            Instant now) {

        DiscountAmount discountAmount = DiscountAmount.of(command.fixedDiscountAmount());

        return DiscountPolicy.forNewFixedDiscount(
                command.sellerId(),
                policyName,
                command.discountGroup(),
                command.discountTargetType(),
                targetIds,
                discountAmount,
                minimumOrderAmount,
                validPeriod,
                usageLimit,
                costShare,
                priority,
                now);
    }

    private List<Long> createTargetIds(RegisterDiscountPolicyCommand command) {
        if (command.targetId() == null) {
            return List.of();
        }
        return List.of(command.targetId());
    }

    private UsageLimit createUsageLimit(RegisterDiscountPolicyCommand command) {
        if (command.maxUsagePerCustomer() == null && command.maxTotalUsage() == null) {
            return UsageLimit.unlimited();
        }
        return UsageLimit.of(command.maxUsagePerCustomer(), command.maxTotalUsage());
    }

    private MinimumOrderAmount createMinimumOrderAmount(RegisterDiscountPolicyCommand command) {
        if (command.minimumOrderAmount() == null) {
            return MinimumOrderAmount.noMinimum();
        }
        return MinimumOrderAmount.of(command.minimumOrderAmount());
    }

    private MaximumDiscountAmount createMaximumDiscountAmount(
            RegisterDiscountPolicyCommand command) {
        if (command.maximumDiscountAmount() == null) {
            return MaximumDiscountAmount.unlimited();
        }
        return MaximumDiscountAmount.of(command.maximumDiscountAmount());
    }
}
