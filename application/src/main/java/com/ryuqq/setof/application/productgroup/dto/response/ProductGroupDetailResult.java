package com.ryuqq.setof.application.productgroup.dto.response;

import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeResult;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * ProductGroupDetailResult - 상품그룹 상세 응답 DTO.
 *
 * <p>fetchProductGroup 엔드포인트의 단건 상세 조회 응답입니다. V1 ApiMapper에서 레거시 ProductGroupResponse 포맷으로 변환합니다.
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
public record ProductGroupDetailResult(
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
        Set<ProductGroupDetailCompositeResult.ProductInfoResult> products,
        Set<ProductGroupDetailCompositeResult.ProductImageResult> images,
        LocalDateTime insertDate) {

    public static ProductGroupDetailResult from(ProductGroupDetailCompositeResult composite) {
        return new ProductGroupDetailResult(
                composite.productGroupId(),
                composite.productGroupName(),
                composite.sellerId(),
                composite.sellerName(),
                composite.brandId(),
                composite.brandName(),
                composite.displayKoreanName(),
                composite.displayEnglishName(),
                composite.brandIconImageUrl(),
                composite.categoryId(),
                composite.path(),
                composite.regularPrice(),
                composite.currentPrice(),
                composite.salePrice(),
                composite.directDiscountRate(),
                composite.directDiscountPrice(),
                composite.discountRate(),
                composite.optionType(),
                composite.displayYn(),
                composite.soldOutYn(),
                composite.detailDescription(),
                composite.deliveryNotice(),
                composite.refundNotice(),
                composite.averageRating(),
                composite.reviewCount(),
                composite.products(),
                composite.images(),
                composite.insertDate());
    }
}
