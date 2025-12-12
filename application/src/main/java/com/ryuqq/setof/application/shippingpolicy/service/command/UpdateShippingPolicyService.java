package com.ryuqq.setof.application.shippingpolicy.service.command;

import com.ryuqq.setof.application.shippingpolicy.dto.command.UpdateShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.manager.command.ShippingPolicyPersistenceManager;
import com.ryuqq.setof.application.shippingpolicy.manager.query.ShippingPolicyReadManager;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.UpdateShippingPolicyUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import com.ryuqq.setof.domain.shippingpolicy.exception.ShippingPolicyNotOwnerException;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryCost;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryGuide;
import com.ryuqq.setof.domain.shippingpolicy.vo.DisplayOrder;
import com.ryuqq.setof.domain.shippingpolicy.vo.FreeShippingThreshold;
import com.ryuqq.setof.domain.shippingpolicy.vo.PolicyName;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 배송 정책 수정 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>ShippingPolicyReadManager로 기존 배송 정책 조회
 *   <li>VO 생성 및 update 메서드 호출
 *   <li>ShippingPolicyPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateShippingPolicyService implements UpdateShippingPolicyUseCase {

    private final ShippingPolicyReadManager shippingPolicyReadManager;
    private final ShippingPolicyPersistenceManager shippingPolicyPersistenceManager;
    private final ClockHolder clockHolder;

    public UpdateShippingPolicyService(
            ShippingPolicyReadManager shippingPolicyReadManager,
            ShippingPolicyPersistenceManager shippingPolicyPersistenceManager,
            ClockHolder clockHolder) {
        this.shippingPolicyReadManager = shippingPolicyReadManager;
        this.shippingPolicyPersistenceManager = shippingPolicyPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(UpdateShippingPolicyCommand command) {
        ShippingPolicy existingPolicy =
                shippingPolicyReadManager.findById(command.shippingPolicyId());

        // 소유권 검증: 요청한 sellerId와 정책의 sellerId가 일치하는지 확인
        if (!existingPolicy.getSellerId().equals(command.sellerId())) {
            throw new ShippingPolicyNotOwnerException(
                    command.shippingPolicyId(), command.sellerId());
        }

        PolicyName policyName = PolicyName.of(command.policyName());
        DeliveryCost defaultDeliveryCost = DeliveryCost.of(command.defaultDeliveryCost());
        FreeShippingThreshold freeShippingThreshold =
                command.freeShippingThreshold() != null
                        ? FreeShippingThreshold.of(command.freeShippingThreshold())
                        : null;
        DeliveryGuide deliveryGuide =
                command.deliveryGuide() != null ? DeliveryGuide.of(command.deliveryGuide()) : null;
        DisplayOrder displayOrder = DisplayOrder.of(command.displayOrder());

        ShippingPolicy updatedPolicy =
                existingPolicy.update(
                        policyName,
                        defaultDeliveryCost,
                        freeShippingThreshold,
                        deliveryGuide,
                        displayOrder,
                        Instant.now(clockHolder.getClock()));

        shippingPolicyPersistenceManager.persist(updatedPolicy);
    }
}
