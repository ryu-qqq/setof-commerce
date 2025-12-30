package com.ryuqq.setof.adapter.in.rest.admin.v1.shippingpolicy.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.shippingpolicy.dto.command.CreateDeliveryNoticeV1ApiRequest;
import com.ryuqq.setof.application.shippingpolicy.dto.command.UpdateShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResponse;
import org.springframework.stereotype.Component;

/**
 * Admin V1 Shippping Policy Mapper
 *
 * <p>Application Response를 Admin V1 API Response로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShippingPolicyAdminV1ApiMapper {

    private static final Integer FREE_SHIPPING_THRESHOLD = 0;

    public UpdateShippingPolicyCommand toUpdateCommand(
            ShippingPolicyResponse shippingPolicyResponse,
            CreateDeliveryNoticeV1ApiRequest request) {

        return new UpdateShippingPolicyCommand(
                shippingPolicyResponse.shippingPolicyId(),
                shippingPolicyResponse.sellerId(),
                shippingPolicyResponse.policyName(),
                request.deliveryFee(),
                FREE_SHIPPING_THRESHOLD,
                shippingPolicyResponse.deliveryGuide(),
                shippingPolicyResponse.displayOrder());
    }
}
