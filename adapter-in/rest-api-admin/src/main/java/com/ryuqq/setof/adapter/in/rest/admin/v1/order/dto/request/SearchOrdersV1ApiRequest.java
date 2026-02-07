package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * SearchOrdersV1ApiRequest - 주문 목록 검색 요청 DTO.
 *
 * <p>레거시 OrderFilter 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "주문 목록 검색 요청")
public record SearchOrdersV1ApiRequest(
        @Parameter(description = "조회 시작일", example = "2025-01-01 00:00:00")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime startDate,
        @Parameter(description = "조회 종료일", example = "2025-01-31 23:59:59")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime endDate,
        @Parameter(
                        description = "검색 키워드 타입",
                        example = "ORDER_ID",
                        schema =
                                @Schema(
                                        allowableValues = {
                                            "ORDER_ID",
                                            "BUYER_NAME",
                                            "BUYER_PHONE",
                                            "PRODUCT_GROUP_ID",
                                            "PRODUCT_GROUP_NAME",
                                            "RECEIVER_NAME",
                                            "RECEIVER_PHONE"
                                        }))
                String searchKeyword,
        @Parameter(description = "검색어", example = "12345") String searchWord,
        @Parameter(description = "마지막 주문 ID (커서 페이징용)", example = "1000") Long lastDomainId,
        @Parameter(
                        description = "주문 상태 목록",
                        example = "[\"ORDER_COMPLETED\", \"DELIVERY_PROCESSING\"]",
                        schema =
                                @Schema(
                                        allowableValues = {
                                            "ORDER_PROCESSING",
                                            "ORDER_COMPLETED",
                                            "ORDER_FAILED",
                                            "DELIVERY_PENDING",
                                            "DELIVERY_PROCESSING",
                                            "DELIVERY_COMPLETED",
                                            "SETTLEMENT_PROCESSING",
                                            "SETTLEMENT_COMPLETED",
                                            "CANCEL_REQUEST",
                                            "CANCEL_REQUEST_CONFIRMED",
                                            "CANCEL_REQUEST_COMPLETED",
                                            "RETURN_REQUEST",
                                            "RETURN_REQUEST_CONFIRMED",
                                            "RETURN_REQUEST_COMPLETED",
                                            "SALE_CANCELLED",
                                            "SALE_CANCELLED_COMPLETED"
                                        }))
                List<String> orderStatusList,
        @NotNull(message = "periodType은 필수입니다.")
                @Parameter(
                        description = "기간 타입",
                        required = true,
                        example = "ORDER",
                        schema = @Schema(allowableValues = {"ORDER", "SETTLEMENT", "HISTORY"}))
                String periodType,
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Parameter(description = "페이지 크기 (1~100)", example = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size) {}
