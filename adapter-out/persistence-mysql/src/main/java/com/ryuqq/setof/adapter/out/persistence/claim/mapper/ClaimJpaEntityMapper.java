package com.ryuqq.setof.adapter.out.persistence.claim.mapper;

import com.ryuqq.setof.adapter.out.persistence.claim.entity.ClaimJpaEntity;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.claim.vo.ClaimId;
import com.ryuqq.setof.domain.claim.vo.ClaimNumber;
import com.ryuqq.setof.domain.claim.vo.ClaimReason;
import com.ryuqq.setof.domain.claim.vo.ClaimStatus;
import com.ryuqq.setof.domain.claim.vo.ClaimType;
import com.ryuqq.setof.domain.claim.vo.InspectionResult;
import com.ryuqq.setof.domain.claim.vo.ReturnShippingMethod;
import com.ryuqq.setof.domain.claim.vo.ReturnShippingStatus;
import org.springframework.stereotype.Component;

/**
 * ClaimJpaEntityMapper - Claim 엔티티 매퍼
 *
 * <p>Domain ↔ JPA Entity 변환을 담당합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ClaimJpaEntityMapper {

    /**
     * Domain → JPA Entity 변환
     *
     * @param domain Claim 도메인 객체
     * @return JPA Entity
     */
    public ClaimJpaEntity toEntity(Claim domain) {
        return ClaimJpaEntity.of(
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
                domain.createdAt(),
                domain.updatedAt(),
                domain.returnShippingMethod() != null ? domain.returnShippingMethod().name() : null,
                domain.returnShippingStatus() != null ? domain.returnShippingStatus().name() : null,
                domain.returnPickupScheduledAt(),
                domain.returnPickupAddress(),
                domain.returnCustomerPhone(),
                domain.returnReceivedAt(),
                domain.inspectionResult() != null ? domain.inspectionResult().name() : null,
                domain.inspectionNote(),
                domain.exchangeShippedAt(),
                domain.exchangeDeliveredAt());
    }

    /**
     * JPA Entity → Domain 변환
     *
     * @param entity JPA Entity
     * @return Claim 도메인 객체
     */
    public Claim toDomain(ClaimJpaEntity entity) {
        return Claim.restore(
                entity.getId(),
                ClaimId.of(entity.getClaimId()),
                ClaimNumber.of(entity.getClaimNumber()),
                entity.getOrderId(),
                entity.getOrderItemId(),
                ClaimType.valueOf(entity.getClaimType()),
                ClaimReason.valueOf(entity.getClaimReason()),
                entity.getClaimReasonDetail(),
                entity.getQuantity(),
                entity.getRefundAmount(),
                ClaimStatus.valueOf(entity.getStatus()),
                entity.getProcessedBy(),
                entity.getProcessedAt(),
                entity.getRejectReason(),
                entity.getReturnTrackingNumber(),
                entity.getReturnCarrier(),
                entity.getExchangeTrackingNumber(),
                entity.getExchangeCarrier(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                toReturnShippingMethod(entity.getReturnShippingMethod()),
                toReturnShippingStatus(entity.getReturnShippingStatus()),
                entity.getReturnPickupScheduledAt(),
                entity.getReturnPickupAddress(),
                entity.getReturnCustomerPhone(),
                entity.getReturnReceivedAt(),
                toInspectionResult(entity.getInspectionResult()),
                entity.getInspectionNote(),
                entity.getExchangeShippedAt(),
                entity.getExchangeDeliveredAt());
    }

    private ReturnShippingMethod toReturnShippingMethod(String value) {
        return value != null ? ReturnShippingMethod.valueOf(value) : null;
    }

    private ReturnShippingStatus toReturnShippingStatus(String value) {
        return value != null ? ReturnShippingStatus.valueOf(value) : null;
    }

    private InspectionResult toInspectionResult(String value) {
        return value != null ? InspectionResult.valueOf(value) : null;
    }

    /**
     * Domain 변경사항을 기존 Entity에 적용
     *
     * @param entity 기존 JPA Entity
     * @param domain 변경된 Domain 객체
     */
    public void updateEntity(ClaimJpaEntity entity, Claim domain) {
        // 기본 상태 업데이트
        entity.updateStatus(domain.status().name());
        entity.updateProcessInfo(domain.processedBy(), domain.processedAt(), domain.rejectReason());

        // 반품 배송 정보 업데이트
        updateReturnShippingFields(entity, domain);

        // 교환 배송 정보 업데이트
        updateExchangeShippingFields(entity, domain);
    }

    private void updateReturnShippingFields(ClaimJpaEntity entity, Claim domain) {
        String shippingMethod =
                domain.returnShippingMethod() != null ? domain.returnShippingMethod().name() : null;
        String shippingStatus =
                domain.returnShippingStatus() != null ? domain.returnShippingStatus().name() : null;
        String inspectionResultValue =
                domain.inspectionResult() != null ? domain.inspectionResult().name() : null;

        // 방문수거 예약 정보
        entity.updateReturnPickupSchedule(
                shippingMethod,
                shippingStatus,
                domain.returnPickupScheduledAt(),
                domain.returnPickupAddress(),
                domain.returnCustomerPhone());

        // 반품 배송 정보 (송장 번호)
        entity.updateReturnShippingInfo(
                shippingMethod,
                shippingStatus,
                domain.returnTrackingNumber(),
                domain.returnCarrier());

        // 반품 수령 및 검수 결과
        entity.updateReturnReceived(
                shippingStatus,
                domain.returnReceivedAt(),
                inspectionResultValue,
                domain.inspectionNote());
    }

    private void updateExchangeShippingFields(ClaimJpaEntity entity, Claim domain) {
        // 교환품 발송 정보
        entity.updateExchangeShipped(
                domain.exchangeTrackingNumber(),
                domain.exchangeCarrier(),
                domain.exchangeShippedAt());

        // 교환품 수령 확인
        entity.updateExchangeDelivered(domain.exchangeDeliveredAt());
    }
}
