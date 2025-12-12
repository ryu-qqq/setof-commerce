package com.ryuqq.setof.application.shippingpolicy.assembler;

import com.ryuqq.setof.application.shippingpolicy.dto.command.RegisterShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResponse;
import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryCost;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryGuide;
import com.ryuqq.setof.domain.shippingpolicy.vo.DisplayOrder;
import com.ryuqq.setof.domain.shippingpolicy.vo.FreeShippingThreshold;
import com.ryuqq.setof.domain.shippingpolicy.vo.PolicyName;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ShippingPolicy Assembler
 *
 * <p>Command DTO와 Domain 객체, Response DTO 간 변환을 담당
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShippingPolicyAssembler {

    /**
     * RegisterShippingPolicyCommand를 ShippingPolicy 도메인으로 변환
     *
     * @param command 등록 커맨드
     * @param now 현재 시각
     * @return ShippingPolicy 도메인 객체
     */
    public ShippingPolicy toDomain(RegisterShippingPolicyCommand command, Instant now) {
        PolicyName policyName = PolicyName.of(command.policyName());
        DeliveryCost defaultDeliveryCost = DeliveryCost.of(command.defaultDeliveryCost());
        FreeShippingThreshold freeShippingThreshold =
                command.freeShippingThreshold() != null
                        ? FreeShippingThreshold.of(command.freeShippingThreshold())
                        : null;
        DeliveryGuide deliveryGuide =
                command.deliveryGuide() != null ? DeliveryGuide.of(command.deliveryGuide()) : null;
        DisplayOrder displayOrder = DisplayOrder.of(command.displayOrder());

        return ShippingPolicy.create(
                command.sellerId(),
                policyName,
                defaultDeliveryCost,
                freeShippingThreshold,
                deliveryGuide,
                command.isDefault(),
                displayOrder,
                now);
    }

    /**
     * ShippingPolicy 도메인을 ShippingPolicyResponse로 변환
     *
     * @param shippingPolicy ShippingPolicy 도메인 객체
     * @return ShippingPolicyResponse
     */
    public ShippingPolicyResponse toResponse(ShippingPolicy shippingPolicy) {
        return ShippingPolicyResponse.of(
                shippingPolicy.getIdValue(),
                shippingPolicy.getSellerId(),
                shippingPolicy.getPolicyNameValue(),
                shippingPolicy.getDefaultDeliveryCostValue(),
                shippingPolicy.getFreeShippingThresholdValue(),
                shippingPolicy.getDeliveryGuideValue(),
                shippingPolicy.isDefault(),
                shippingPolicy.getDisplayOrderValue());
    }

    /**
     * ShippingPolicy 도메인 목록을 ShippingPolicyResponse 목록으로 변환
     *
     * @param shippingPolicies ShippingPolicy 도메인 목록
     * @return ShippingPolicyResponse 목록
     */
    public List<ShippingPolicyResponse> toResponses(List<ShippingPolicy> shippingPolicies) {
        return shippingPolicies.stream().map(this::toResponse).toList();
    }
}
