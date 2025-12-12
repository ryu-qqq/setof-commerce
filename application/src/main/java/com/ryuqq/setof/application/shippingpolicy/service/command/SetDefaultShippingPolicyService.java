package com.ryuqq.setof.application.shippingpolicy.service.command;

import com.ryuqq.setof.application.shippingpolicy.dto.command.SetDefaultShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.manager.command.ShippingPolicyPersistenceManager;
import com.ryuqq.setof.application.shippingpolicy.manager.query.ShippingPolicyReadManager;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.SetDefaultShippingPolicyUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 기본 배송 정책 설정 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>기존 기본 정책 조회 및 해제
 *   <li>새로운 기본 정책 설정
 *   <li>변경된 정책들 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class SetDefaultShippingPolicyService implements SetDefaultShippingPolicyUseCase {

    private final ShippingPolicyReadManager shippingPolicyReadManager;
    private final ShippingPolicyPersistenceManager shippingPolicyPersistenceManager;
    private final ClockHolder clockHolder;

    public SetDefaultShippingPolicyService(
            ShippingPolicyReadManager shippingPolicyReadManager,
            ShippingPolicyPersistenceManager shippingPolicyPersistenceManager,
            ClockHolder clockHolder) {
        this.shippingPolicyReadManager = shippingPolicyReadManager;
        this.shippingPolicyPersistenceManager = shippingPolicyPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(SetDefaultShippingPolicyCommand command) {
        Instant now = Instant.now(clockHolder.getClock());

        // 기존 기본 정책 해제
        ShippingPolicy currentDefault =
                shippingPolicyReadManager.findDefaultBySellerId(command.sellerId());
        if (currentDefault != null
                && !currentDefault.getIdValue().equals(command.shippingPolicyId())) {
            ShippingPolicy unsetPolicy = currentDefault.unsetDefault(now);
            shippingPolicyPersistenceManager.persist(unsetPolicy);
        }

        // 새로운 기본 정책 설정
        ShippingPolicy targetPolicy =
                shippingPolicyReadManager.findById(command.shippingPolicyId());
        if (!targetPolicy.isDefault()) {
            ShippingPolicy defaultPolicy = targetPolicy.setAsDefault(now);
            shippingPolicyPersistenceManager.persist(defaultPolicy);
        }
    }
}
