package com.ryuqq.setof.application.discount.service.command;

import com.ryuqq.setof.application.discount.dto.command.CreateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.factory.DiscountPolicyCommandFactory;
import com.ryuqq.setof.application.discount.internal.DiscountPolicyCommandCoordinator;
import com.ryuqq.setof.application.discount.port.in.command.CreateDiscountPolicyUseCase;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import org.springframework.stereotype.Service;

/**
 * CreateDiscountPolicyService - 할인 정책 생성 Service.
 *
 * <p>APP-SVC-001: Service는 @Service 어노테이션을 선언하고 UseCase 인터페이스를 구현합니다. @Transactional은
 * Coordinator/Manager에서 선언하며 Service에서는 선언하지 않습니다.
 *
 * <p>처리 흐름:
 *
 * <ol>
 *   <li>Factory를 통해 Command → DiscountPolicy 도메인 객체 생성 (타겟 포함)
 *   <li>Coordinator를 통해 정책 + 타겟 저장 + 아웃박스 생성
 * </ol>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class CreateDiscountPolicyService implements CreateDiscountPolicyUseCase {

    private final DiscountPolicyCommandFactory commandFactory;
    private final DiscountPolicyCommandCoordinator coordinator;

    public CreateDiscountPolicyService(
            DiscountPolicyCommandFactory commandFactory,
            DiscountPolicyCommandCoordinator coordinator) {
        this.commandFactory = commandFactory;
        this.coordinator = coordinator;
    }

    @Override
    public long execute(CreateDiscountPolicyCommand command) {
        DiscountPolicy policy = commandFactory.create(command);
        return coordinator.createPolicyWithTargets(policy);
    }
}
