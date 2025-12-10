package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 결제 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "결제 응답")
public record PaymentV1ApiResponse(@Schema(
        description = "구매자 정보") @JsonInclude(JsonInclude.Include.NON_NULL) BuyerInfoV1ApiResponse buyerInfo,
        @Schema(description = "결제 상세 정보") PaymentDetailV1ApiResponse payment,
        @Schema(description = "수령인 정보") @JsonInclude(JsonInclude.Include.NON_NULL) ReceiverInfoV1ApiResponse receiverInfo,
        @Schema(description = "환불 계좌 정보") @JsonInclude(JsonInclude.Include.NON_NULL) RefundAccountV1ApiResponse refundAccount,
        @Schema(description = "가상계좌 정보") VBankAccountV1ApiResponse vBankAccount,
        @Schema(description = "주문 상품 목록") Set<OrderProductV1ApiResponse> orderProducts) {

    @Schema(description = "구매자 정보")
    public record BuyerInfoV1ApiResponse(
            @Schema(description = "구매자 이름", example = "홍길동") String buyerName,
            @Schema(description = "구매자 이메일", example = "buyer@example.com") String buyerEmail,
            @Schema(description = "구매자 전화번호", example = "01012345678") String buyerPhoneNumber) {
    }

    @Schema(description = "결제 상세 정보")
    public record PaymentDetailV1ApiResponse(
            @Schema(description = "결제 ID", example = "1") Long paymentId,
            @Schema(description = "결제 대행사 ID", example = "portone_123") String paymentAgencyId,
            @Schema(description = "결제 상태", example = "COMPLETED") String paymentStatus,
            @Schema(description = "결제 방법 Enum", example = "CARD") String paymentMethodEnum,
            @Schema(description = "결제 방법", example = "카드") String paymentMethod,
            @Schema(description = "결제 일시",
                    example = "2024-01-01 00:00:00") @com.fasterxml.jackson.annotation.JsonFormat(
                            pattern = "yyyy-MM-dd HH:mm:ss") java.time.LocalDateTime paymentDate,
            @Schema(description = "취소 일시",
                    example = "2024-01-01 00:00:00") @com.fasterxml.jackson.annotation.JsonFormat(
                            pattern = "yyyy-MM-dd HH:mm:ss") java.time.LocalDateTime canceledDate,
            @Schema(description = "사용자 ID", example = "1") Long userId,
            @Schema(description = "사이트명", example = "SETOF") String siteName,
            @Schema(description = "할인 전 금액", example = "100000") Long preDiscountAmount,
            @Schema(description = "결제 금액", example = "90000") Long paymentAmount,
            @Schema(description = "사용한 마일리지 금액", example = "1000.0") Double usedMileageAmount,
            @Schema(description = "카드명", example = "신한카드") String cardName,
            @Schema(description = "카드 번호", example = "1234-****-****-5678") String cardNumber,
            @Schema(description = "총 예상 마일리지 금액",
                    example = "900.0") Double totalExpectedMileageAmount) {
    }

    @Schema(description = "수령인 정보")
    public record ReceiverInfoV1ApiResponse(
            @Schema(description = "수령인 이름", example = "홍길동") String receiverName,
            @Schema(description = "수령인 전화번호", example = "01012345678") String receiverPhoneNumber,
            @Schema(description = "주소 1", example = "서울시 강남구 테헤란로 123") String addressLine1,
            @Schema(description = "주소 2", example = "124-1234") String addressLine2,
            @Schema(description = "우편번호", example = "12345") String zipCode,
            @Schema(description = "국가 코드", example = "KR") String country,
            @Schema(description = "배송 요청 사항", example = "문 앞에 놔주세요") String deliveryRequest,
            @Schema(description = "전화번호", example = "01012345678") String phoneNumber) {
    }

    @Schema(description = "환불 계좌 정보")
    public record RefundAccountV1ApiResponse(
            @Schema(description = "은행명", example = "신한은행") String bankName,
            @Schema(description = "계좌번호", example = "110123456789") String accountNumber,
            @Schema(description = "환불 계좌 ID", example = "1") Long refundAccountId,
            @Schema(description = "예금주명", example = "홍길동") String accountHolderName) {
    }

    @Schema(description = "가상계좌 정보")
    public record VBankAccountV1ApiResponse(
            @Schema(description = "은행명", example = "신한은행") String bankName,
            @Schema(description = "계좌번호", example = "110123456789") String accountNumber,
            @Schema(description = "결제 금액", example = "90000") Long paymentAmount,
            @Schema(description = "가상계좌 입금 기한",
                    example = "2024-01-31 23:59:59") @com.fasterxml.jackson.annotation.JsonFormat(
                            pattern = "yyyy-MM-dd HH:mm:ss") java.time.LocalDateTime vBankDueDate) {
    }

    @Schema(description = "주문 상품 정보")
    public record OrderProductV1ApiResponse(
            @Schema(description = "결제 ID", example = "1") Long paymentId,
            @Schema(description = "셀러 ID", example = "100") Long sellerId,
            @Schema(description = "주문 ID", example = "1") Long orderId,
            @Schema(description = "브랜드 ID", example = "1") Long brandId,
            @Schema(description = "브랜드명", example = "Nike") String brandName,
            @Schema(description = "상품 그룹 ID", example = "12345") Long productGroupId,
            @Schema(description = "상품 그룹명", example = "멋진 티셔츠") String productGroupName,
            @Schema(description = "상품 ID", example = "123") Long productId,
            @Schema(description = "셀러명", example = "셀러명") String sellerName,
            @Schema(description = "상품 그룹 메인 이미지 URL",
                    example = "https://example.com/image.jpg") String productGroupMainImageUrl,
            @Schema(description = "상품 수량", example = "2") Integer productQuantity,
            @Schema(description = "주문 상태", example = "COMPLETED") String orderStatus,
            @Schema(description = "정가", example = "50000") Long regularPrice,
            @Schema(description = "판매가", example = "39000") Long salePrice,
            @Schema(description = "할인가", example = "11000") Long discountPrice,
            @Schema(description = "직접 할인가", example = "5000") Long directDiscountPrice,
            @Schema(description = "주문 금액", example = "78000") Long orderAmount,
            @Schema(description = "옵션", example = "빨강 M") String option,
            @Schema(description = "총 예상 환불 마일리지 금액",
                    example = "1000.0") Double totalExpectedRefundMileageAmount,
            @Schema(description = "리뷰 작성 여부 (Y/N)", example = "N") String reviewYn,
            @Schema(description = "클레임 거부 여부 (Y/N)", example = "N") String claimRejected) {
    }
}
