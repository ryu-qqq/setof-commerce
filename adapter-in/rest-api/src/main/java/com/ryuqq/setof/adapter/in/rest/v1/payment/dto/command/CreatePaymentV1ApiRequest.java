package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * V1 일반 결제 요청
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "일반 결제 요청")
public record CreatePaymentV1ApiRequest(
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
        @Schema(description = "배송지 정보") ShippingInfoV1ApiRequest shippingInfo,
        @Schema(description = "환불 계좌 정보") RefundAccountInfoV1ApiRequest refundAccount,
        @Schema(description = "주문 목록", requiredMode = Schema.RequiredMode.REQUIRED)
                @Valid
                @NotNull(message = "주문 목록은 필수입니다.")
                List<CreateOrderV1ApiRequest> orders) {

    @Schema(description = "배송지 정보")
    public record ShippingInfoV1ApiRequest(
            @Schema(description = "배송지 ID", example = "1") Long shippingAddressId,
            @Schema(description = "배송지 상세 정보") ShippingDetailsV1ApiRequest shippingDetails,
            @Schema(description = "기본 배송지 여부 (Y/N)", example = "Y") String defaultYn) {

        @Schema(description = "배송지 상세 정보")
        public record ShippingDetailsV1ApiRequest(
                @Schema(description = "수령인 이름", example = "홍길동") String receiverName,
                @Schema(description = "배송지명", example = "집") String shippingAddressName,
                @Schema(description = "상세 주소 1", example = "서울시 강남구 테헤란로 123") String addressLine1,
                @Schema(description = "상세 주소 2", example = "124-1234") String addressLine2,
                @Schema(description = "우편번호", example = "12345") String zipCode,
                @Schema(description = "국가 코드", example = "KR") String country,
                @Schema(description = "배송 요청 사항", example = "문 앞에 놔주세요") String deliveryRequest,
                @Schema(description = "수취인 핸드폰 번호", example = "01011111111") String phoneNumber) {}
    }

    @Schema(description = "환불 계좌 정보")
    public record RefundAccountInfoV1ApiRequest(
            @Schema(description = "은행명", example = "신한은행") String bankName,
            @Schema(description = "계좌번호", example = "110123456789") String accountNumber,
            @Schema(description = "예금주명", example = "홍길동") String accountHolderName) {}

    @Schema(description = "주문 생성 요청")
    public record CreateOrderV1ApiRequest(
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
