package com.ryuqq.setof.application.claim.assembler;

import com.ryuqq.setof.application.claim.dto.response.ClaimResponse;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import org.springframework.stereotype.Component;

/**
 * ClaimAssembler - Claim 응답 조립기
 *
 * <p>Domain → Response DTO 변환을 담당합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ClaimAssembler {

    /**
     * Domain → Response 변환
     *
     * @param domain Claim 도메인 객체
     * @return 응답 DTO
     */
    public ClaimResponse toResponse(Claim domain) {
        return new ClaimResponse(
                domain.id(),
                domain.claimId().value(),
                domain.claimNumber().value(),
                domain.orderId(),
                domain.orderItemId(),
                domain.claimType().name(),
                domain.claimReason().name(),
                domain.claimReasonDetail(),
                domain.quantity(),
                domain.refundAmount(),
                domain.status().name(),
                domain.processedBy(),
                domain.processedAt(),
                domain.rejectReason(),
                domain.returnTrackingNumber(),
                domain.returnCarrier(),
                domain.exchangeTrackingNumber(),
                domain.exchangeCarrier(),
                toEnumName(domain.returnShippingMethod()),
                toEnumName(domain.returnShippingStatus()),
                domain.returnPickupScheduledAt(),
                domain.returnPickupAddress(),
                domain.returnCustomerPhone(),
                domain.returnReceivedAt(),
                toEnumName(domain.inspectionResult()),
                domain.inspectionNote(),
                domain.exchangeShippedAt(),
                domain.exchangeDeliveredAt(),
                domain.createdAt(),
                domain.updatedAt());
    }

    private String toEnumName(Enum<?> enumValue) {
        return enumValue != null ? enumValue.name() : null;
    }
}
