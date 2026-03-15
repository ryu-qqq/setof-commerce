package com.ryuqq.setof.application.discount.service.command;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.discount.dto.command.ModifyDiscountTargetsCommand;
import com.ryuqq.setof.application.discount.factory.DiscountPolicyCommandFactory;
import com.ryuqq.setof.application.discount.internal.DiscountPolicyCommandCoordinator;
import com.ryuqq.setof.application.discount.manager.DiscountPolicyReadManager;
import com.ryuqq.setof.application.discount.port.in.command.ModifyDiscountTargetsUseCase;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetDiff;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import org.springframework.stereotype.Service;

/**
 * ModifyDiscountTargetsService - 할인 적용 대상 수정 Service.
 *
 * <p>APP-SVC-001: Service는 @Service 어노테이션을 선언하고 UseCase 인터페이스를 구현합니다. @Transactional은
 * Coordinator/Manager에서 선언하며 Service에서는 선언하지 않습니다.
 *
 * <p>처리 흐름:
 *
 * <ol>
 *   <li>Factory를 통해 Command → StatusChangeContext 생성 (시간 캡슐화)
 *   <li>ReadManager로 기존 정책(타겟 포함) 조회
 *   <li>Domain의 replaceTargets()로 Diff 계산
 *   <li>Coordinator를 통해 Diff 기반 persist + 아웃박스 생성
 * </ol>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class ModifyDiscountTargetsService implements ModifyDiscountTargetsUseCase {

    private final DiscountPolicyReadManager readManager;
    private final DiscountPolicyCommandFactory commandFactory;
    private final DiscountPolicyCommandCoordinator coordinator;

    public ModifyDiscountTargetsService(
            DiscountPolicyReadManager readManager,
            DiscountPolicyCommandFactory commandFactory,
            DiscountPolicyCommandCoordinator coordinator) {
        this.readManager = readManager;
        this.commandFactory = commandFactory;
        this.coordinator = coordinator;
    }

    @Override
    public void execute(ModifyDiscountTargetsCommand command) {
        StatusChangeContext<ModifyDiscountTargetsCommand> context =
                commandFactory.createModifyTargetsContext(command);

        ModifyDiscountTargetsCommand cmd = context.id();
        DiscountPolicy policy = readManager.getById(cmd.discountPolicyId());

        DiscountTargetDiff diff =
                policy.replaceTargets(
                        DiscountTargetType.valueOf(cmd.targetType()),
                        cmd.targetIds(),
                        context.changedAt());

        coordinator.persistTargetDiff(cmd.discountPolicyId(), diff);
    }
}
