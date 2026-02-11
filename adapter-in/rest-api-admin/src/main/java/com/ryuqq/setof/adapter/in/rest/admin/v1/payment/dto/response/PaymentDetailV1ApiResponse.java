package com.ryuqq.setof.adapter.in.rest.admin.v1.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * PaymentDetailV1ApiResponse - 결제 상세 응답 DTO.
 *
 * <p>레거시 PaymentResponse 기반 변환.
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class -> record 타입
 *   <li>Lombok @Getter -> record 기본 접근자
 *   <li>Money 타입 -> BigDecimal 타입
 *   <li>Enum 타입 -> String 타입 (allowableValues 명시)
 *   <li>@Schema 어노테이션 추가
 *   <li>중첩 class -> 중첩 record
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.payment.dto.PaymentResponse
 */
@Schema(description = "결제 상세 응답")
public record PaymentDetailV1ApiResponse(
        @Schema(description = "구매자 정보") BuyerInfoResponse buyerInfo,
        @Schema(description = "결제 정보") PaymentInfoResponse payment,
        @Schema(description = "수령인 정보") ReceiverInfoResponse receiverInfo,
        @Schema(description = "배송 정보") ShipmentInfoResponse shipmentInfo,
        @Schema(description = "주문 상품 목록") List<OrderProductResponse> orderProducts) {

    /**
     * 구매자 정보 응답.
     *
     * @see com.connectly.partnerAdmin.module.payment.entity.embedded.BuyerInfo
     */
    @Schema(description = "구매자 정보")
    public record BuyerInfoResponse(
            @Schema(description = "구매자명", example = "홍길동") String buyerName,
            @Schema(description = "구매자 이메일", example = "hong@example.com") String buyerEmail,
            @Schema(description = "구매자 전화번호", example = "010-1234-5678") String buyerPhoneNumber) {}

    /**
     * 결제 정보 응답.
     *
     * @see com.connectly.partnerAdmin.module.payment.dto.payment.PaymentDetail
     */
    @Schema(description = "결제 정보")
    public record PaymentInfoResponse(
            @Schema(description = "결제 ID", example = "12345") long paymentId,
            @Schema(description = "PG사 결제 ID", example = "pg_abc123") String paymentAgencyId,
            @Schema(
                            description = "결제 상태",
                            example = "PAID",
                            allowableValues = {
                                "READY",
                                "PAID",
                                "CANCELED",
                                "PARTIAL_CANCELED",
                                "FAILED"
                            })
                    String paymentStatus,
            @Schema(
                            description = "결제 수단",
                            example = "CARD",
                            allowableValues = {
                                "CARD",
                                "VIRTUAL_ACCOUNT",
                                "TRANSFER",
                                "PHONE",
                                "KAKAO_PAY",
                                "NAVER_PAY",
                                "TOSS_PAY"
                            })
                    String paymentMethod,
            @Schema(description = "결제일시", example = "2026-02-01T14:30:00")
                    LocalDateTime paymentDate,
            @Schema(description = "취소일시", example = "null", nullable = true)
                    LocalDateTime canceledDate,
            @Schema(description = "사용자 ID", example = "100") long userId,
            @Schema(
                            description = "사이트명",
                            example = "SETOF",
                            allowableValues = {"SETOF", "CONNECTLY"})
                    String siteName,
            @Schema(description = "청구 금액 (결제금액 + 마일리지)", example = "150000") BigDecimal billAmount,
            @Schema(description = "실 결제 금액", example = "140000") BigDecimal paymentAmount,
            @Schema(description = "사용 마일리지", example = "10000") BigDecimal usedMileageAmount) {}

    /**
     * 수령인 정보 응답.
     *
     * @see com.connectly.partnerAdmin.module.payment.dto.receiver.ReceiverInfo
     */
    @Schema(description = "수령인 정보")
    public record ReceiverInfoResponse(
            @Schema(description = "수령인명", example = "김수령") String receiverName,
            @Schema(description = "수령인 전화번호", example = "010-9876-5432") String receiverPhoneNumber,
            @Schema(description = "주소 1", example = "서울시 강남구") String addressLine1,
            @Schema(description = "주소 2", example = "테헤란로 123") String addressLine2,
            @Schema(description = "우편번호", example = "06234") String zipCode,
            @Schema(
                            description = "국가",
                            example = "KOREA",
                            allowableValues = {"KOREA", "USA", "JAPAN", "CHINA"})
                    String country,
            @Schema(description = "배송 요청사항", example = "문 앞에 놓아주세요") String deliveryRequest) {}

    /**
     * 배송 정보 응답.
     *
     * @see com.connectly.partnerAdmin.module.payment.dto.shipment.PaymentShipmentInfo
     */
    @Schema(description = "배송 정보")
    public record ShipmentInfoResponse(
            @Schema(
                            description = "배송 상태",
                            example = "DELIVERED",
                            allowableValues = {
                                "PENDING",
                                "READY",
                                "SHIPPING",
                                "DELIVERED",
                                "CANCELED"
                            })
                    String deliveryStatus,
            @Schema(description = "택배사 코드", example = "CJ", nullable = true)
                    String shipmentCompanyCode,
            @Schema(description = "운송장 번호", example = "123456789012", nullable = true)
                    String invoice,
            @Schema(description = "배송 완료일", example = "2026-02-03T10:00:00", nullable = true)
                    LocalDateTime shipmentCompletedDate) {}

    /**
     * 주문 상품 응답.
     *
     * @see com.connectly.partnerAdmin.module.order.dto.OrderProduct
     */
    @Schema(description = "주문 상품")
    public record OrderProductResponse(
            @Schema(description = "주문 ID", example = "1001") long orderId,
            @Schema(description = "상품그룹 ID", example = "500") long productGroupId,
            @Schema(description = "상품 ID", example = "501") long productId,
            @Schema(description = "브랜드 정보") BrandResponse brand,
            @Schema(description = "판매자명", example = "판매자A") String sellerName,
            @Schema(description = "상품그룹명", example = "나이키 에어맥스 90") String productGroupName,
            @Schema(description = "상품 대표 이미지 URL", example = "https://cdn.example.com/image.jpg")
                    String productGroupMainImageUrl,
            @Schema(description = "배송 지역", example = "전국") String deliveryArea,
            @Schema(description = "상품 수량", example = "2") int productQuantity,
            @Schema(
                            description = "주문 상태",
                            example = "DELIVERED",
                            allowableValues = {
                                "PENDING",
                                "PAID",
                                "PREPARING",
                                "SHIPPING",
                                "DELIVERED",
                                "CANCELED",
                                "RETURN_REQUESTED",
                                "RETURNED"
                            })
                    String orderStatus,
            @Schema(description = "정가", example = "89000") BigDecimal regularPrice,
            @Schema(description = "주문 금액", example = "79000") BigDecimal orderAmount,
            @Schema(description = "환불 예상 마일리지", example = "790")
                    BigDecimal totalExpectedRefundMileageAmount,
            @Schema(description = "옵션 문자열", example = "블랙 M") String option,
            @Schema(description = "SKU 번호", example = "NK-AM90-BLK-M") String skuNumber,
            @Schema(description = "옵션 상세 목록") List<OptionResponse> options) {

        /** 브랜드 정보 응답. */
        @Schema(description = "브랜드 정보")
        public record BrandResponse(
                @Schema(description = "브랜드 ID", example = "10") long brandId,
                @Schema(description = "브랜드명", example = "나이키") String brandName) {}

        /** 옵션 정보 응답. */
        @Schema(description = "옵션 정보")
        public record OptionResponse(
                @Schema(description = "옵션그룹 ID", example = "1") long optionGroupId,
                @Schema(description = "옵션상세 ID", example = "10") long optionDetailId,
                @Schema(description = "옵션명", example = "색상") String optionName,
                @Schema(description = "옵션값", example = "블랙") String optionValue) {}
    }
}
