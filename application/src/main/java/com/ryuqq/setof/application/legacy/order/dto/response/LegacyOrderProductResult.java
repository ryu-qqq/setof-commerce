package com.ryuqq.setof.application.legacy.order.dto.response;

import java.util.Set;

/**
 * LegacyOrderProductResult - 레거시 주문 상품 결과 DTO.
 *
 * @param paymentId 결제 ID
 * @param sellerId 판매자 ID
 * @param orderId 주문 ID
 * @param brand 브랜드 정보
 * @param productGroupId 상품 그룹 ID
 * @param productGroupName 상품 그룹명
 * @param productId 상품 ID
 * @param sellerName 판매자명
 * @param productGroupMainImageUrl 상품 메인 이미지 URL
 * @param productQuantity 상품 수량
 * @param orderStatus 주문 상태
 * @param regularPrice 정가
 * @param salePrice 판매가
 * @param directDiscountPrice 직접 할인가
 * @param orderAmount 주문 금액
 * @param options 옵션 정보
 * @param refundNotice 환불 정보
 * @param shipmentInfo 배송 정보
 * @param reviewYn 리뷰 작성 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyOrderProductResult(
        long paymentId,
        long sellerId,
        long orderId,
        LegacyBrandResult brand,
        long productGroupId,
        String productGroupName,
        long productId,
        String sellerName,
        String productGroupMainImageUrl,
        int productQuantity,
        String orderStatus,
        long regularPrice,
        long salePrice,
        long directDiscountPrice,
        long orderAmount,
        Set<LegacyOptionResult> options,
        LegacyRefundNoticeResult refundNotice,
        LegacyShipmentInfoResult shipmentInfo,
        String reviewYn) {

    /** 정적 팩토리 메서드. */
    public static LegacyOrderProductResult of(
            long paymentId,
            long sellerId,
            long orderId,
            LegacyBrandResult brand,
            long productGroupId,
            String productGroupName,
            long productId,
            String sellerName,
            String productGroupMainImageUrl,
            int productQuantity,
            String orderStatus,
            long regularPrice,
            long salePrice,
            long directDiscountPrice,
            long orderAmount,
            Set<LegacyOptionResult> options,
            LegacyRefundNoticeResult refundNotice,
            LegacyShipmentInfoResult shipmentInfo,
            String reviewYn) {
        return new LegacyOrderProductResult(
                paymentId,
                sellerId,
                orderId,
                brand,
                productGroupId,
                productGroupName,
                productId,
                sellerName,
                productGroupMainImageUrl,
                productQuantity,
                orderStatus,
                regularPrice,
                salePrice,
                directDiscountPrice,
                orderAmount,
                options,
                refundNotice,
                shipmentInfo,
                reviewYn);
    }
}
