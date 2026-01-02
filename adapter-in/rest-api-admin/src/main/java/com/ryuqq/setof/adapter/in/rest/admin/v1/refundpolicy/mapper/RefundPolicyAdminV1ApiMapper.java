package com.ryuqq.setof.adapter.in.rest.admin.v1.refundpolicy.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.refundpolicy.dto.command.CreateRefundNoticeV1ApiRequest;
import com.ryuqq.setof.application.refundpolicy.dto.command.UpdateRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResponse;
import org.springframework.stereotype.Component;

/**
 * Admin V1 Refund Policy Mapper
 *
 * <p>Application Response를 Admin V1 API Response로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefundPolicyAdminV1ApiMapper {

    public UpdateRefundPolicyCommand toUpdateRefundPolicyCommand(
            RefundPolicyResponse refundPolicyResponse, CreateRefundNoticeV1ApiRequest request) {

        return new UpdateRefundPolicyCommand(
                refundPolicyResponse.refundPolicyId(),
                refundPolicyResponse.sellerId(),
                refundPolicyResponse.policyName(),
                refundPolicyResponse.returnAddressLine1(),
                refundPolicyResponse.returnAddressLine2(),
                refundPolicyResponse.returnZipCode(),
                refundPolicyResponse.refundPeriodDays(),
                request.returnChargeDomestic(),
                refundPolicyResponse.refundGuide());
    }
}
