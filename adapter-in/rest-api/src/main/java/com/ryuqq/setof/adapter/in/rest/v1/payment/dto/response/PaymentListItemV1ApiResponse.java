package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderV1ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * PaymentListItemV1ApiResponse - 결제 목록 개별 아이템 응답 DTO (레거시 PaymentResponse 호환).
 *
 * <p>레거시 응답 구조: {@code {payment: {...}, vBankAccount: {...}, orderProducts: [...]}}
 *
 * <p>buyerInfo, receiverInfo, refundAccount는 목록 조회 시 null이므로 {@code @JsonInclude(NON_NULL)}로 제외됩니다.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * @param payment 결제 상세 정보
 * @param vBankAccount 가상계좌 정보
 * @param orderProducts 주문 상품 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "결제 목록 아이템 응답 (레거시 호환)")
public record PaymentListItemV1ApiResponse(
        @Schema(description = "결제 상세 정보") PaymentInfo payment,
        @Schema(description = "가상계좌 정보") VBankAccountInfo vBankAccount,
        @Schema(description = "주문 상품 목록")
                List<OrderV1ApiResponse.OrderProductResponse> orderProducts) {

    /**
     * 결제 상세 정보 (레거시 PaymentDetail 호환).
     *
     * @param paymentId 결제 ID
     * @param paymentAgencyId PG사 거래 ID
     * @param paymentStatus 결제 상태
     * @param paymentMethodEnum 결제수단 Enum명
     * @param paymentMethod 결제수단 표시명
     * @param paymentDate 결제 일시
     * @param canceledDate 취소 일시
     * @param userId 사용자 ID
     * @param siteName 사이트명
     * @param preDiscountAmount 할인 전 금액
     * @param paymentAmount 결제 금액
     * @param usedMileageAmount 사용 마일리지
     * @param cardName 카드사명
     * @param cardNumber 카드번호
     * @param totalExpectedMileageAmount 예상 적립 마일리지
     */
    @Schema(description = "결제 상세 정보")
    public record PaymentInfo(
            @Schema(description = "결제 ID", example = "12345") long paymentId,
            @Schema(description = "PG사 거래 ID") String paymentAgencyId,
            @Schema(description = "결제 상태") String paymentStatus,
            @Schema(description = "결제수단 Enum") String paymentMethodEnum,
            @Schema(description = "결제수단 표시명") String paymentMethod,
            @Schema(description = "결제 일시") String paymentDate,
            @Schema(description = "취소 일시", nullable = true) String canceledDate,
            @Schema(description = "사용자 ID") long userId,
            @Schema(description = "사이트명") @JsonInclude(JsonInclude.Include.NON_NULL)
                    String siteName,
            @Schema(description = "할인 전 금액") long preDiscountAmount,
            @Schema(description = "결제 금액") long paymentAmount,
            @Schema(description = "사용 마일리지") double usedMileageAmount,
            @Schema(description = "카드사명") String cardName,
            @Schema(description = "카드번호") String cardNumber,
            @Schema(description = "예상 적립 마일리지") double totalExpectedMileageAmount) {}

    /**
     * 가상계좌 정보 (레거시 VBankAccountResponse 호환).
     *
     * @param bankName 은행명
     * @param accountNumber 계좌번호
     * @param paymentAmount 입금 금액
     * @param vBankDueDate 입금 기한
     */
    @Schema(description = "가상계좌 정보")
    public record VBankAccountInfo(
            @Schema(description = "은행명") String bankName,
            @Schema(description = "계좌번호") String accountNumber,
            @Schema(description = "입금 금액") long paymentAmount,
            @Schema(description = "입금 기한", nullable = true) String vBankDueDate) {}
}
