package com.ryuqq.setof.adapter.in.rest.v1.order.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * SearchOrderCountsV1ApiRequest - 주문 상태별 건수 조회 요청 DTO.
 *
 * <p>레거시 OrderController.fetchOrderCounts 파라미터 기반 변환.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>기본값 처리는 Mapper에서 수행합니다. Request DTO에서는 기본값 설정 금지.
 *
 * @param orderStatuses 조회할 주문 상태 목록 (필수, 1개 이상)
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.order.controller.OrderController
 */
@Schema(description = "주문 상태별 건수 조회 요청")
public record SearchOrderCountsV1ApiRequest(
        @Parameter(
                        description = "조회할 주문 상태 목록 (필수, 복수 선택 가능)",
                        example =
                                "[\"ORDER_PROCESSING\", \"DELIVERY_PENDING\","
                                        + " \"DELIVERY_COMPLETED\"]",
                        required = true,
                        schema =
                                @Schema(
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
                                        }))
                @NotEmpty(message = "조회할 주문 상태를 1개 이상 선택해야 합니다.")
                List<String> orderStatuses) {}
