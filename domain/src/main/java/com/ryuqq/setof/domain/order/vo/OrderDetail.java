package com.ryuqq.setof.domain.order.vo;

import java.time.LocalDateTime;

/**
 * 주문 상세 조회 VO.
 *
 * <p>주문 목록 API에서 반환되는 풍부한 주문 정보를 담는 불변 값 객체입니다. orders + payment + payment_bill + shipment + 스냅샷
 * 테이블들의 JOIN 결과를 구조화합니다.
 *
 * @param orderId 주문 ID
 * @param orderStatus 주문 상태
 * @param quantity 주문 수량
 * @param orderAmount 주문 금액
 * @param reviewYn 리뷰 작성 여부
 * @param orderDate 주문 일시
 * @param productSnapshot 주문 시점 상품 스냅샷 (브랜드/셀러/이미지/옵션 포함)
 * @param regularPrice 정가
 * @param salePrice 판매가
 * @param directDiscountPrice 즉시할인가
 * @param paymentSummary 결제 요약 정보
 * @param buyerInfo 구매자 정보
 * @param receiverInfo 수령인 정보
 * @param shipmentSummary 배송 정보
 * @param refundNotice 환불/반품 안내
 * @param totalExpectedMileageAmount 예상 적립 마일리지
 */
public record OrderDetail(
        long orderId,
        String orderStatus,
        int quantity,
        long orderAmount,
        String reviewYn,
        LocalDateTime orderDate,
        OrderProductSnapshot productSnapshot,
        long regularPrice,
        long salePrice,
        long directDiscountPrice,
        OrderPaymentSummary paymentSummary,
        OrderBuyerInfo buyerInfo,
        ReceiverInfo receiverInfo,
        OrderShipmentSummary shipmentSummary,
        OrderRefundNotice refundNotice,
        double totalExpectedMileageAmount) {

    public OrderDetail {
        if (productSnapshot == null) {
            throw new IllegalArgumentException("상품 스냅샷은 필수입니다");
        }
    }

    /** 할인 금액 (정가 - 판매가). */
    public long discountPrice() {
        return regularPrice - salePrice;
    }
}
