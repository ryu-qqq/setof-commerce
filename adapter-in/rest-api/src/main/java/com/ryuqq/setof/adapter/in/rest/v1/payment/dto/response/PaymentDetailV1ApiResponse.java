package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response;

import com.ryuqq.setof.adapter.in.rest.v1.order.dto.response.OrderV1ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * PaymentDetailV1ApiResponse - 결제 단건 상세 조회 응답 DTO.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 → Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param paymentId 결제 ID
 * @param paymentStatus 결제 상태
 * @param paymentMethod 결제수단 표시명
 * @param paymentDate 결제 일시
 * @param canceledDate 취소 일시
 * @param paymentAmount 결제 금액
 * @param usedMileageAmount 사용 마일리지
 * @param paymentAgencyId PG사 거래 ID
 * @param cardName 카드사명
 * @param cardNumber 카드번호 (마스킹)
 * @param orderIds 해당 결제에 묶인 주문 ID 목록
 * @param orderProducts 주문 상품 목록 (주문별 상품/옵션/배송/환불 정보)
 * @param vBankAccount 가상계좌 정보 (가상계좌 결제 시에만 포함, nullable)
 * @param buyerInfo 구매자 정보
 * @param receiverInfo 수령인 정보
 * @param refundAccount 환불 계좌 정보 (nullable)
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "결제 단건 상세 응답")
public record PaymentDetailV1ApiResponse(
        @Schema(description = "결제 ID", example = "12345") long paymentId,
        @Schema(
                        description = "결제 상태",
                        example = "PAID",
                        allowableValues = {
                            "PAYMENT_PENDING",
                            "PAID",
                            "PAYMENT_FAILED",
                            "PAYMENT_CANCELED",
                            "REFUND_REQUESTED",
                            "REFUND_COMPLETED"
                        })
                String paymentStatus,
        @Schema(description = "결제수단 표시명", example = "신용카드") String paymentMethod,
        @Schema(description = "결제 일시", example = "2024-01-15T10:30:00") LocalDateTime paymentDate,
        @Schema(description = "취소 일시", nullable = true) LocalDateTime canceledDate,
        @Schema(description = "결제 금액", example = "50000") long paymentAmount,
        @Schema(description = "사용 마일리지", example = "1000.0") double usedMileageAmount,
        @Schema(description = "PG사 거래 ID", example = "imp_123456") String paymentAgencyId,
        @Schema(description = "카드사명", example = "신한카드") String cardName,
        @Schema(description = "카드번호 (마스킹)", example = "1234-****-****-5678") String cardNumber,
        @Schema(description = "해당 결제에 묶인 주문 ID 목록") Set<Long> orderIds,
        @Schema(description = "주문 상품 목록")
                List<OrderV1ApiResponse.OrderProductResponse> orderProducts,
        @Schema(description = "가상계좌 정보", nullable = true) VBankV1ApiResponse vBankAccount,
        @Schema(description = "구매자 정보") BuyerInfoV1ApiResponse buyerInfo,
        @Schema(description = "수령인 정보") ReceiverInfoV1ApiResponse receiverInfo,
        @Schema(description = "환불 계좌 정보", nullable = true)
                RefundAccountV1ApiResponse refundAccount) {

    /**
     * VBankV1ApiResponse - 가상계좌 정보 응답.
     *
     * @param bankName 은행명
     * @param accountNumber 계좌번호
     * @param paymentAmount 입금 금액
     * @param dueDate 입금 기한
     */
    @Schema(description = "가상계좌 정보")
    public record VBankV1ApiResponse(
            @Schema(description = "은행명", example = "KB국민은행") String bankName,
            @Schema(description = "계좌번호", example = "123-456-789012") String accountNumber,
            @Schema(description = "입금 금액", example = "50000") long paymentAmount,
            @Schema(description = "입금 기한", example = "2024-01-18T23:59:59")
                    LocalDateTime dueDate) {}

    /**
     * BuyerInfoV1ApiResponse - 구매자 정보 응답.
     *
     * @param name 구매자명
     * @param email 구매자 이메일
     * @param phoneNumber 구매자 전화번호
     */
    @Schema(description = "구매자 정보")
    public record BuyerInfoV1ApiResponse(
            @Schema(description = "구매자명", example = "홍길동") String name,
            @Schema(description = "구매자 이메일", example = "hong@example.com") String email,
            @Schema(description = "구매자 전화번호", example = "010-1234-5678") String phoneNumber) {}

    /**
     * ReceiverInfoV1ApiResponse - 수령인 정보 응답.
     *
     * @param receiverName 수령인명
     * @param receiverPhoneNumber 수령인 연락처
     * @param addressLine1 주소1
     * @param addressLine2 주소2 (상세주소)
     * @param zipCode 우편번호
     * @param country 국가
     * @param deliveryRequest 배송 요청사항
     * @param phoneNumber 주문자 연락처
     */
    @Schema(description = "수령인 정보")
    public record ReceiverInfoV1ApiResponse(
            @Schema(description = "수령인명", example = "홍길동") String receiverName,
            @Schema(description = "수령인 연락처", example = "010-1234-5678") String receiverPhoneNumber,
            @Schema(description = "주소1", example = "서울시 강남구") String addressLine1,
            @Schema(description = "주소2 (상세주소)", example = "테헤란로 123") String addressLine2,
            @Schema(description = "우편번호", example = "06234") String zipCode,
            @Schema(description = "국가", example = "KOREA") String country,
            @Schema(description = "배송 요청사항", example = "문 앞에 놓아주세요") String deliveryRequest,
            @Schema(description = "주문자 연락처", example = "010-1234-5678") String phoneNumber) {}

    /**
     * RefundAccountV1ApiResponse - 환불 계좌 정보 응답.
     *
     * @param refundAccountId 환불 계좌 ID
     * @param bankName 은행명
     * @param accountNumber 계좌번호
     * @param holderName 예금주명
     */
    @Schema(description = "환불 계좌 정보")
    public record RefundAccountV1ApiResponse(
            @Schema(description = "환불 계좌 ID", example = "100") long refundAccountId,
            @Schema(description = "은행명", example = "KB국민은행") String bankName,
            @Schema(description = "계좌번호", example = "123-456-789012") String accountNumber,
            @Schema(description = "예금주명", example = "홍길동") String holderName) {}
}
