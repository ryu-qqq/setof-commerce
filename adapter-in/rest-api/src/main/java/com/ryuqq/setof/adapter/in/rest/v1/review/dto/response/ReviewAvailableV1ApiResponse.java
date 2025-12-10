package com.ryuqq.setof.adapter.in.rest.v1.review.dto.response;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰 작성 가능 상품 정보 응답")
public record ReviewAvailableV1ApiResponse(
        @Schema(description = "결제 아이디", example = "1") Long paymentId,
        @Schema(description = "판매자 아이디", example = "1") Long sellerId,
        @Schema(description = "주문 아이디", example = "1") Long orderId,
        @Schema(description = "브랜드 정보") ReviewBrandV1ApiResponse brand,
        @Schema(description = "상품군 아이디", example = "1") Long productGroupId,
        @Schema(description = "상품군 명", example = "스니커즈") String productGroupName,
        @Schema(description = "상품 아이디", example = "1") Long productId,
        @Schema(description = "판매자 명", example = "나이키스토어") String sellerName,
        @Schema(description = "상품군 대표 이미지 URL") String productGroupMainImageUrl,
        @Schema(description = "상품 수량", example = "1") Integer productQuantity,
        @Schema(description = "주문 상태", example = "ORDER_COMPLETED") String orderStatus,
        @Schema(description = "정가", example = "100000") Long regularPrice,
        @Schema(description = "판매가", example = "90000") Long salePrice,
        @Schema(description = "할인가", example = "10000") Long discountPrice,
        @Schema(description = "즉시 할인 금액", example = "5000") Long directDiscountPrice,
        @Schema(description = "주문 금액", example = "85000") Long orderAmount,
        @Schema(description = "옵션 정보", example = "블랙 / 260") String option,
        @Schema(description = "예상 적립 마일리지",
                example = "123.45") Double totalExpectedRefundMileageAmount,
        @Schema(description = "환불 안내") RefundNoticeV1ApiResponse refundNotice,
        @Schema(description = "리뷰 여부", example = "Y") String reviewYn,
        @Schema(description = "클레임 거절 여부", example = "N") String claimRejected,
        @Schema(description = "주문 거절 사유") OrderRejectReasonV1ApiResponse orderRejectReason,
        @Schema(description = "배송 정보") PaymentShipmentInfoV1ApiResponse shipmentInfo,
        @Schema(description = "결제 일시", example = "yyyy-MM-dd HH:mm:ss") LocalDateTime paymentDate) {

    @Schema(description = "리뷰 브랜드 정보")
    public record ReviewBrandV1ApiResponse(
            @Schema(description = "브랜드 아이디", example = "1") Long brandId,
            @Schema(description = "브랜드 명", example = "나이키") String brandName) {
    }

    @Schema(description = "환불 안내")
    public record RefundNoticeV1ApiResponse(
            @Schema(description = "국내 반송 방법") String returnMethodDomestic,
            @Schema(description = "국내 반송 택배사") String returnCourierDomestic,
            @Schema(description = "국내 반송 비용") Integer returnChargeDomestic,
            @Schema(description = "국내 반송 지역") String returnExchangeAreaDomestic) {
    }

    @Schema(description = "주문 거절 사유")
    public record OrderRejectReasonV1ApiResponse(
            @Schema(description = "주문 상태", example = "ORDER_REJECTED") String orderStatus,
            @Schema(description = "변경 사유") String changeReason,
            @Schema(description = "변경 상세 사유") String changeDetailReason,
            @Schema(description = "등록 일시",
                    example = "yyyy-MM-dd HH:mm:ss") LocalDateTime insertDate) {
    }

    @Schema(description = "결제 배송 정보")
    public record PaymentShipmentInfoV1ApiResponse(
            @Schema(description = "주문 아이디", example = "1") Long orderId,
            @Schema(description = "배송 상태", example = "SHIPPING") String deliveryStatus,
            @JsonInclude(JsonInclude.Include.NON_NULL) @Schema(description = "배송사 코드",
                    example = "KR-POST") String shipmentCompanyCode,
            @JsonInclude(JsonInclude.Include.NON_NULL) @Schema(description = "송장 번호",
                    example = "1234567890") String invoice,
            @Schema(description = "배송 7일 이후 여부", example = "N") String isAfter7DaysDelivery,
            @Schema(description = "배송 3개월 이후 여부", example = "N") String isAfter3MonthsDelivery) {
    }
}
