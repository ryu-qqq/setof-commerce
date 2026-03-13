package com.ryuqq.setof.application.productgroup.dto.composite;

import com.ryuqq.setof.application.productgroup.dto.response.ProductOptionMatrixResult;
import com.ryuqq.setof.application.productgroupdescription.dto.response.ProductGroupDescriptionResult;
import com.ryuqq.setof.application.productgroupimage.dto.response.ProductGroupImageResult;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResult;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResult;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResult;
import java.time.Instant;
import java.util.List;

/**
 * 웹(사용자) 상품 그룹 상세 Composite 결과 DTO.
 *
 * <p>사용자 화면에 필요한 브랜드 아이콘, 평점/리뷰 정보를 포함합니다. Admin용 CompositeResult와 동일한 번들에서 조립되지만 사용자 관점에 맞게 구성됩니다.
 *
 * @param id 상품 그룹 ID
 * @param sellerId 셀러 ID
 * @param sellerName 셀러명
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param brandIconImageUrl 브랜드 아이콘 이미지 URL
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리명
 * @param categoryPath 카테고리 ID 경로
 * @param productGroupName 상품 그룹명
 * @param optionType 옵션 유형
 * @param status 상품 그룹 상태
 * @param averageRating 평균 평점 (현재 0.0 고정, 추후 리뷰 도메인 연동)
 * @param reviewCount 리뷰 수 (현재 0 고정, 추후 리뷰 도메인 연동)
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 * @param images 상품 그룹 이미지 목록
 * @param optionProductMatrix 옵션 구조 + 상품(SKU) 통합 매트릭스
 * @param shippingPolicy 배송 정책 상세
 * @param refundPolicy 환불 정책 상세
 * @param description 상품 상세설명 (nullable)
 * @param productNotice 상품 고시정보 (nullable)
 */
public record WebProductGroupDetailCompositeResult(
        Long id,
        Long sellerId,
        String sellerName,
        Long brandId,
        String brandName,
        String brandIconImageUrl,
        Long categoryId,
        String categoryName,
        String categoryPath,
        String productGroupName,
        String optionType,
        String status,
        double averageRating,
        long reviewCount,
        Instant createdAt,
        Instant updatedAt,
        List<ProductGroupImageResult> images,
        ProductOptionMatrixResult optionProductMatrix,
        ShippingPolicyResult shippingPolicy,
        RefundPolicyResult refundPolicy,
        ProductGroupDescriptionResult description,
        ProductNoticeResult productNotice) {

    public WebProductGroupDetailCompositeResult {
        images = images != null ? List.copyOf(images) : List.of();
    }
}
