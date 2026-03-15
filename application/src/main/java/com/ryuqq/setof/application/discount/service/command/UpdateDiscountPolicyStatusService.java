package com.ryuqq.setof.application.discount.service.command;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyStatusCommand;
import com.ryuqq.setof.application.discount.factory.DiscountPolicyCommandFactory;
import com.ryuqq.setof.application.discount.internal.DiscountPolicyCommandCoordinator;
import com.ryuqq.setof.application.discount.manager.DiscountPolicyReadManager;
import com.ryuqq.setof.application.discount.port.in.command.UpdateDiscountPolicyStatusUseCase;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import org.springframework.stereotype.Service;

/**
 * UpdateDiscountPolicyStatusService - 할인 정책 상태 일괄 변경 Service.
 *
 * <p>APP-SVC-001: Service는 @Service 어노테이션을 선언하고 UseCase 인터페이스를 구현합니다. @Transactional은
 * Coordinator/Manager에서 선언하며 Service에서는 선언하지 않습니다.
 *
 * <p>처리 흐름:
 *
 * <ol>
 *   <li>Factory를 통해 Command → StatusChangeContext 생성
 *   <li>policyIds 순회하며 ReadManager로 기존 정책 조회
 *   <li>active 여부에 따라 activate()/deactivate() 호출
 *   <li>Coordinator를 통해 정책 저장 + 아웃박스 생성
 * </ol>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class UpdateDiscountPolicyStatusService implements UpdateDiscountPolicyStatusUseCase {

    private final DiscountPolicyReadManager readManager;
    private final DiscountPolicyCommandFactory commandFactory;
    private final DiscountPolicyCommandCoordinator coordinator;

    public UpdateDiscountPolicyStatusService(
            DiscountPolicyReadManager readManager,
            DiscountPolicyCommandFactory commandFactory,
            DiscountPolicyCommandCoordinator coordinator) {
        this.readManager = readManager;
        this.commandFactory = commandFactory;
        this.coordinator = coordinator;
    }

    @Override
    public void execute(UpdateDiscountPolicyStatusCommand command) {
        StatusChangeContext<UpdateDiscountPolicyStatusCommand> context =
                commandFactory.createStatusChangeContext(command);

        for (Long policyId : context.id().policyIds()) {
            DiscountPolicy policy = readManager.getById(policyId);

            if (context.id().active()) {
                policy.activate(context.changedAt());
            } else {
                policy.deactivate(context.changedAt());
            }

            coordinator.updatePolicy(policy);
        }
    }
}
