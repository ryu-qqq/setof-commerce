package com.ryuqq.setof.adapter.in.rest.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * OrderV1ApiResponse - 주문 아이템 응답 DTO.
 *
 * <p>레거시 OrderResponse 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param payment 결제 정보
 * @param orderProduct 주문 상품 정보
 * @param buyerInfo 구매자 정보
 * @param receiverInfo 수령인 정보
 * @param totalExpectedMileageAmount 예상 적립 마일리지
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.order.dto.fetch.OrderResponse
 */
@Schema(description = "주문 응답")
public record OrderV1ApiResponse(
        @Schema(description = "결제 정보") PaymentResponse payment,
        @Schema(description = "주문 상품 정보") OrderProductResponse orderProduct,
        @Schema(description = "구매자 정보") BuyerInfoResponse buyerInfo,
        @Schema(description = "수령인 정보") ReceiverInfoResponse receiverInfo,
        @Schema(description = "예상 적립 마일리지", example = "1100.0") double totalExpectedMileageAmount) {

    /**
     * PaymentResponse - 결제 정보 응답.
     *
     * @param paymentId 결제 ID
     * @param paymentAgencyId PG사 주문번호
     * @param paymentStatus 결제 상태
     * @param paymentMethod 결제 수단
     * @param paymentDate 결제 일시
     * @param canceledDate 취소 일시
     * @param paymentAmount 결제 금액
     * @param usedMileageAmount 사용 마일리지
     * @param cardName 카드명
     * @param cardNumber 카드번호 (마스킹)
     * @param totalExpectedMileageAmount 예상 적립 마일리지
     */
    @Schema(description = "결제 정보")
    public record PaymentResponse(
            @Schema(description = "결제 ID", example = "12345") long paymentId,
            @Schema(description = "PG사 주문번호", example = "PG_ORDER_123") String paymentAgencyId,
            @Schema(description = "결제 상태", example = "PAYMENT_COMPLETED") String paymentStatus,
            @Schema(description = "결제 수단", example = "신용카드") String paymentMethod,
            @Schema(description = "결제 일시", example = "2024-01-15T14:30:00")
                    LocalDateTime paymentDate,
            @Schema(description = "취소 일시", nullable = true) LocalDateTime canceledDate,
            @Schema(description = "결제 금액", example = "50000") long paymentAmount,
            @Schema(description = "사용 마일리지", example = "1000.0") double usedMileageAmount,
            @Schema(description = "카드명", example = "삼성카드") String cardName,
            @Schema(description = "카드번호 (마스킹)", example = "1234-****-****-5678") String cardNumber,
            @Schema(description = "예상 적립 마일리지", example = "500.0")
                    double totalExpectedMileageAmount) {}

    /**
     * OrderProductResponse - 주문 상품 정보 응답.
     *
     * @param orderId 주문 ID
     * @param sellerId 판매자 ID
     * @param sellerName 판매자명
     * @param brand 브랜드 정보
     * @param productGroupId 상품그룹 ID
     * @param productGroupName 상품그룹명
     * @param productId 상품(SKU) ID
     * @param productGroupMainImageUrl 대표 이미지 URL
     * @param productQuantity 주문 수량
     * @param orderStatus 주문 상태
     * @param regularPrice 정가
     * @param salePrice 판매가
     * @param discountPrice 할인액
     * @param directDiscountPrice 즉시할인가
     * @param orderAmount 주문 금액
     * @param option 옵션 값 (조합된 문자열)
     * @param options 옵션 상세 목록
     * @param refundNotice 환불/반품 안내
     * @param shipmentInfo 배송 정보
     * @param reviewYn 리뷰 작성 여부
     */
    @Schema(description = "주문 상품 정보")
    public record OrderProductResponse(
            @Schema(description = "주문 ID", example = "67890") long orderId,
            @Schema(description = "판매자 ID", example = "100") long sellerId,
            @Schema(description = "판매자명", example = "공식스토어") String sellerName,
            @Schema(description = "브랜드 정보") BrandResponse brand,
            @Schema(description = "상품그룹 ID", example = "200") long productGroupId,
            @Schema(description = "상품그룹명", example = "에어맥스 90") String productGroupName,
            @Schema(description = "상품(SKU) ID", example = "300") long productId,
            @Schema(description = "대표 이미지 URL", example = "https://cdn.example.com/image.jpg")
                    String productGroupMainImageUrl,
            @Schema(description = "주문 수량", example = "1") int productQuantity,
            @Schema(description = "주문 상태", example = "DELIVERY_PENDING") String orderStatus,
            @Schema(description = "정가", example = "150000") long regularPrice,
            @Schema(description = "판매가", example = "120000") long salePrice,
            @Schema(description = "할인액", example = "30000") long discountPrice,
            @Schema(description = "즉시할인가", example = "10000") long directDiscountPrice,
            @Schema(description = "주문 금액", example = "110000") long orderAmount,
            @Schema(description = "옵션 값 (조합)", example = "270mm 화이트") String option,
            @Schema(description = "옵션 상세 목록") Set<OptionResponse> options,
            @Schema(description = "환불/반품 안내") RefundNoticeResponse refundNotice,
            @Schema(description = "배송 정보") ShipmentInfoResponse shipmentInfo,
            @Schema(description = "리뷰 작성 여부", example = "N") String reviewYn) {}

    /**
     * BrandResponse - 브랜드 정보 응답.
     *
     * @param brandId 브랜드 ID
     * @param brandName 브랜드명
     */
    @Schema(description = "브랜드 정보")
    public record BrandResponse(
            @Schema(description = "브랜드 ID", example = "10") long brandId,
            @Schema(description = "브랜드명", example = "나이키") String brandName) {}

    /**
     * OptionResponse - 옵션 정보 응답.
     *
     * @param optionGroupId 옵션 그룹 ID
     * @param optionDetailId 옵션 상세 ID
     * @param optionName 옵션 그룹명 (예: 사이즈, 색상)
     * @param optionValue 옵션 값 (예: 270mm, 화이트)
     */
    @Schema(description = "옵션 정보")
    public record OptionResponse(
            @Schema(description = "옵션 그룹 ID", example = "1") long optionGroupId,
            @Schema(description = "옵션 상세 ID", example = "10") long optionDetailId,
            @Schema(description = "옵션 그룹명", example = "사이즈") String optionName,
            @Schema(description = "옵션 값", example = "270mm") String optionValue) {}

    /**
     * RefundNoticeResponse - 환불/반품 안내 응답.
     *
     * @param returnMethodDomestic 국내 반품 방법
     * @param returnCourierDomestic 국내 반품 택배사
     * @param returnChargeDomestic 국내 반품 비용
     * @param returnAddress 반품 주소
     */
    @Schema(description = "환불/반품 안내")
    public record RefundNoticeResponse(
            @Schema(description = "국내 반품 방법", example = "택배 수거") String returnMethodDomestic,
            @Schema(description = "국내 반품 택배사", example = "CJ대한통운") String returnCourierDomestic,
            @Schema(description = "국내 반품 비용", example = "3000") String returnChargeDomestic,
            @Schema(description = "반품 주소", example = "서울시 강남구 테헤란로 123") String returnAddress) {}

    /**
     * ShipmentInfoResponse - 배송 정보 응답.
     *
     * @param orderId 주문 ID
     * @param deliveryStatus 배송 상태
     * @param companyCode 배송사 코드
     * @param invoiceNo 송장 번호
     * @param insertDate 등록 일시
     */
    @Schema(description = "배송 정보")
    public record ShipmentInfoResponse(
            @Schema(description = "주문 ID", example = "67890") long orderId,
            @Schema(description = "배송 상태", example = "PREPARING") String deliveryStatus,
            @Schema(description = "배송사 코드", example = "CJ_LOGISTICS") String companyCode,
            @Schema(description = "송장 번호", example = "1234567890", nullable = true)
                    String invoiceNo,
            @Schema(description = "등록 일시", example = "2024-01-15T15:00:00")
                    LocalDateTime insertDate) {}

    /**
     * BuyerInfoResponse - 구매자 정보 응답.
     *
     * @param name 구매자명
     * @param phoneNumber 연락처
     * @param email 이메일
     */
    @Schema(description = "구매자 정보")
    public record BuyerInfoResponse(
            @Schema(description = "구매자명", example = "홍길동") String name,
            @Schema(description = "연락처", example = "010-1234-5678") String phoneNumber,
            @Schema(description = "이메일", example = "hong@example.com") String email) {}

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
     * @param phoneNumber 연락처
     */
    @Schema(description = "수령인 정보")
    public record ReceiverInfoResponse(
            @Schema(description = "수령인명", example = "홍길동") String receiverName,
            @Schema(description = "수령인 연락처", example = "010-1234-5678") String receiverPhoneNumber,
            @Schema(description = "주소1", example = "서울시 강남구") String addressLine1,
            @Schema(description = "주소2 (상세주소)", example = "테헤란로 123") String addressLine2,
            @Schema(description = "우편번호", example = "06234") String zipCode,
            @Schema(description = "국가", example = "DOMESTIC") String country,
            @Schema(description = "배송 요청사항", example = "문앞에 놓아주세요") String deliveryRequest,
            @Schema(description = "연락처", example = "010-1234-5678") String phoneNumber) {}
}
