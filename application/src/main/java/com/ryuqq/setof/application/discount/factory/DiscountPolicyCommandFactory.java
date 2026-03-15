package com.ryuqq.setof.application.discount.factory;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.discount.dto.command.CreateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.ModifyDiscountTargetsCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyStatusCommand;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicyUpdateData;
import com.ryuqq.setof.domain.discount.vo.ApplicationType;
import com.ryuqq.setof.domain.discount.vo.DiscountBudget;
import com.ryuqq.setof.domain.discount.vo.DiscountMethod;
import com.ryuqq.setof.domain.discount.vo.DiscountPeriod;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyName;
import com.ryuqq.setof.domain.discount.vo.DiscountRate;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.Priority;
import com.ryuqq.setof.domain.discount.vo.PublisherType;
import com.ryuqq.setof.domain.discount.vo.StackingGroup;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * DiscountPolicyCommandFactory - 할인 정책 도메인 객체 생성 Factory.
 *
 * <p>APP-TIM-001: TimeProvider.now() 호출은 Factory에서만 허용합니다.
 *
 * <p>Factory의 책임:
 *
 * <ul>
 *   <li>create() - Command → 완성된 Domain Aggregate 생성 (타겟 포함)
 *   <li>createUpdateContext() - Command → UpdateContext 생성
 *   <li>createStatusChangeContexts() - Command → StatusChangeContext 목록 생성
 *   <li>createModifyTargetsContext() - Command → StatusChangeContext 생성
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountPolicyCommandFactory {

    private final TimeProvider timeProvider;

    public DiscountPolicyCommandFactory(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    /**
     * 할인 정책 생성 Command로 타겟이 포함된 완성 DiscountPolicy를 생성합니다.
     *
     * <p>APP-TIM-001: TimeProvider.now()를 Factory에서 호출합니다.
     *
     * @param command 할인 정책 생성 Command
     * @return 타겟이 포함된 신규 할인 정책 Aggregate
     */
    public DiscountPolicy create(CreateDiscountPolicyCommand command) {
        Instant now = timeProvider.now();

        DiscountPolicy policy =
                DiscountPolicy.forNew(
                        DiscountPolicyName.of(command.name()),
                        command.description(),
                        toDiscountMethod(command.discountMethod()),
                        toDiscountRate(command.discountRate()),
                        toMoney(command.discountAmount()),
                        toMoney(command.maxDiscountAmount()),
                        command.discountCapped(),
                        toMoney(command.minimumOrderAmount()),
                        ApplicationType.valueOf(command.applicationType()),
                        PublisherType.valueOf(command.publisherType()),
                        toSellerId(command.sellerId()),
                        StackingGroup.valueOf(command.stackingGroup()),
                        Priority.of(command.priority()),
                        DiscountPeriod.of(command.startAt(), command.endAt()),
                        DiscountBudget.of(Money.of(command.totalBudget()), Money.zero()),
                        now);

        if (command.targets() != null) {
            for (CreateDiscountPolicyCommand.TargetItem item : command.targets()) {
                policy.addTarget(
                        DiscountTargetType.valueOf(item.targetType()), item.targetId(), now);
            }
        }

        return policy;
    }

    /**
     * 할인 정책 수정 Command로 UpdateContext를 생성합니다.
     *
     * <p>APP-TIM-001: TimeProvider.now()를 Factory에서 호출합니다.
     *
     * @param command 할인 정책 수정 Command
     * @return UpdateContext (DiscountPolicyId, DiscountPolicyUpdateData, changedAt)
     */
    public UpdateContext<Long, DiscountPolicyUpdateData> createUpdateContext(
            UpdateDiscountPolicyCommand command) {
        Instant now = timeProvider.now();
        DiscountPolicyUpdateData updateData =
                DiscountPolicyUpdateData.of(
                        DiscountPolicyName.of(command.name()),
                        command.description(),
                        toDiscountMethod(command.discountMethod()),
                        toDiscountRate(command.discountRate()),
                        toMoney(command.discountAmount()),
                        toMoney(command.maxDiscountAmount()),
                        command.discountCapped(),
                        toMoney(command.minimumOrderAmount()),
                        Priority.of(command.priority()),
                        DiscountPeriod.of(command.startAt(), command.endAt()),
                        DiscountBudget.of(Money.of(command.totalBudget()), Money.zero()));
        return new UpdateContext<>(command.discountPolicyId(), updateData, now);
    }

    /**
     * 할인 정책 상태 일괄 변경 Command로 StatusChangeContext 목록을 생성합니다.
     *
     * <p>APP-TIM-001: TimeProvider.now()를 Factory에서 호출합니다.
     *
     * @param command 상태 변경 Command
     * @return StatusChangeContext 목록 (policyId별)
     */
    public StatusChangeContext<UpdateDiscountPolicyStatusCommand> createStatusChangeContext(
            UpdateDiscountPolicyStatusCommand command) {
        Instant now = timeProvider.now();
        return new StatusChangeContext<>(command, now);
    }

    /**
     * 할인 적용 대상 수정 Command로 StatusChangeContext를 생성합니다.
     *
     * <p>APP-TIM-001: TimeProvider.now()를 Factory에서 호출합니다.
     *
     * @param command 할인 적용 대상 수정 Command
     * @return StatusChangeContext (command, changedAt)
     */
    public StatusChangeContext<ModifyDiscountTargetsCommand> createModifyTargetsContext(
            ModifyDiscountTargetsCommand command) {
        Instant now = timeProvider.now();
        return new StatusChangeContext<>(command, now);
    }

    // ---------------------------------------------------------------------------
    // private 변환 헬퍼
    // ---------------------------------------------------------------------------

    private DiscountMethod toDiscountMethod(String value) {
        return DiscountMethod.valueOf(value);
    }

    private DiscountRate toDiscountRate(Double value) {
        return value != null ? DiscountRate.of(value) : null;
    }

    private Money toMoney(Integer value) {
        return value != null ? Money.of(value) : null;
    }

    private SellerId toSellerId(Long value) {
        return value != null ? SellerId.of(value) : null;
    }
}
