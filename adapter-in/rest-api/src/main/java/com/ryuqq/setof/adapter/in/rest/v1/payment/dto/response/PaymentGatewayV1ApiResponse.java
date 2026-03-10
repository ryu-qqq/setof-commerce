package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * PaymentGatewayV1ApiResponse - 결제 준비 응답 DTO.
 *
 * <p>Legacy PaymentGatewayRequestDto와 동일한 구조입니다. 클라이언트는 이 응답의 paymentUniqueId로 PortOne SDK 결제를
 * 진행합니다.
 *
 * @param paymentUniqueId PG사 전달용 결제 고유 키 (예: PAYMENT20240101_12345)
 * @param paymentId 내부 결제 ID
 * @param orderIds 생성된 주문 ID 목록
 * @param expectedMileageAmount 예상 적립 마일리지
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "결제 준비 응답 (PG 결제 전)")
public record PaymentGatewayV1ApiResponse(
        @Schema(description = "PG사 결제 고유 키", example = "PAYMENT20240101_12345")
                String paymentUniqueId,
        @JsonInclude(JsonInclude.Include.NON_NULL)
                @Schema(description = "내부 결제 ID", example = "12345")
                Long paymentId,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) @Schema(description = "생성된 주문 ID 목록")
                List<Long> orderIds,
        @Schema(description = "예상 적립 마일리지", example = "298.0") double expectedMileageAmount) {}
