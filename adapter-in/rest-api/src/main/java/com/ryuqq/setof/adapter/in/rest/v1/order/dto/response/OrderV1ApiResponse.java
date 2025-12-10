package com.ryuqq.setof.adapter.in.rest.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 주문 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "주문 응답")
public record OrderV1ApiResponse(
        @Schema(description = "결제 상세 정보") PaymentDetailV1ApiResponse payment,
        @Schema(description = "주문 상품 정보") OrderProductV1ApiResponse orderProduct,
        @Schema(description = "구매자 정보") BuyerInfoV1ApiResponse buyerInfo,
        @Schema(description = "수령인 정보") ReceiverInfoV1ApiResponse receiverInfo,
        @Schema(description = "총 예상 마일리지 금액",
                example = "1000.0") Double totalExpectedMileageAmount) {

    @Schema(description = "결제 상세 정보")
    public record PaymentDetailV1ApiResponse(
            @Schema(description = "결제 ID", example = "1") Long paymentId,
            @Schema(description = "결제 금액", example = "50000") Long paymentAmount,
            @Schema(description = "결제 상태", example = "COMPLETED") String paymentStatus,
            @Schema(description = "결제 방법", example = "CARD") String paymentMethod) {
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
            @Schema(description = "클레임 거부 여부 (Y/N)", example = "N") String claimRejected,
            @Schema(description = "주문 거부 사유") OrderRejectReasonV1ApiResponse orderRejectReason,
            @Schema(description = "배송 정보") PaymentShipmentInfoV1ApiResponse shipmentInfo) {
    }

    @Schema(description = "구매자 정보")
    public record BuyerInfoV1ApiResponse(
            @Schema(description = "구매자 이름", example = "홍길동") String buyerName,
            @Schema(description = "구매자 이메일", example = "buyer@example.com") String buyerEmail,
            @Schema(description = "구매자 전화번호", example = "01012345678") String buyerPhoneNumber) {
    }

    @Schema(description = "수령인 정보")
    public record ReceiverInfoV1ApiResponse(
            @Schema(description = "수령인 이름", example = "홍길동") String receiverName,
            @Schema(description = "수령인 전화번호", example = "01012345678") String receiverPhoneNumber,
            @Schema(description = "수령인 주소", example = "서울시 강남구 테헤란로 123") String receiverAddress) {
    }

    @Schema(description = "주문 거부 사유")
    public record OrderRejectReasonV1ApiResponse(
            @Schema(description = "주문 ID", example = "1") Long orderId,
            @Schema(description = "주문 상태", example = "CANCELLED") String orderStatus,
            @Schema(description = "변경 사유", example = "고객 요청") String changeReason,
            @Schema(description = "변경 상세 사유", example = "단순 변심") String changeDetailReason) {
    }

    @Schema(description = "배송 정보")
    public record PaymentShipmentInfoV1ApiResponse(
            @Schema(description = "송장 번호", example = "1234567890") String invoiceNo,
            @Schema(description = "배송 회사 코드", example = "CJ") String shipmentCompanyCode) {
    }
}
