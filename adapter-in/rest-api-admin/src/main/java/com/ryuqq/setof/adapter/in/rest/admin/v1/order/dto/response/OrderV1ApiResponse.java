package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 주문 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "주문 응답")
public record OrderV1ApiResponse(@Schema(description = "주문 ID", example = "1") Long orderId,
        @Schema(description = "구매자 정보") BuyerInfoV1ApiResponse buyerInfo,
        @Schema(description = "결제 상세 정보") PaymentDetailV1ApiResponse payment,
        @Schema(description = "수령인 정보") ReceiverInfoV1ApiResponse receiverInfo,
        @Schema(description = "배송 정보") PaymentShipmentInfoV1ApiResponse paymentShipmentInfo,
        @Schema(description = "정산 정보") SettlementInfoV1ApiResponse settlementInfo,
        @Schema(description = "주문 상품 정보") OrderProductV1ApiResponse orderProduct) {

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
            @Schema(description = "결제 방법", example = "CARD") String paymentMethod,
            @Schema(description = "결제 일시",
                    example = "2024-01-01 00:00:00") @com.fasterxml.jackson.annotation.JsonFormat(
                            pattern = "yyyy-MM-dd HH:mm:ss") java.time.LocalDateTime paymentDate,
            @Schema(description = "취소 일시",
                    example = "2024-01-01 00:00:00") @com.fasterxml.jackson.annotation.JsonFormat(
                            pattern = "yyyy-MM-dd HH:mm:ss") java.time.LocalDateTime canceledDate,
            @Schema(description = "사용자 ID", example = "1") Long userId,
            @Schema(description = "사이트명", example = "SETOF") String siteName,
            @Schema(description = "청구 금액", example = "90000") java.math.BigDecimal billAmount,
            @Schema(description = "결제 금액", example = "90000") java.math.BigDecimal paymentAmount,
            @Schema(description = "사용한 마일리지 금액",
                    example = "1000") java.math.BigDecimal usedMileageAmount) {
    }

    @Schema(description = "수령인 정보")
    public record ReceiverInfoV1ApiResponse(
            @Schema(description = "수령인 이름", example = "홍길동") String receiverName,
            @Schema(description = "수령인 전화번호", example = "01012345678") String receiverPhoneNumber,
            @Schema(description = "주소 1", example = "서울시 강남구 테헤란로 123") String addressLine1,
            @Schema(description = "주소 2", example = "124-1234") String addressLine2,
            @Schema(description = "우편번호", example = "12345") String zipCode,
            @Schema(description = "국가 코드", example = "KR") String country,
            @Schema(description = "배송 요청 사항", example = "문 앞에 놔주세요") String deliveryRequest) {
    }

    @Schema(description = "배송 정보")
    public record PaymentShipmentInfoV1ApiResponse(
            @Schema(description = "배송 상태", example = "SHIPPING") String deliveryStatus,
            @Schema(description = "배송 회사 코드", example = "CJ") String shipmentCompanyCode,
            @Schema(description = "송장 번호", example = "1234567890") String invoice,
            @Schema(description = "배송 완료 일시",
                    example = "2024-01-01 00:00:00") @com.fasterxml.jackson.annotation.JsonFormat(
                            pattern = "yyyy-MM-dd HH:mm:ss") java.time.LocalDateTime shipmentCompletedDate) {
    }

    @Schema(description = "정산 정보")
    public record SettlementInfoV1ApiResponse(
            @Schema(description = "수수료율", example = "5.0") Double commissionRate,
            @Schema(description = "수수료", example = "4500.0") Double fee,
            @Schema(description = "예상 정산 금액",
                    example = "85500") java.math.BigDecimal expectationSettlementAmount,
            @Schema(description = "정산 금액", example = "85500") java.math.BigDecimal settlementAmount,
            @Schema(description = "공유 비율", example = "50.0") Double shareRatio,
            @Schema(description = "예상 정산일",
                    example = "2024-01-15 00:00:00") @com.fasterxml.jackson.annotation.JsonFormat(
                            pattern = "yyyy-MM-dd HH:mm:ss") java.time.LocalDateTime expectedSettlementDay,
            @Schema(description = "정산일",
                    example = "2024-01-15 00:00:00") @com.fasterxml.jackson.annotation.JsonFormat(
                            pattern = "yyyy-MM-dd HH:mm:ss") java.time.LocalDateTime settlementDay) {
    }

    @Schema(description = "주문 상품 정보")
    public record OrderProductV1ApiResponse(
            @Schema(description = "주문 ID", example = "1") Long orderId,
            @Schema(description = "상품 그룹 스냅샷 상세") ProductGroupSnapShotDetailsV1ApiResponse productGroupDetails,
            @Schema(description = "브랜드 정보") BrandV1ApiResponse brand,
            @Schema(description = "상품 그룹 ID", example = "12345") Long productGroupId,
            @Schema(description = "상품 ID", example = "123") Long productId,
            @Schema(description = "셀러명", example = "셀러명") String sellerName,
            @Schema(description = "상품 그룹 메인 이미지 URL",
                    example = "https://example.com/image.jpg") String productGroupMainImageUrl,
            @Schema(description = "배송 지역", example = "전국") String deliveryArea,
            @Schema(description = "상품 수량", example = "2") Integer productQuantity,
            @Schema(description = "주문 상태", example = "COMPLETED") String orderStatus,
            @Schema(description = "정가", example = "50000") java.math.BigDecimal regularPrice,
            @Schema(description = "주문 금액", example = "78000") java.math.BigDecimal orderAmount,
            @Schema(description = "총 예상 환불 마일리지 금액",
                    example = "1000") java.math.BigDecimal totalExpectedRefundMileageAmount,
            @Schema(description = "옵션", example = "빨강 M") String option,
            @Schema(description = "SKU 번호", example = "STYLE001_빨강 M") String skuNumber,
            @Schema(description = "옵션 목록") java.util.Set<OptionV1ApiResponse> options) {

        @Schema(description = "상품 그룹 스냅샷 상세")
        public record ProductGroupSnapShotDetailsV1ApiResponse(
                @Schema(description = "상품 그룹명", example = "멋진 티셔츠") String productGroupName,
                @Schema(description = "옵션 타입", example = "SIZE_COLOR") String optionType,
                @Schema(description = "관리 타입", example = "SELF") String managementType,
                @Schema(description = "가격 정보") PriceV1ApiResponse price,
                @Schema(description = "상품 상태", example = "ON_SALE") String productStatus,
                @Schema(description = "의류 상세 정보") ClothesDetailV1ApiResponse clothesDetailInfo,
                @Schema(description = "셀러 ID", example = "100") Long sellerId,
                @Schema(description = "카테고리 ID", example = "1") Long categoryId,
                @Schema(description = "브랜드 ID", example = "1") Long brandId) {
        }

        @Schema(description = "브랜드 정보")
        public record BrandV1ApiResponse(
                @Schema(description = "브랜드 ID", example = "1") Long brandId,
                @Schema(description = "브랜드명", example = "Nike") String brandName) {
        }

        @Schema(description = "가격 정보")
        public record PriceV1ApiResponse(
                @Schema(description = "정가", example = "50000") Long regularPrice,
                @Schema(description = "현재가", example = "40000") Long currentPrice,
                @Schema(description = "판매가", example = "39000") Long salePrice,
                @Schema(description = "직접 할인율", example = "10") Integer directDiscountRate,
                @Schema(description = "직접 할인가", example = "1000") Long directDiscountPrice,
                @Schema(description = "할인율", example = "22") Integer discountRate) {
        }

        @Schema(description = "의류 상세 정보")
        public record ClothesDetailV1ApiResponse(
                @Schema(description = "상품 상태", example = "NEW") String productCondition,
                @Schema(description = "원산지", example = "한국") String origin,
                @Schema(description = "스타일 코드", example = "STYLE001") String styleCode) {
        }

        @Schema(description = "옵션 응답")
        public record OptionV1ApiResponse(
                @Schema(description = "옵션 그룹 ID", example = "1") Long optionGroupId,
                @Schema(description = "옵션 상세 ID", example = "1") Long optionDetailId,
                @Schema(description = "옵션명", example = "SIZE") String optionName,
                @Schema(description = "옵션 값", example = "M") String optionValue) {
        }
    }
}
