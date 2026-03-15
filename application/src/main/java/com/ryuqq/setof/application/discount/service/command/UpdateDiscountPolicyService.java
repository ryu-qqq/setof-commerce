package com.ryuqq.setof.application.discount.service.command;

import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.factory.DiscountPolicyCommandFactory;
import com.ryuqq.setof.application.discount.internal.DiscountPolicyCommandCoordinator;
import com.ryuqq.setof.application.discount.manager.DiscountPolicyReadManager;
import com.ryuqq.setof.application.discount.port.in.command.UpdateDiscountPolicyUseCase;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicyUpdateData;
import org.springframework.stereotype.Service;

/**
 * UpdateDiscountPolicyService - 할인 정책 수정 Service.
 *
 * <p>APP-SVC-001: Service는 @Service 어노테이션을 선언하고 UseCase 인터페이스를 구현합니다. @Transactional은
 * Coordinator/Manager에서 선언하며 Service에서는 선언하지 않습니다.
 *
 * <p>처리 흐름:
 *
 * <ol>
 *   <li>Factory를 통해 Command → UpdateContext 생성
 *   <li>ReadManager를 통해 기존 정책 조회
 *   <li>도메인 update() 호출
 *   <li>Coordinator를 통해 정책 저장 + 아웃박스 생성
 * </ol>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class UpdateDiscountPolicyService implements UpdateDiscountPolicyUseCase {

    private final DiscountPolicyReadManager readManager;
    private final DiscountPolicyCommandFactory commandFactory;
    private final DiscountPolicyCommandCoordinator coordinator;

    public UpdateDiscountPolicyService(
            DiscountPolicyReadManager readManager,
            DiscountPolicyCommandFactory commandFactory,
            DiscountPolicyCommandCoordinator coordinator) {
        this.readManager = readManager;
        this.commandFactory = commandFactory;
        this.coordinator = coordinator;
    }

    @Override
    public void execute(UpdateDiscountPolicyCommand command) {
        UpdateContext<Long, DiscountPolicyUpdateData> context =
                commandFactory.createUpdateContext(command);
        DiscountPolicy policy = readManager.getById(context.id());
        policy.update(context.updateData(), context.changedAt());
        coordinator.updatePolicy(policy);
    }
}
