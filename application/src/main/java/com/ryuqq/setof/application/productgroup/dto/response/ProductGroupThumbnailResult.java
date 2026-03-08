package com.ryuqq.setof.application.productgroup.dto.response;

import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupThumbnailCompositeResult;
import java.time.LocalDateTime;

/**
 * ProductGroupThumbnailResult - 상품그룹 썸네일 응답 DTO.
 *
 * <p>V1 ApiMapper에서 레거시 응답 포맷으로 변환하기 위한 Application Layer Result입니다.
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
 * @param displayYn 노출 여부
 * @param soldOutYn 품절 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductGroupThumbnailResult(
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
        String soldOutYn) {

    public static ProductGroupThumbnailResult from(ProductGroupThumbnailCompositeResult composite) {
        return new ProductGroupThumbnailResult(
                composite.productGroupId(),
                composite.sellerId(),
                composite.productGroupName(),
                composite.brandId(),
                composite.brandName(),
                composite.displayKoreanName(),
                composite.displayEnglishName(),
                composite.brandIconImageUrl(),
                composite.productImageUrl(),
                composite.regularPrice(),
                composite.currentPrice(),
                composite.salePrice(),
                composite.directDiscountRate(),
                composite.directDiscountPrice(),
                composite.discountRate(),
                composite.insertDate(),
                composite.averageRating(),
                composite.reviewCount(),
                composite.score(),
                composite.displayYn(),
                composite.soldOutYn());
    }
}
