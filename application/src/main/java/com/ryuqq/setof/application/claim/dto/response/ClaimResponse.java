package com.ryuqq.setof.application.claim.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * ClaimResponse - 클레임 응답 DTO
 *
 * @param id DB ID
 * @param claimId 클레임 ID (UUID)
 * @param claimNumber 클레임 번호
 * @param orderId 주문 ID
 * @param orderItemId 주문 상품 ID
 * @param claimType 클레임 유형
 * @param claimReason 클레임 사유
 * @param claimReasonDetail 상세 사유
 * @param quantity 수량
 * @param refundAmount 환불 금액
 * @param status 상태
 * @param processedBy 처리자 ID
 * @param processedAt 처리 일시
 * @param rejectReason 반려 사유
 * @param returnTrackingNumber 반품 송장 번호
 * @param returnCarrier 반품 배송사
 * @param exchangeTrackingNumber 교환품 송장 번호
 * @param exchangeCarrier 교환품 배송사
 * @param returnShippingMethod 반품 배송 방식 (CUSTOMER_SEND, SELLER_PICKUP)
 * @param returnShippingStatus 반품 배송 상태
 * @param returnPickupScheduledAt 수거 예약 일시
 * @param returnPickupAddress 수거지 주소
 * @param returnCustomerPhone 고객 연락처
 * @param returnReceivedAt 반품 수령 일시
 * @param inspectionResult 검수 결과 (PASS, FAIL, PARTIAL)
 * @param inspectionNote 검수 메모
 * @param exchangeShippedAt 교환품 발송 일시
 * @param exchangeDeliveredAt 교환품 수령 일시
 * @param createdAt 생성 일시
 * @param updatedAt 수정 일시
 * @author development-team
 * @since 2.0.0
 */
public record ClaimResponse(
        Long id,
        String claimId,
        String claimNumber,
        String orderId,
        String orderItemId,
        String claimType,
        String claimReason,
        String claimReasonDetail,
        Integer quantity,
        BigDecimal refundAmount,
        String status,
        String processedBy,
        Instant processedAt,
        String rejectReason,
        String returnTrackingNumber,
        String returnCarrier,
        String exchangeTrackingNumber,
        String exchangeCarrier,
        String returnShippingMethod,
        String returnShippingStatus,
        Instant returnPickupScheduledAt,
        String returnPickupAddress,
        String returnCustomerPhone,
        Instant returnReceivedAt,
        String inspectionResult,
        String inspectionNote,
        Instant exchangeShippedAt,
        Instant exchangeDeliveredAt,
        Instant createdAt,
        Instant updatedAt) {}
