package com.ryuqq.setof.adapter.in.rest.v1.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * UpdateOrderV1ApiRequest - 주문 상태 변경 요청 DTO.
 *
 * <p>Legacy 다형성 구조(NormalOrder/ClaimOrder/RefundOrder)를 단일 flat record로 수용합니다. type 필드와 orderStatus
 * 조합으로 V1 Router가 적절한 처리를 수행합니다.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * @param type 주문 유형 (normalOrder, claimOrder, refundOrder)
 * @param paymentId 결제 ID
 * @param orderId 주문 ID (nullable - ORDER_FAILED 시 null 가능)
 * @param orderStatus 목표 주문 상태
 * @param saveOrderSnapShot 주문 스냅샷 저장 여부 (normalOrder 전용)
 * @param changeReason 변경 사유 (claimOrder/refundOrder 전용)
 * @param changeDetailReason 변경 상세 사유 (claimOrder/refundOrder 전용)
 * @param paymentAgencyId PG사 결제 ID (refundOrder 전용)
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "주문 상태 변경 요청")
public record UpdateOrderV1ApiRequest(
        @Schema(description = "주문 유형", example = "normalOrder") @NotNull String type,
        @Schema(description = "결제 ID", example = "1001") long paymentId,
        @Schema(description = "주문 ID", example = "5001") Long orderId,
        @Schema(description = "목표 주문 상태", example = "CANCEL_REQUEST") @NotNull String orderStatus,
        @Schema(description = "주문 스냅샷 저장 여부") Boolean saveOrderSnapShot,
        @Schema(description = "변경 사유") String changeReason,
        @Schema(description = "변경 상세 사유") String changeDetailReason,
        @Schema(description = "PG사 결제 ID") String paymentAgencyId) {}
