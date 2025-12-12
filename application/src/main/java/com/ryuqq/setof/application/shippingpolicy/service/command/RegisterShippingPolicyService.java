package com.ryuqq.setof.application.shippingpolicy.service.command;

import com.ryuqq.setof.application.shippingpolicy.dto.command.RegisterShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.factory.command.ShippingPolicyCommandFactory;
import com.ryuqq.setof.application.shippingpolicy.manager.command.ShippingPolicyPersistenceManager;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.RegisterShippingPolicyUseCase;
import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import org.springframework.stereotype.Service;

/**
 * 배송 정책 등록 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>ShippingPolicyCommandFactory로 ShippingPolicy 도메인 생성 (VO 검증 포함)
 *   <li>ShippingPolicyPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RegisterShippingPolicyService implements RegisterShippingPolicyUseCase {

    private final ShippingPolicyCommandFactory shippingPolicyCommandFactory;
    private final ShippingPolicyPersistenceManager shippingPolicyPersistenceManager;

    public RegisterShippingPolicyService(
            ShippingPolicyCommandFactory shippingPolicyCommandFactory,
            ShippingPolicyPersistenceManager shippingPolicyPersistenceManager) {
        this.shippingPolicyCommandFactory = shippingPolicyCommandFactory;
        this.shippingPolicyPersistenceManager = shippingPolicyPersistenceManager;
    }

    @Override
    public Long execute(RegisterShippingPolicyCommand command) {
        ShippingPolicy shippingPolicy = shippingPolicyCommandFactory.create(command);
        ShippingPolicyId shippingPolicyId =
                shippingPolicyPersistenceManager.persist(shippingPolicy);
        return shippingPolicyId.value();
    }
}
