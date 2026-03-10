package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * CreatePaymentV1ApiRequest - 직접 구매 결제 요청 DTO.
 *
 * <p>Legacy CreatePayment 구조와 동일합니다. POST /api/v1/payment 요청 본문입니다.
 *
 * @param payAmount 현금 결제 금액
 * @param mileageAmount 마일리지 사용 금액
 * @param payMethod 결제 수단 (CARD, KAKAO_PAY, NAVER_PAY, VBANK, VBANK_ESCROW, MILEAGE)
 * @param shippingInfo 배송지 정보
 * @param refundAccount 환불 계좌 정보 (가상계좌 시 필요)
 * @param orders 주문 항목 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "직접 구매 결제 요청")
public record CreatePaymentV1ApiRequest(
        @Schema(description = "현금 결제 금액", example = "29800") long payAmount,
        @Schema(description = "마일리지 사용 금액", example = "1000") long mileageAmount,
        @Schema(description = "결제 수단", example = "CARD") @NotNull String payMethod,
        @Schema(description = "배송지 정보") @Valid ShippingInfoV1ApiRequest shippingInfo,
        @Schema(description = "환불 계좌 정보 (가상계좌 결제 시)") @Valid
                RefundAccountInfoV1ApiRequest refundAccount,
        @Schema(description = "주문 항목 목록") @Valid @NotNull @Size(min = 1)
                List<CreateOrderV1ApiRequest> orders) {}
