package com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.response;

import com.ryuqq.setof.application.claim.dto.response.ClaimResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * ClaimAdminV2ApiResponse - 클레임 Admin API 응답 DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "클레임 관리 응답")
public record ClaimAdminV2ApiResponse(
        @Schema(description = "DB ID", example = "1") Long id,
        @Schema(description = "클레임 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440001")
                String claimId,
        @Schema(description = "클레임 번호", example = "CLM-20241215-000001") String claimNumber,
        @Schema(description = "주문 ID", example = "550e8400-e29b-41d4-a716-446655440000")
                String orderId,
        @Schema(description = "주문 상품 ID", example = "item-001") String orderItemId,
        @Schema(description = "클레임 유형", example = "RETURN") String claimType,
        @Schema(description = "클레임 사유", example = "WRONG_SIZE") String claimReason,
        @Schema(description = "상세 사유", example = "사이즈가 맞지 않습니다") String claimReasonDetail,
        @Schema(description = "클레임 수량", example = "1") Integer quantity,
        @Schema(description = "환불 금액", example = "35000") BigDecimal refundAmount,
        @Schema(description = "클레임 상태", example = "APPROVED") String status,
        @Schema(description = "처리자 ID", example = "admin-001") String processedBy,
        @Schema(description = "처리 일시") Instant processedAt,
        @Schema(description = "반려 사유", example = "반품 기간 초과") String rejectReason,
        @Schema(description = "반품 송장 번호", example = "123456789012") String returnTrackingNumber,
        @Schema(description = "반품 배송사", example = "CJ대한통운") String returnCarrier,
        @Schema(description = "교환품 송장 번호", example = "987654321098") String exchangeTrackingNumber,
        @Schema(description = "교환품 배송사", example = "CJ대한통운") String exchangeCarrier,
        @Schema(description = "반품 배송 방식", example = "SELLER_PICKUP") String returnShippingMethod,
        @Schema(description = "반품 배송 상태", example = "IN_TRANSIT") String returnShippingStatus,
        @Schema(description = "수거 예약 일시") Instant returnPickupScheduledAt,
        @Schema(description = "수거지 주소", example = "서울시 강남구 테헤란로 123") String returnPickupAddress,
        @Schema(description = "고객 연락처", example = "010-1234-5678") String returnCustomerPhone,
        @Schema(description = "반품 수령 일시") Instant returnReceivedAt,
        @Schema(description = "검수 결과", example = "PASS") String inspectionResult,
        @Schema(description = "검수 메모", example = "상품 상태 양호") String inspectionNote,
        @Schema(description = "교환품 발송 일시") Instant exchangeShippedAt,
        @Schema(description = "교환품 수령 일시") Instant exchangeDeliveredAt,
        @Schema(description = "생성 일시") Instant createdAt,
        @Schema(description = "수정 일시") Instant updatedAt) {

    /** Application Response → API Response 변환 */
    public static ClaimAdminV2ApiResponse from(ClaimResponse response) {
        return new ClaimAdminV2ApiResponse(
                response.id(),
                response.claimId(),
                response.claimNumber(),
                response.orderId(),
                response.orderItemId(),
                response.claimType(),
                response.claimReason(),
                response.claimReasonDetail(),
                response.quantity(),
                response.refundAmount(),
                response.status(),
                response.processedBy(),
                response.processedAt(),
                response.rejectReason(),
                response.returnTrackingNumber(),
                response.returnCarrier(),
                response.exchangeTrackingNumber(),
                response.exchangeCarrier(),
                response.returnShippingMethod(),
                response.returnShippingStatus(),
                response.returnPickupScheduledAt(),
                response.returnPickupAddress(),
                response.returnCustomerPhone(),
                response.returnReceivedAt(),
                response.inspectionResult(),
                response.inspectionNote(),
                response.exchangeShippedAt(),
                response.exchangeDeliveredAt(),
                response.createdAt(),
                response.updatedAt());
    }
}
