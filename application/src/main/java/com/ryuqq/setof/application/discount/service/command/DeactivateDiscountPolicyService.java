package com.ryuqq.setof.application.discount.service.command;

import com.ryuqq.setof.application.discount.dto.command.DeactivateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.manager.command.DiscountPolicyPersistenceManager;
import com.ryuqq.setof.application.discount.manager.query.DiscountPolicyReadManager;
import com.ryuqq.setof.application.discount.port.in.command.DeactivateDiscountPolicyUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 할인 정책 비활성화 서비스
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeactivateDiscountPolicyService implements DeactivateDiscountPolicyUseCase {

    private final DiscountPolicyReadManager discountPolicyReadManager;
    private final DiscountPolicyPersistenceManager discountPolicyPersistenceManager;
    private final ClockHolder clockHolder;

    public DeactivateDiscountPolicyService(
            DiscountPolicyReadManager discountPolicyReadManager,
            DiscountPolicyPersistenceManager discountPolicyPersistenceManager,
            ClockHolder clockHolder) {
        this.discountPolicyReadManager = discountPolicyReadManager;
        this.discountPolicyPersistenceManager = discountPolicyPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(DeactivateDiscountPolicyCommand command) {
        DiscountPolicy discountPolicy =
                discountPolicyReadManager.findById(command.discountPolicyId());

        Instant now = Instant.now(clockHolder.getClock());
        DiscountPolicy deactivated = discountPolicy.deactivate(now);

        discountPolicyPersistenceManager.persist(deactivated);
    }
}
