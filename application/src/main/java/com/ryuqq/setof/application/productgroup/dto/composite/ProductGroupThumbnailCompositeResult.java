package com.ryuqq.setof.application.productgroup.dto.composite;

import java.time.LocalDateTime;

/**
 * ProductGroupThumbnailCompositeResult - 상품그룹 썸네일 Composite 조회 결과.
 *
 * <p>목록 조회 (fetchProductGroups, search, brand/seller 필터) 공통으로 사용되는 Composite 결과입니다.
 *
 * @param productGroupId 상품그룹 ID
 * @param sellerId 셀러 ID
 * @param productGroupName 상품그룹명
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param displayKoreanName 브랜드 한글 표시명
 * @param displayEnglishName 브랜드 영문 표시명
 * @param brandIconImageUrl 브랜드 아이콘 이미지 URL
 * @param productImageUrl 대표 이미지 URL (MAIN)
 * @param regularPrice 정가
 * @param currentPrice 현재가
 * @param salePrice 판매가
 * @param directDiscountRate 직접 할인율
 * @param directDiscountPrice 직접 할인액
 * @param discountRate 총 할인율
 * @param insertDate 등록일
 * @param averageRating 평균 평점
 * @param reviewCount 리뷰 수
 * @param score 추천 스코어
 * @param displayYn 노출 여부
 * @param soldOutYn 품절 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductGroupThumbnailCompositeResult(
        long productGroupId,
        long sellerId,
        String productGroupName,
        long brandId,
        String brandName,
        String displayKoreanName,
        String displayEnglishName,
        String brandIconImageUrl,
        String productImageUrl,
        int regularPrice,
        int currentPrice,
        int salePrice,
        int directDiscountRate,
        int directDiscountPrice,
        int discountRate,
        LocalDateTime insertDate,
        double averageRating,
        long reviewCount,
        double score,
        String displayYn,
        String soldOutYn) {}
