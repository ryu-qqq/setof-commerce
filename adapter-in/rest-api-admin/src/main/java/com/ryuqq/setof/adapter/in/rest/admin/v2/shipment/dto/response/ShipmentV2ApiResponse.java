package com.ryuqq.setof.adapter.in.rest.admin.v2.shipment.dto.response;

import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * 운송장 API 응답 DTO
 *
 * @param shipmentId 운송장 ID
 * @param sellerId 셀러 ID
 * @param checkoutId 결제건 ID
 * @param carrierId 택배사 ID
 * @param invoiceNumber 운송장 번호
 * @param senderName 발송인 이름
 * @param senderPhone 발송인 연락처
 * @param type 배송 유형
 * @param status 배송 상태
 * @param lastLocation 마지막 위치
 * @param lastMessage 마지막 메시지
 * @param shippedAt 발송 시각
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "운송장 응답")
public record ShipmentV2ApiResponse(
        @Schema(description = "운송장 ID", example = "1") Long shipmentId,
        @Schema(description = "셀러 ID", example = "100") Long sellerId,
        @Schema(description = "결제건 ID", example = "50") Long checkoutId,
        @Schema(description = "택배사 ID", example = "1") Long carrierId,
        @Schema(description = "운송장 번호", example = "1234567890123") String invoiceNumber,
        @Schema(description = "발송인 이름", example = "홍길동") String senderName,
        @Schema(description = "발송인 연락처", example = "010-1234-5678") String senderPhone,
        @Schema(description = "배송 유형", example = "NORMAL") String type,
        @Schema(description = "배송 상태", example = "IN_TRANSIT") String status,
        @Schema(description = "마지막 위치", example = "서울 강남 HUB") String lastLocation,
        @Schema(description = "마지막 메시지", example = "배송 출발") String lastMessage,
        @Schema(description = "발송 시각") Instant shippedAt,
        @Schema(description = "생성일시") Instant createdAt,
        @Schema(description = "수정일시") Instant updatedAt) {

    /**
     * Shipment 도메인을 API Response로 변환
     *
     * @param shipment Shipment 도메인
     * @return API 응답 DTO
     */
    public static ShipmentV2ApiResponse from(Shipment shipment) {
        return new ShipmentV2ApiResponse(
                shipment.getIdValue(),
                shipment.getSellerId(),
                shipment.getCheckoutId(),
                shipment.getCarrierId(),
                shipment.getInvoiceNumberValue(),
                shipment.getSenderName(),
                shipment.getSenderPhone(),
                shipment.getTypeValue(),
                shipment.getStatusValue(),
                shipment.getLastLocation(),
                shipment.getLastMessage(),
                shipment.getShippedAt(),
                shipment.getCreatedAt(),
                shipment.getUpdatedAt());
    }
}
