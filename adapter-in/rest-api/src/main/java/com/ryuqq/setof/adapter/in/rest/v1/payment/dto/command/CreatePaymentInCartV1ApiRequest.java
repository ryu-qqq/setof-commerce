package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * V1 장바구니 결제 요청
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "장바구니 결제 요청")
public record CreatePaymentInCartV1ApiRequest(
        @Schema(
                        description = "결제 금액",
                        example = "90000",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "결제 금액은 필수입니다.")
                Long payAmount,
        @Schema(description = "마일리지 금액", example = "1000") Long mileageAmount,
        @Schema(
                        description = "결제 방법",
                        example = "CARD",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "결제 방법은 필수입니다.")
                String payMethod,
        @Schema(description = "배송지 정보")
                CreatePaymentV1ApiRequest.ShippingInfoV1ApiRequest shippingInfo,
        @Schema(description = "환불 계좌 정보")
                CreatePaymentV1ApiRequest.RefundAccountInfoV1ApiRequest refundAccount,
        @Schema(description = "주문 목록", requiredMode = Schema.RequiredMode.REQUIRED)
                @Valid
                @NotNull(message = "주문 목록은 필수입니다.")
                @Size(min = 1, message = "적어도 하나 이상의 주문이 필요합니다.")
                List<CreateOrderInCartV1ApiRequest> orders) {

    @Schema(description = "장바구니 주문 생성 요청")
    public record CreateOrderInCartV1ApiRequest(
            @Schema(
                            description = "장바구니 ID",
                            example = "1",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "장바구니 ID는 필수입니다.")
                    Long cartId,
            @Schema(description = "결제 ID", example = "1") Long paymentId,
            @Schema(
                            description = "상품 그룹 ID",
                            example = "12345",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "상품 그룹 ID는 필수입니다.")
                    Long productGroupId,
            @Schema(
                            description = "상품 ID",
                            example = "123",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "상품 ID는 필수입니다.")
                    Long productId,
            @Schema(
                            description = "셀러 ID",
                            example = "100",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "셀러 ID는 필수입니다.")
                    Long sellerId,
            @Schema(
                            description = "상품 수량",
                            example = "2",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "상품 수량은 필수입니다.")
                    @Min(value = 1, message = "상품 수량은 최소 1 입니다.")
                    @Max(value = 999, message = "상품 수량은 999를 넘을 수 없습니다.")
                    Integer quantity,
            @Schema(
                            description = "주문 총 금액",
                            example = "78000",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "주문 총 금액은 필수입니다.")
                    @Min(value = 100, message = "주문 총 금액은 100원 이상입니다.")
                    Long orderAmount) {}
}
