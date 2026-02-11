package com.ryuqq.setof.application.legacy.product.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * LegacyProductGroupDetailResult - 레거시 상품그룹 상세 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param productGroupId 상품그룹 ID
 * @param productGroupName 상품그룹명
 * @param sellerId 셀러 ID
 * @param sellerName 셀러명
 * @param brand 브랜드 정보
 * @param categoryId 카테고리 ID
 * @param path 카테고리 경로
 * @param regularPrice 정가
 * @param currentPrice 현재가
 * @param salePrice 판매가
 * @param optionType 옵션 타입 (SINGLE/COMBINATION)
 * @param displayYn 표시 여부
 * @param soldOutYn 품절 여부
 * @param detailDescription 상세 설명
 * @param deliveryNotice 배송 안내
 * @param refundNotice 환불 안내
 * @param averageRating 평균 평점
 * @param reviewCount 리뷰 수
 * @param products 상품 목록
 * @param productImages 상품 이미지 목록
 * @param createdAt 등록일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyProductGroupDetailResult(
        long productGroupId,
        String productGroupName,
        long sellerId,
        String sellerName,
        LegacyBrandInfo brand,
        long categoryId,
        String path,
        int regularPrice,
        int currentPrice,
        int salePrice,
        String optionType,
        String displayYn,
        String soldOutYn,
        String detailDescription,
        String deliveryNotice,
        String refundNotice,
        double averageRating,
        long reviewCount,
        Set<LegacyProductInfo> products,
        Set<LegacyProductImageInfo> productImages,
        LocalDateTime createdAt) {

    /** 팩토리 메서드. */
    public static LegacyProductGroupDetailResult of(
            long productGroupId,
            String productGroupName,
            long sellerId,
            String sellerName,
            LegacyBrandInfo brand,
            long categoryId,
            String path,
            int regularPrice,
            int currentPrice,
            int salePrice,
            String optionType,
            String displayYn,
            String soldOutYn,
            String detailDescription,
            String deliveryNotice,
            String refundNotice,
            double averageRating,
            long reviewCount,
            Set<LegacyProductInfo> products,
            Set<LegacyProductImageInfo> productImages,
            LocalDateTime createdAt) {
        return new LegacyProductGroupDetailResult(
                productGroupId,
                productGroupName,
                sellerId,
                sellerName,
                brand,
                categoryId,
                path,
                regularPrice,
                currentPrice,
                salePrice,
                optionType,
                displayYn,
                soldOutYn,
                detailDescription,
                deliveryNotice,
                refundNotice,
                averageRating,
                reviewCount,
                products,
                productImages,
                createdAt);
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
     * 브랜드 정보.
     *
     * @param brandId 브랜드 ID
     * @param brandName 브랜드명
     * @param displayKoreanName 표시용 한글 브랜드명
     * @param displayEnglishName 표시용 영문 브랜드명
     * @param brandIconImageUrl 브랜드 아이콘 이미지 URL
     */
    public record LegacyBrandInfo(
            long brandId,
            String brandName,
            String displayKoreanName,
            String displayEnglishName,
            String brandIconImageUrl) {

        public static LegacyBrandInfo of(
                long brandId,
                String brandName,
                String displayKoreanName,
                String displayEnglishName,
                String brandIconImageUrl) {
            return new LegacyBrandInfo(
                    brandId, brandName, displayKoreanName, displayEnglishName, brandIconImageUrl);
        }
    }

    /**
     * 상품 정보.
     *
     * @param productId 상품 ID
     * @param additionalPrice 추가금
     * @param stockQuantity 재고 수량
     * @param soldOutYn 품절 여부
     * @param options 옵션 목록
     */
    public record LegacyProductInfo(
            long productId,
            int additionalPrice,
            int stockQuantity,
            String soldOutYn,
            List<LegacyOptionInfo> options) {

        public static LegacyProductInfo of(
                long productId,
                int additionalPrice,
                int stockQuantity,
                String soldOutYn,
                List<LegacyOptionInfo> options) {
            return new LegacyProductInfo(
                    productId, additionalPrice, stockQuantity, soldOutYn, options);
        }

        public boolean isSoldOut() {
            return "Y".equals(soldOutYn);
        }
    }

    /**
     * 옵션 정보.
     *
     * @param optionGroupId 옵션 그룹 ID
     * @param optionGroupName 옵션 그룹명 (색상, 사이즈 등)
     * @param optionDetailId 옵션 상세 ID
     * @param optionValue 옵션 값 (빨강, M 등)
     */
    public record LegacyOptionInfo(
            long optionGroupId, String optionGroupName, long optionDetailId, String optionValue) {

        public static LegacyOptionInfo of(
                long optionGroupId,
                String optionGroupName,
                long optionDetailId,
                String optionValue) {
            return new LegacyOptionInfo(
                    optionGroupId, optionGroupName, optionDetailId, optionValue);
        }
    }

    /**
     * 상품 이미지 정보.
     *
     * @param productGroupImageId 이미지 ID
     * @param imageType 이미지 타입 (MAIN/SUB/DETAIL)
     * @param imageUrl 이미지 URL
     */
    public record LegacyProductImageInfo(
            long productGroupImageId, String imageType, String imageUrl) {

        public static LegacyProductImageInfo of(
                long productGroupImageId, String imageType, String imageUrl) {
            return new LegacyProductImageInfo(productGroupImageId, imageType, imageUrl);
        }
    }
}
