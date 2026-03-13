package com.ryuqq.setof.application.productgroup.dto.composite;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * ProductGroupDetailCompositeResult - 상품그룹 상세 Composite 조회 결과.
 *
 * <p>fetchProductGroup 엔드포인트의 단건 상세 조회 결과입니다. 기본 정보 + 개별 상품(옵션 포함) + 이미지 목록을 포함합니다.
 *
 * @param productGroupId 상품그룹 ID
 * @param productGroupName 상품그룹명
 * @param sellerId 셀러 ID
 * @param sellerName 셀러명
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param displayKoreanName 브랜드 한글 표시명
 * @param displayEnglishName 브랜드 영문 표시명
 * @param brandIconImageUrl 브랜드 아이콘 이미지 URL
 * @param categoryId 카테고리 ID
 * @param path 카테고리 경로 (쉼표 구분)
 * @param regularPrice 정가
 * @param currentPrice 현재가
 * @param salePrice 판매가
 * @param directDiscountRate 직접 할인율
 * @param directDiscountPrice 직접 할인액
 * @param discountRate 총 할인율
 * @param optionType 옵션 유형
 * @param displayYn 노출 여부
 * @param soldOutYn 품절 여부
 * @param detailDescription 상세 설명 이미지 URL
 * @param deliveryNotice 배송 안내 (JSON)
 * @param refundNotice 반품 안내 (JSON)
 * @param averageRating 평균 평점
 * @param reviewCount 리뷰 수
 * @param products 개별 상품 목록
 * @param images 이미지 목록
 * @param insertDate 등록일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyProductGroupDetailCompositeResult(
        long productGroupId,
        String productGroupName,
        long sellerId,
        String sellerName,
        long brandId,
        String brandName,
        String displayKoreanName,
        String displayEnglishName,
        String brandIconImageUrl,
        long categoryId,
        String path,
        int regularPrice,
        int currentPrice,
        int salePrice,
        int directDiscountRate,
        int directDiscountPrice,
        int discountRate,
        String optionType,
        String displayYn,
        String soldOutYn,
        String detailDescription,
        String deliveryNotice,
        String refundNotice,
        double averageRating,
        long reviewCount,
        Set<ProductInfoResult> products,
        Set<ProductImageResult> images,
        LocalDateTime insertDate) {

    /**
     * ProductInfoResult - 개별 상품 정보.
     *
     * @param productId 상품 ID
     * @param additionalPrice 추가 가격
     * @param stockQuantity 재고 수량
     * @param soldOutYn 품절 여부
     * @param options 옵션 목록
     */
    public record ProductInfoResult(
            long productId,
            int additionalPrice,
            int stockQuantity,
            String soldOutYn,
            List<OptionInfoResult> options) {}

    /**
     * OptionInfoResult - 옵션 정보.
     *
     * @param optionGroupId 옵션 그룹 ID
     * @param optionGroupName 옵션 그룹명
     * @param optionDetailId 옵션 상세 ID
     * @param optionValue 옵션값
     */
    public record OptionInfoResult(
            long optionGroupId, String optionGroupName, long optionDetailId, String optionValue) {}

    /**
     * ProductImageResult - 상품 이미지 정보.
     *
     * @param productGroupImageId 이미지 ID
     * @param imageType 이미지 유형 (MAIN / SUB 등)
     * @param imageUrl 이미지 URL
     */
    public record ProductImageResult(long productGroupImageId, String imageType, String imageUrl) {}
}
