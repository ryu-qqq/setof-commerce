package com.ryuqq.setof.application.discount.service.command;

import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountTargetsCommand;
import com.ryuqq.setof.application.discount.manager.command.DiscountPolicyPersistenceManager;
import com.ryuqq.setof.application.discount.manager.query.DiscountPolicyReadManager;
import com.ryuqq.setof.application.discount.port.in.command.UpdateDiscountTargetsUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 할인 정책 적용 대상 수정 서비스
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateDiscountTargetsService implements UpdateDiscountTargetsUseCase {

    private final DiscountPolicyReadManager discountPolicyReadManager;
    private final DiscountPolicyPersistenceManager discountPolicyPersistenceManager;
    private final ClockHolder clockHolder;

    public UpdateDiscountTargetsService(
            DiscountPolicyReadManager discountPolicyReadManager,
            DiscountPolicyPersistenceManager discountPolicyPersistenceManager,
            ClockHolder clockHolder) {
        this.discountPolicyReadManager = discountPolicyReadManager;
        this.discountPolicyPersistenceManager = discountPolicyPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(UpdateDiscountTargetsCommand command) {
        DiscountPolicy discountPolicy =
                discountPolicyReadManager.findById(command.discountPolicyId());

        Instant now = Instant.now(clockHolder.getClock());
        DiscountPolicy updated = discountPolicy.updateTargetIds(command.targetIds(), now);

        discountPolicyPersistenceManager.persist(updated);
    }
}
