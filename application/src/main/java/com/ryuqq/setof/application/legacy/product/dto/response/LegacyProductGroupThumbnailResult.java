package com.ryuqq.setof.application.legacy.product.dto.response;

import java.time.LocalDateTime;

/**
 * LegacyProductGroupThumbnailResult - 레거시 상품그룹 썸네일 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param productGroupId 상품그룹 ID
 * @param sellerId 셀러 ID
 * @param productGroupName 상품그룹명
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param displayKoreanName 표시용 한글 브랜드명
 * @param displayEnglishName 표시용 영문 브랜드명
 * @param brandIconImageUrl 브랜드 아이콘 이미지 URL
 * @param productImageUrl 대표 이미지 URL
 * @param regularPrice 정가
 * @param currentPrice 현재가
 * @param salePrice 판매가
 * @param createdAt 등록일
 * @param averageRating 평균 평점
 * @param reviewCount 리뷰 수
 * @param score 추천 스코어
 * @param displayYn 표시 여부
 * @param soldOutYn 품절 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyProductGroupThumbnailResult(
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
        LocalDateTime createdAt,
        double averageRating,
        long reviewCount,
        Double score,
        String displayYn,
        String soldOutYn) {

    /**
     * 팩토리 메서드.
     *
     * @param productGroupId 상품그룹 ID
     * @param sellerId 셀러 ID
     * @param productGroupName 상품그룹명
     * @param brandId 브랜드 ID
     * @param brandName 브랜드명
     * @param displayKoreanName 표시용 한글 브랜드명
     * @param displayEnglishName 표시용 영문 브랜드명
     * @param brandIconImageUrl 브랜드 아이콘 이미지 URL
     * @param productImageUrl 대표 이미지 URL
     * @param regularPrice 정가
     * @param currentPrice 현재가
     * @param salePrice 판매가
     * @param createdAt 등록일
     * @param averageRating 평균 평점
     * @param reviewCount 리뷰 수
     * @param score 추천 스코어
     * @param displayYn 표시 여부
     * @param soldOutYn 품절 여부
     * @return LegacyProductGroupThumbnailResult
     */
    public static LegacyProductGroupThumbnailResult of(
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
            LocalDateTime createdAt,
            double averageRating,
            long reviewCount,
            Double score,
            String displayYn,
            String soldOutYn) {
        return new LegacyProductGroupThumbnailResult(
                productGroupId,
                sellerId,
                productGroupName,
                brandId,
                brandName,
                displayKoreanName,
                displayEnglishName,
                brandIconImageUrl,
                productImageUrl,
                regularPrice,
                currentPrice,
                salePrice,
                createdAt,
                averageRating,
                reviewCount,
                score,
                displayYn,
                soldOutYn);
    }

    /**
     * 할인율 계산.
     *
     * @return 할인율 (%)
     */
    public int discountRate() {
        if (regularPrice == 0) {
            return 0;
        }
        return (int) ((double) (regularPrice - salePrice) / regularPrice * 100);
    }

    /**
     * 품절 여부 확인.
     *
     * @return 품절이면 true
     */
    public boolean isSoldOut() {
        return "Y".equals(soldOutYn);
    }

    /**
     * 표시 중인지 확인.
     *
     * @return 표시 중이면 true
     */
    public boolean isDisplayed() {
        return "Y".equals(displayYn);
    }
}
