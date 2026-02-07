package com.ryuqq.setof.adapter.in.rest.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * OrderCountV1ApiResponse - 주문 상태별 건수 응답 DTO.
 *
 * <p>레거시 OrderCountDto 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param orderStatus 주문 상태
 * @param count 해당 상태의 건수
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.order.dto.OrderCountDto
 */
@Schema(description = "주문 상태별 건수 응답")
public record OrderCountV1ApiResponse(
        @Schema(
                        description = "주문 상태",
                        example = "ORDER_PROCESSING",
                        allowableValues = {
                            "ORDER_FAILED",
                            "ORDER_PROCESSING",
                            "ORDER_COMPLETED",
                            "DELIVERY_PENDING",
                            "DELIVERY_PROCESSING",
                            "DELIVERY_COMPLETED",
                            "CANCEL_REQUEST",
                            "CANCEL_REQUEST_RECANT",
                            "CANCEL_REQUEST_REJECTED",
                            "CANCEL_REQUEST_CONFIRMED",
                            "SALE_CANCELLED",
                            "RETURN_REQUEST",
                            "RETURN_DELIVERY_PROCESSING",
                            "RETURN_REQUEST_CONFIRMED",
                            "RETURN_REQUEST_RECANT",
                            "RETURN_REQUEST_REJECTED",
                            "CANCEL_REQUEST_COMPLETED",
                            "SALE_CANCELLED_COMPLETED",
                            "RETURN_REQUEST_COMPLETED",
                            "SETTLEMENT_PROCESSING",
                            "SETTLEMENT_COMPLETED"
                        })
                String orderStatus,
        @Schema(description = "해당 상태의 건수", example = "5") long count) {}
