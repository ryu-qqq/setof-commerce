package com.ryuqq.setof.adapter.in.rest.v1.order.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

/**
 * SearchOrdersCursorV1ApiRequest - 주문 목록 검색 요청 DTO (커서 페이징).
 *
 * <p>레거시 OrderFilter 기반 변환.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-010: Request DTO 조회 네이밍 규칙 (Search*CursorApiRequest - 커서 페이징).
 *
 * <p>기본값 처리는 Mapper에서 수행합니다. Request DTO에서는 기본값 설정 금지.
 *
 * @param startDate 검색 시작일
 * @param endDate 검색 종료일
 * @param orderStatuses 주문 상태 필터 (복수 선택 가능)
 * @param lastOrderId 마지막으로 조회한 주문 ID (커서 페이징용)
 * @param size 조회할 아이템 수 (1~100)
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.order.dto.filter.OrderFilter
 */
@Schema(description = "주문 목록 검색 요청 (커서 페이징)")
public record SearchOrdersCursorV1ApiRequest(
        @Parameter(description = "검색 시작일 (yyyy-MM-dd HH:mm:ss)", example = "2024-01-01 00:00:00")
                @Schema(description = "검색 시작일", nullable = true)
                LocalDateTime startDate,
        @Parameter(description = "검색 종료일 (yyyy-MM-dd HH:mm:ss)", example = "2024-12-31 23:59:59")
                @Schema(description = "검색 종료일", nullable = true)
                LocalDateTime endDate,
        @Parameter(
                        description = "주문 상태 필터 (복수 선택 가능)",
                        example = "[\"ORDER_COMPLETED\", \"DELIVERY_PENDING\"]",
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
                                        },
                                        nullable = true))
                List<String> orderStatuses,
        @Parameter(description = "마지막으로 조회한 주문 ID (다음 페이지 조회 시 사용)", example = "1000")
                @Schema(description = "커서: 마지막 주문 ID", nullable = true)
                Long lastOrderId,
        @Parameter(description = "조회할 아이템 수 (1~100)", example = "20")
                @Schema(description = "페이지 크기", defaultValue = "20")
                @Min(value = 1, message = "조회 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "조회 크기는 100 이하여야 합니다.")
                Integer size) {}
