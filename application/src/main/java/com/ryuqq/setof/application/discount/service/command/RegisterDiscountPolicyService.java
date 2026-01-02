package com.ryuqq.setof.application.discount.service.command;

import com.ryuqq.setof.application.discount.dto.command.RegisterDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.factory.command.DiscountPolicyCommandFactory;
import com.ryuqq.setof.application.discount.manager.command.DiscountPolicyPersistenceManager;
import com.ryuqq.setof.application.discount.port.in.command.RegisterDiscountPolicyUseCase;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import org.springframework.stereotype.Service;

/**
 * 할인 정책 등록 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>DiscountPolicyCommandFactory로 DiscountPolicy 도메인 생성 (VO 검증 포함)
 *   <li>DiscountPolicyPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RegisterDiscountPolicyService implements RegisterDiscountPolicyUseCase {

    private final DiscountPolicyCommandFactory discountPolicyCommandFactory;
    private final DiscountPolicyPersistenceManager discountPolicyPersistenceManager;

    public RegisterDiscountPolicyService(
            DiscountPolicyCommandFactory discountPolicyCommandFactory,
            DiscountPolicyPersistenceManager discountPolicyPersistenceManager) {
        this.discountPolicyCommandFactory = discountPolicyCommandFactory;
        this.discountPolicyPersistenceManager = discountPolicyPersistenceManager;
    }

    @Override
    public Long execute(RegisterDiscountPolicyCommand command) {
        DiscountPolicy discountPolicy = discountPolicyCommandFactory.create(command);
        DiscountPolicyId discountPolicyId =
                discountPolicyPersistenceManager.persist(discountPolicy);
        return discountPolicyId.value();
    }
}
