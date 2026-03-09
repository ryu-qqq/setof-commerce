package com.ryuqq.setof.storage.legacy.composite.productgroup.dto;

import java.time.LocalDateTime;

/**
 * LegacyWebProductGroupThumbnailQueryDto - 레거시 Web 상품그룹 썸네일 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * <p>product_group, product_group_image, brand, product_rating_stats, product_score 조인 결과를 담습니다.
 *
 * @param productGroupId 상품그룹 ID
 * @param sellerId 셀러 ID
 * @param productGroupName 상품그룹명
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param displayKoreanName 브랜드 한글 표시명
 * @param displayEnglishName 브랜드 영문 표시명
 * @param brandIconImageUrl 브랜드 아이콘 이미지 URL
 * @param productImageUrl 대표 이미지 URL
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
 * @param displayYn 표시 여부
 * @param soldOutYn 품절 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebProductGroupThumbnailQueryDto(
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
