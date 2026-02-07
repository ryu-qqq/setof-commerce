package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * OrderDetailV1ApiResponse - 주문 상세 응답 DTO.
 *
 * <p>레거시 OrderResponse 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "주문 상세 응답")
public record OrderDetailV1ApiResponse(
        @Schema(description = "주문 ID", example = "12345") long orderId,
        @Schema(description = "구매자 정보") BuyerInfoResponse buyerInfo,
        @Schema(description = "결제 정보") PaymentDetailResponse payment,
        @Schema(description = "수령자 정보") ReceiverInfoResponse receiverInfo,
        @Schema(description = "배송 정보") ShipmentInfoResponse shipmentInfo,
        @Schema(description = "정산 정보") SettlementInfoResponse settlementInfo,
        @Schema(description = "주문 상품 정보") OrderProductResponse orderProduct) {

    @Schema(description = "구매자 정보")
    public record BuyerInfoResponse(
            @Schema(description = "구매자명", example = "홍길동") String buyerName,
            @Schema(description = "구매자 이메일", example = "hong@example.com") String buyerEmail,
            @Schema(description = "구매자 연락처", example = "010-1234-5678") String buyerPhoneNumber) {}

    @Schema(description = "결제 상세 정보")
    public record PaymentDetailResponse(
            @Schema(description = "결제 ID", example = "1001") long paymentId,
            @Schema(description = "결제 대행사 ID", example = "PG123456") String paymentAgencyId,
            @Schema(description = "결제 상태", example = "PAYMENT_COMPLETED") String paymentStatus,
            @Schema(description = "결제 수단", example = "CARD") String paymentMethod,
            @Schema(description = "결제일시", example = "2025-01-15 10:30:00")
                    LocalDateTime paymentDate,
            @Schema(description = "취소일시", example = "2025-01-16 14:00:00")
                    LocalDateTime canceledDate,
            @Schema(description = "사용자 ID", example = "1001") long userId,
            @Schema(description = "판매 채널", example = "OUR_MALL") String siteName,
            @Schema(description = "청구 금액", example = "55000") BigDecimal billAmount,
            @Schema(description = "결제 금액", example = "50000") BigDecimal paymentAmount,
            @Schema(description = "사용 마일리지", example = "5000") BigDecimal usedMileageAmount) {}

    @Schema(description = "수령자 정보")
    public record ReceiverInfoResponse(
            @Schema(description = "수령인명", example = "홍길동") String receiverName,
            @Schema(description = "수령인 연락처", example = "010-1234-5678") String phoneNumber,
            @Schema(description = "기본 주소", example = "서울시 강남구 테헤란로 123") String addressLine1,
            @Schema(description = "상세 주소", example = "101동 1001호") String addressLine2,
            @Schema(description = "우편번호", example = "06234") String zipCode,
            @Schema(description = "국가", example = "KR") String country,
            @Schema(description = "배송 요청사항", example = "문 앞에 놓아주세요") String deliveryRequest) {}

    @Schema(description = "배송 정보")
    public record ShipmentInfoResponse(
            @Schema(description = "배송 상태", example = "DELIVERY_COMPLETED") String deliveryStatus,
            @Schema(description = "택배사 코드", example = "CJ") String companyCode,
            @Schema(description = "송장 번호", example = "1234567890") String invoiceNo,
            @Schema(description = "배송 완료일", example = "2025-01-18 09:00:00")
                    LocalDateTime deliveryDate) {}

    @Schema(description = "정산 정보")
    public record SettlementInfoResponse(
            @Schema(description = "셀러 수수료율", example = "5.0") BigDecimal sellerCommissionRate,
            @Schema(description = "정산일 (구매확정일)", example = "2025-01-25 00:00:00")
                    LocalDateTime settlementDate,
            @Schema(description = "할인 셀러 부담 비율", example = "50.0")
                    BigDecimal directDiscountSellerBurdenRatio,
            @Schema(description = "구매 확정일", example = "2025-01-20 10:00:00")
                    LocalDateTime purchaseConfirmedDate,
            @Schema(description = "상품 정가", example = "60000") BigDecimal currentPrice,
            @Schema(description = "직접 할인 금액", example = "10000") BigDecimal directDiscountPrice,
            @Schema(description = "주문 수량", example = "2") int quantity) {}

    @Schema(description = "주문 상품 정보")
    public record OrderProductResponse(
            @Schema(description = "주문 ID", example = "12345") long orderId,
            @Schema(description = "상품그룹 ID", example = "100") long productGroupId,
            @Schema(description = "상품 ID", example = "200") long productId,
            @Schema(description = "셀러명", example = "테스트셀러") String sellerName,
            @Schema(description = "상품 메인 이미지 URL", example = "https://example.com/image.jpg")
                    String productGroupMainImageUrl,
            @Schema(description = "배송 지역", example = "서울") String deliveryArea,
            @Schema(description = "주문 수량", example = "2") int productQuantity,
            @Schema(description = "주문 상태", example = "DELIVERY_COMPLETED") String orderStatus,
            @Schema(description = "정가", example = "60000") BigDecimal regularPrice,
            @Schema(description = "주문 금액", example = "50000") BigDecimal orderAmount,
            @Schema(description = "환불 예정 마일리지", example = "1000")
                    BigDecimal totalExpectedRefundMileageAmount,
            @Schema(description = "옵션 문자열", example = "블랙 M") String option,
            @Schema(description = "SKU 번호", example = "STYLE001_블랙_M") String skuNumber,
            @Schema(description = "옵션 목록") Set<OptionResponse> options,
            @Schema(description = "브랜드 정보") BrandResponse brand,
            @Schema(description = "상품그룹 상세") ProductGroupDetailResponse productGroupDetails) {}

    @Schema(description = "옵션 정보")
    public record OptionResponse(
            @Schema(description = "옵션명", example = "색상") String optionName,
            @Schema(description = "옵션값", example = "블랙") String optionValue) {}

    @Schema(description = "브랜드 정보")
    public record BrandResponse(
            @Schema(description = "브랜드 ID", example = "1") long brandId,
            @Schema(description = "브랜드명", example = "나이키") String brandName,
            @Schema(description = "브랜드 영문명", example = "NIKE") String brandNameEn) {}

    @Schema(description = "상품그룹 상세")
    public record ProductGroupDetailResponse(
            @Schema(description = "상품그룹명", example = "나이키 에어맥스") String productGroupName,
            @Schema(description = "스타일 코드", example = "NK-AM-001") String styleCode) {}
}
