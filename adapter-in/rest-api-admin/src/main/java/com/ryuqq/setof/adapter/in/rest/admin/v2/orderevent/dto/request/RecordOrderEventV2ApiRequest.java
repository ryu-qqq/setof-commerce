package com.ryuqq.setof.adapter.in.rest.admin.v2.orderevent.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * RecordOrderEventV2ApiRequest - 주문 이벤트 기록 API Request DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "주문 이벤트 기록 요청")
public record RecordOrderEventV2ApiRequest(
        @Schema(
                        description = "주문 ID",
                        example = "550e8400-e29b-41d4-a716-446655440000",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "주문 ID는 필수입니다")
                String orderId,
        @Schema(
                        description = "이벤트 유형",
                        example = "ORDER_CONFIRMED",
                        requiredMode = Schema.RequiredMode.REQUIRED,
                        allowableValues = {
                            "ORDER_CREATED",
                            "ORDER_CONFIRMED",
                            "ORDER_SHIPPED",
                            "ORDER_DELIVERED",
                            "ORDER_COMPLETED",
                            "ORDER_CANCELLED",
                            "PAYMENT_APPROVED",
                            "PAYMENT_CANCELLED",
                            "PAYMENT_REFUNDED",
                            "PAYMENT_PARTIAL_REFUNDED",
                            "CLAIM_REQUESTED",
                            "CLAIM_APPROVED",
                            "CLAIM_REJECTED",
                            "CLAIM_IN_PROGRESS",
                            "CLAIM_COMPLETED",
                            "CLAIM_CANCELLED"
                        })
                @NotBlank(message = "이벤트 유형은 필수입니다")
                String eventType,
        @Schema(
                        description = "이벤트 소스",
                        example = "ORDER",
                        requiredMode = Schema.RequiredMode.REQUIRED,
                        allowableValues = {"ORDER", "CLAIM", "PAYMENT", "SHIPPING"})
                @NotBlank(message = "이벤트 소스는 필수입니다")
                String source,
        @Schema(description = "이벤트 제목", example = "주문 확인됨") String title,
        @Schema(description = "이벤트 상세 내용", example = "판매자가 주문을 확인했습니다.") String description,
        @Schema(description = "관련 ID (클레임 ID 등)", example = "CLM-20241215-000001")
                String referenceId,
        @Schema(
                        description = "행위자 유형",
                        example = "ADMIN",
                        requiredMode = Schema.RequiredMode.REQUIRED,
                        allowableValues = {"CUSTOMER", "SELLER", "ADMIN", "SYSTEM"})
                @NotBlank(message = "행위자 유형은 필수입니다")
                String actorType,
        @Schema(description = "행위자 ID", example = "admin-001") String actorId) {}
