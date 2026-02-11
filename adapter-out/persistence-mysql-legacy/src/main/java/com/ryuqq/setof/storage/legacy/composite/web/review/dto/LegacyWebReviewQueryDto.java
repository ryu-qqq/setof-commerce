package com.ryuqq.setof.storage.legacy.composite.web.review.dto;

import java.time.LocalDateTime;

/**
 * 레거시 Web Review 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param reviewId 리뷰 ID
 * @param orderId 주문 ID
 * @param userName 작성자 이름
 * @param rating 평점
 * @param content 리뷰 내용
 * @param productGroupId 상품그룹 ID
 * @param productGroupName 상품그룹명
 * @param productGroupImageUrl 상품그룹 메인 이미지 URL
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리명
 * @param insertDate 작성일시
 * @param paymentDate 결제일시
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebReviewQueryDto(
        long reviewId,
        long orderId,
        String userName,
        double rating,
        String content,
        long productGroupId,
        String productGroupName,
        String productGroupImageUrl,
        long brandId,
        String brandName,
        long categoryId,
        String categoryName,
        LocalDateTime insertDate,
        LocalDateTime paymentDate) {}
