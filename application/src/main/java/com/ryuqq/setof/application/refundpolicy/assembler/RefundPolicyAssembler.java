package com.ryuqq.setof.application.refundpolicy.assembler;

import com.ryuqq.setof.application.refundpolicy.dto.command.RegisterRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResponse;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.vo.PolicyName;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundDeliveryCost;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundGuide;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPeriodDays;
import com.ryuqq.setof.domain.refundpolicy.vo.ReturnAddress;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * RefundPolicy Assembler
 *
 * <p>Command DTO와 Domain 객체, Response DTO 간 변환을 담당
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefundPolicyAssembler {

    /**
     * RegisterRefundPolicyCommand를 RefundPolicy 도메인으로 변환
     *
     * @param command 등록 커맨드
     * @param now 현재 시각
     * @return RefundPolicy 도메인 객체
     */
    public RefundPolicy toDomain(RegisterRefundPolicyCommand command, Instant now) {
        PolicyName policyName = PolicyName.of(command.policyName());
        ReturnAddress returnAddress =
                ReturnAddress.of(
                        command.returnAddressLine1(),
                        command.returnAddressLine2(),
                        command.returnZipCode());
        RefundPeriodDays refundPeriodDays = RefundPeriodDays.of(command.refundPeriodDays());
        RefundDeliveryCost refundDeliveryCost = RefundDeliveryCost.of(command.refundDeliveryCost());
        RefundGuide refundGuide =
                command.refundGuide() != null ? RefundGuide.of(command.refundGuide()) : null;

        return RefundPolicy.create(
                command.sellerId(),
                policyName,
                returnAddress,
                refundPeriodDays,
                refundDeliveryCost,
                refundGuide,
                command.isDefault(),
                command.displayOrder(),
                now);
    }

    /**
     * RefundPolicy 도메인을 RefundPolicyResponse로 변환
     *
     * @param refundPolicy RefundPolicy 도메인 객체
     * @return RefundPolicyResponse
     */
    public RefundPolicyResponse toResponse(RefundPolicy refundPolicy) {
        return RefundPolicyResponse.of(
                refundPolicy.getIdValue(),
                refundPolicy.getSellerId(),
                refundPolicy.getPolicyNameValue(),
                refundPolicy.getReturnAddressLine1(),
                refundPolicy.getReturnAddressLine2(),
                refundPolicy.getReturnZipCode(),
                refundPolicy.getRefundPeriodDaysValue(),
                refundPolicy.getRefundDeliveryCostValue(),
                refundPolicy.getRefundGuideValue(),
                refundPolicy.isDefault(),
                refundPolicy.getDisplayOrder());
    }

    /**
     * RefundPolicy 도메인 목록을 RefundPolicyResponse 목록으로 변환
     *
     * @param refundPolicies RefundPolicy 도메인 목록
     * @return RefundPolicyResponse 목록
     */
    public List<RefundPolicyResponse> toResponses(List<RefundPolicy> refundPolicies) {
        return refundPolicies.stream().map(this::toResponse).toList();
    }
}
