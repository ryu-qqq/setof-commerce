package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * CreatePaymentInCartV1ApiRequest - 장바구니 구매 결제 요청 DTO.
 *
 * <p>Legacy CreatePaymentInCart 구조와 동일합니다. POST /api/v1/payment/cart 요청 본문입니다.
 * CreatePaymentV1ApiRequest와 동일하지만 orders의 각 항목에 cartId가 포함됩니다.
 *
 * @param payAmount 현금 결제 금액
 * @param mileageAmount 마일리지 사용 금액
 * @param payMethod 결제 수단
 * @param shippingInfo 배송지 정보
 * @param refundAccount 환불 계좌 정보
 * @param orders 장바구니 주문 항목 목록 (cartId 포함)
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "장바구니 구매 결제 요청")
public record CreatePaymentInCartV1ApiRequest(
        @Schema(description = "현금 결제 금액", example = "29800") long payAmount,
        @Schema(description = "마일리지 사용 금액", example = "0") long mileageAmount,
        @Schema(description = "결제 수단", example = "CARD") @NotNull String payMethod,
        @Schema(description = "배송지 정보") @Valid ShippingInfoV1ApiRequest shippingInfo,
        @Schema(description = "환불 계좌 정보") @Valid RefundAccountInfoV1ApiRequest refundAccount,
        @Schema(description = "장바구니 주문 항목 목록") @Valid @NotNull @Size(min = 1)
                List<CreateOrderInCartV1ApiRequest> orders) {}
