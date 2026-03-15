package com.ryuqq.setof.application.discount.service.command;

import com.ryuqq.setof.application.discount.dto.command.CreateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.factory.DiscountPolicyCommandFactory;
import com.ryuqq.setof.application.discount.internal.DiscountPolicyCommandCoordinator;
import com.ryuqq.setof.application.discount.port.in.command.CreateDiscountPoliciesFromExcelUseCase;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * CreateDiscountPoliciesFromExcelService - 엑셀 업로드 기반 할인 정책 일괄 생성 Service.
 *
 * <p>APP-SVC-001: Service는 @Service 어노테이션을 선언하고 UseCase 인터페이스를 구현합니다. @Transactional은
 * Coordinator/Manager에서 선언하며 Service에서는 선언하지 않습니다.
 *
 * <p>처리 흐름:
 *
 * <ol>
 *   <li>Command 목록을 순회하며 Factory → DiscountPolicy 생성 (타겟 포함)
 *   <li>각 정책을 Coordinator를 통해 저장
 * </ol>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class CreateDiscountPoliciesFromExcelService
        implements CreateDiscountPoliciesFromExcelUseCase {

    private final DiscountPolicyCommandFactory commandFactory;
    private final DiscountPolicyCommandCoordinator coordinator;

    public CreateDiscountPoliciesFromExcelService(
            DiscountPolicyCommandFactory commandFactory,
            DiscountPolicyCommandCoordinator coordinator) {
        this.commandFactory = commandFactory;
        this.coordinator = coordinator;
    }

    @Override
    public List<Long> execute(List<CreateDiscountPolicyCommand> commands) {
        List<Long> policyIds = new ArrayList<>(commands.size());
        for (CreateDiscountPolicyCommand command : commands) {
            DiscountPolicy policy = commandFactory.create(command);
            long policyId = coordinator.createPolicyWithTargets(policy);
            policyIds.add(policyId);
        }
        return policyIds;
    }
}
