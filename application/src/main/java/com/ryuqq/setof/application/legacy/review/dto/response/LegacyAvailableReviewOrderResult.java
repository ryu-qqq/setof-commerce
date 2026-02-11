package com.ryuqq.setof.application.legacy.review.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 레거시 작성 가능한 리뷰 주문 결과 DTO.
 *
 * <p>ReviewOrderProductDto 대응.
 *
 * @param paymentId 결제 ID
 * @param sellerId 셀러 ID
 * @param orderId 주문 ID
 * @param brand 브랜드 정보
 * @param productGroupId 상품그룹 ID
 * @param productGroupName 상품그룹명
 * @param productId 상품 ID
 * @param sellerName 셀러명
 * @param productGroupMainImageUrl 상품그룹 메인 이미지 URL
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
public record LegacyAvailableReviewOrderResult(
        long paymentId,
        long sellerId,
        long orderId,
        BrandResult brand,
        long productGroupId,
        String productGroupName,
        long productId,
        String sellerName,
        String productGroupMainImageUrl,
        int productQuantity,
        String orderStatus,
        long regularPrice,
        long currentPrice,
        long orderAmount,
        List<OptionResult> options,
        LocalDateTime paymentDate) {

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
