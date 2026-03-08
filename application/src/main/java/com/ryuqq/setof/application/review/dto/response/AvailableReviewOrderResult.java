package com.ryuqq.setof.application.review.dto.response;

import java.time.Instant;
import java.util.List;

/**
 * 작성 가능한 리뷰 주문 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param legacyOrderId 레거시 주문 ID (Long, 레거시 시스템)
 * @param orderId 주문 ID (String, UUIDv7 기반, nullable)
 * @param legacyPaymentId 레거시 결제 ID (Long, 레거시 시스템)
 * @param paymentId 결제 ID (String, UUIDv7 기반, nullable)
 * @param seller 셀러 정보
 * @param product 상품 정보
 * @param brand 브랜드 정보
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
public record AvailableReviewOrderResult(
        Long legacyOrderId,
        String orderId,
        Long legacyPaymentId,
        String paymentId,
        SellerResult seller,
        ProductResult product,
        BrandResult brand,
        int productQuantity,
        String orderStatus,
        long regularPrice,
        long currentPrice,
        long orderAmount,
        List<OptionResult> options,
        Instant paymentDate) {

    public record SellerResult(long sellerId, String name) {
        public static SellerResult of(long sellerId, String name) {
            return new SellerResult(sellerId, name);
        }
    }

    public record ProductResult(
            long productId,
            long productGroupId,
            String productGroupName,
            String productGroupMainImageUrl) {
        public static ProductResult of(
                long productId,
                long productGroupId,
                String productGroupName,
                String productGroupMainImageUrl) {
            return new ProductResult(
                    productId, productGroupId, productGroupName, productGroupMainImageUrl);
        }
    }

    public record BrandResult(long brandId, String brandName) {
        public static BrandResult of(long brandId, String brandName) {
            return new BrandResult(brandId, brandName);
        }
    }

    public record OptionResult(
            long optionGroupId, long optionDetailId, String optionName, String optionValue) {
        public static OptionResult of(
                long optionGroupId, long optionDetailId, String optionName, String optionValue) {
            return new OptionResult(optionGroupId, optionDetailId, optionName, optionValue);
        }
    }
}
