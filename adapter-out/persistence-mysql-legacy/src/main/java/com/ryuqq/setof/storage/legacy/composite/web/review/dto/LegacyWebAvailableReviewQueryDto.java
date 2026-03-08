package com.ryuqq.setof.storage.legacy.composite.web.review.dto;

import java.time.LocalDateTime;

/**
 * 레거시 Web 작성 가능한 리뷰 주문 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * <p>order + payment + product + product_group + product_group_image + brand + seller JOIN 결과.
 *
 * @param orderId 주문 ID
 * @param paymentId 결제 ID
 * @param sellerId 셀러 ID
 * @param sellerName 셀러명
 * @param productId 상품 ID
 * @param productGroupId 상품그룹 ID
 * @param productGroupName 상품그룹명
 * @param productGroupMainImageUrl 상품그룹 메인 이미지 URL
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param productQuantity 주문 수량
 * @param orderStatus 주문 상태
 * @param regularPrice 정가
 * @param currentPrice 판매가
 * @param orderAmount 주문 금액
 * @param paymentDate 결제일시
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebAvailableReviewQueryDto(
        long orderId,
        long paymentId,
        long sellerId,
        String sellerName,
        long productId,
        long productGroupId,
        String productGroupName,
        String productGroupMainImageUrl,
        long brandId,
        String brandName,
        int productQuantity,
        String orderStatus,
        long regularPrice,
        long currentPrice,
        long orderAmount,
        LocalDateTime paymentDate) {}
