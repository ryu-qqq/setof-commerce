package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * PaymentDetailV1ApiResponse - 결제 상세 조회 응답 DTO.
 *
 * <p>레거시 PaymentResponse 기반 변환 (상세 조회용 전체 버전).
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param buyerInfo 구매자 정보
 * @param payment 결제 상세 정보
 * @param receiverInfo 수령인 정보
 * @param refundAccount 환불 계좌 정보
 * @param vBankAccount 가상계좌 정보
 * @param orderProducts 주문 상품 목록
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.payment.dto.payment.PaymentResponse
 */
@Schema(description = "결제 상세 응답")
public record PaymentDetailV1ApiResponse(
        @Schema(description = "구매자 정보") BuyerInfoResponse buyerInfo,
        @Schema(description = "결제 상세 정보") PaymentResponse payment,
        @Schema(description = "수령인 정보") ReceiverInfoResponse receiverInfo,
        @Schema(description = "환불 계좌 정보", nullable = true) RefundAccountResponse refundAccount,
        @Schema(description = "가상계좌 정보", nullable = true) VBankAccountResponse vBankAccount,
        @Schema(description = "주문 상품 목록") Set<OrderProductResponse> orderProducts) {

    /**
     * BuyerInfoResponse - 구매자 정보 응답.
     *
     * @param buyerName 구매자명
     * @param buyerTel 구매자 연락처
     * @param buyerEmail 구매자 이메일
     */
    @Schema(description = "구매자 정보")
    public record BuyerInfoResponse(
            @Schema(description = "구매자명", example = "홍길동") String buyerName,
            @Schema(description = "구매자 연락처", example = "010-1234-5678") String buyerTel,
            @Schema(description = "구매자 이메일", example = "hong@example.com") String buyerEmail) {}

    /**
     * PaymentResponse - 결제 정보 응답.
     *
     * @param paymentId 결제 ID
     * @param paymentAgencyId PG사 주문번호
     * @param paymentStatus 결제 상태
     * @param paymentMethod 결제 수단 (표시명)
     * @param paymentDate 결제 일시
     * @param canceledDate 취소 일시
     * @param siteName 사이트명
     * @param preDiscountAmount 할인 전 금액
     * @param paymentAmount 결제 금액
     * @param usedMileageAmount 사용 마일리지
     * @param cardName 카드명
     * @param cardNumber 카드번호 (마스킹)
     * @param totalExpectedMileageAmount 예상 적립 마일리지
     */
    @Schema(description = "결제 정보")
    public record PaymentResponse(
            @Schema(description = "결제 ID", example = "12345") long paymentId,
            @Schema(description = "PG사 주문번호", example = "imp_123456") String paymentAgencyId,
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
            @Schema(description = "결제 수단", example = "신용카드") String paymentMethod,
            @Schema(description = "결제 일시", example = "2024-01-15T10:30:00")
                    LocalDateTime paymentDate,
            @Schema(description = "취소 일시", nullable = true) LocalDateTime canceledDate,
            @Schema(description = "사이트명", example = "SETOF") String siteName,
            @Schema(description = "할인 전 금액", example = "60000") long preDiscountAmount,
            @Schema(description = "결제 금액", example = "50000") long paymentAmount,
            @Schema(description = "사용 마일리지", example = "1000.0") double usedMileageAmount,
            @Schema(description = "카드명", example = "신한카드") String cardName,
            @Schema(description = "카드번호 (마스킹)", example = "1234-****-****-5678") String cardNumber,
            @Schema(description = "예상 적립 마일리지", example = "500.0")
                    double totalExpectedMileageAmount) {}

    /**
     * ReceiverInfoResponse - 수령인 정보 응답.
     *
     * @param receiverName 수령인명
     * @param receiverPhoneNumber 수령인 연락처
     * @param addressLine1 주소1
     * @param addressLine2 주소2 (상세주소)
     * @param zipCode 우편번호
     * @param country 국가
     * @param deliveryRequest 배송 요청사항
     * @param phoneNumber 연락처 (사용자 전화번호)
     */
    @Schema(description = "수령인 정보")
    public record ReceiverInfoResponse(
            @Schema(description = "수령인명", example = "홍길동") String receiverName,
            @Schema(description = "수령인 연락처", example = "010-1234-5678") String receiverPhoneNumber,
            @Schema(description = "주소1", example = "서울시 강남구") String addressLine1,
            @Schema(description = "주소2 (상세주소)", example = "테헤란로 123") String addressLine2,
            @Schema(description = "우편번호", example = "06234") String zipCode,
            @Schema(description = "국가", example = "KOREA") String country,
            @Schema(description = "배송 요청사항", example = "문 앞에 놓아주세요") String deliveryRequest,
            @Schema(description = "연락처", example = "010-1234-5678") String phoneNumber) {}

    /**
     * RefundAccountResponse - 환불 계좌 정보 응답.
     *
     * @param bankName 은행명
     * @param accountNumber 계좌번호
     * @param refundAccountId 환불 계좌 ID
     * @param accountHolderName 예금주명
     */
    @Schema(description = "환불 계좌 정보")
    public record RefundAccountResponse(
            @Schema(description = "은행명", example = "KB국민은행") String bankName,
            @Schema(description = "계좌번호", example = "123-456-789012") String accountNumber,
            @Schema(description = "환불 계좌 ID", example = "100") long refundAccountId,
            @Schema(description = "예금주명", example = "홍길동") String accountHolderName) {}

    /**
     * VBankAccountResponse - 가상계좌 정보 응답.
     *
     * @param bankName 은행명
     * @param accountNumber 계좌번호
     * @param paymentAmount 입금 금액
     * @param vBankDueDate 입금 기한
     */
    @Schema(description = "가상계좌 정보")
    public record VBankAccountResponse(
            @Schema(description = "은행명", example = "KB국민은행") String bankName,
            @Schema(description = "계좌번호", example = "123-456-789012") String accountNumber,
            @Schema(description = "입금 금액", example = "50000") long paymentAmount,
            @Schema(description = "입금 기한", example = "2024-01-18T23:59:59")
                    LocalDateTime vBankDueDate) {}

    /**
     * OrderProductResponse - 주문 상품 정보 응답.
     *
     * @param orderId 주문 ID
     * @param productGroupId 상품그룹 ID
     * @param productGroupName 상품그룹명
     * @param productId 상품(SKU) ID
     * @param productGroupMainImageUrl 대표 이미지 URL
     * @param quantity 수량
     * @param orderStatus 주문 상태
     * @param regularPrice 정가
     * @param orderAmount 주문 금액
     * @param totalSaleAmount 총 할인 금액
     * @param rejectReason 거절 사유 (거절된 경우에만)
     */
    @Schema(description = "주문 상품 정보")
    public record OrderProductResponse(
            @Schema(description = "주문 ID", example = "67890") long orderId,
            @Schema(description = "상품그룹 ID", example = "200") long productGroupId,
            @Schema(description = "상품그룹명", example = "에어맥스 90") String productGroupName,
            @Schema(description = "상품(SKU) ID", example = "300") long productId,
            @Schema(description = "대표 이미지 URL", example = "https://cdn.example.com/image.jpg")
                    String productGroupMainImageUrl,
            @Schema(description = "수량", example = "2") int quantity,
            @Schema(description = "주문 상태", example = "DELIVERY_PENDING") String orderStatus,
            @Schema(description = "정가", example = "150000") long regularPrice,
            @Schema(description = "주문 금액", example = "120000") long orderAmount,
            @Schema(description = "총 할인 금액", example = "30000") long totalSaleAmount,
            @Schema(description = "거절 사유", nullable = true) String rejectReason) {}
}
