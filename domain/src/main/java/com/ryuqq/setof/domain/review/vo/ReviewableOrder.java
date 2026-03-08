package com.ryuqq.setof.domain.review.vo;

import java.time.Instant;
import java.util.List;

/**
 * 리뷰 작성 가능한 주문 정보 Value Object.
 *
 * <p>Order 도메인 데이터를 Review 컨텍스트에서 사용하기 위한 읽기 전용 VO입니다.
 *
 * @param legacyOrderId 레거시 주문 ID (Long, 레거시 시스템)
 * @param orderId 주문 ID (String, UUIDv7 기반, nullable)
 * @param legacyPaymentId 레거시 결제 ID (Long, 레거시 시스템)
 * @param paymentId 결제 ID (String, UUIDv7 기반, nullable)
 * @param seller 셀러 스냅샷
 * @param product 상품 스냅샷
 * @param brand 브랜드 스냅샷
 * @param productQuantity 주문 수량
 * @param orderStatus 주문 상태
 * @param regularPrice 정가
 * @param currentPrice 판매가
 * @param orderAmount 주문 금액
 * @param options 옵션 목록
 * @param paymentDate 결제일시
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ReviewableOrder(
        Long legacyOrderId,
        String orderId,
        Long legacyPaymentId,
        String paymentId,
        SellerSnapshot seller,
        ProductSnapshot product,
        BrandSnapshot brand,
        int productQuantity,
        String orderStatus,
        long regularPrice,
        long currentPrice,
        long orderAmount,
        List<ReviewableOrderOption> options,
        Instant paymentDate) {

    public record SellerSnapshot(long sellerId, String name) {}

    public record ProductSnapshot(
            long productId,
            long productGroupId,
            String productGroupName,
            String productGroupMainImageUrl) {}

    public record BrandSnapshot(long brandId, String name) {}

    public record ReviewableOrderOption(
            long optionGroupId, long optionDetailId, String optionName, String optionValue) {

        public static ReviewableOrderOption of(
                long optionGroupId, long optionDetailId, String optionName, String optionValue) {
            return new ReviewableOrderOption(
                    optionGroupId, optionDetailId, optionName, optionValue);
        }
    }
}
