package com.ryuqq.setof.application.discount.service.command;

import com.ryuqq.setof.application.discount.dto.command.DeleteDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.manager.command.DiscountPolicyPersistenceManager;
import com.ryuqq.setof.application.discount.manager.query.DiscountPolicyReadManager;
import com.ryuqq.setof.application.discount.port.in.command.DeleteDiscountPolicyUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 할인 정책 삭제 서비스 (Soft Delete)
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeleteDiscountPolicyService implements DeleteDiscountPolicyUseCase {

    private final DiscountPolicyReadManager discountPolicyReadManager;
    private final DiscountPolicyPersistenceManager discountPolicyPersistenceManager;
    private final ClockHolder clockHolder;

    public DeleteDiscountPolicyService(
            DiscountPolicyReadManager discountPolicyReadManager,
            DiscountPolicyPersistenceManager discountPolicyPersistenceManager,
            ClockHolder clockHolder) {
        this.discountPolicyReadManager = discountPolicyReadManager;
        this.discountPolicyPersistenceManager = discountPolicyPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(DeleteDiscountPolicyCommand command) {
        DiscountPolicy discountPolicy =
                discountPolicyReadManager.findById(command.discountPolicyId());

        Instant now = Instant.now(clockHolder.getClock());
        DiscountPolicy deleted = discountPolicy.delete(now);

        discountPolicyPersistenceManager.persist(deleted);
    }
}
