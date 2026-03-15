package com.ryuqq.setof.application.productgroup.dto.composite;

import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResult;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResult;
import java.time.Instant;

/**
 * 상품 그룹 상세용 Composition 쿼리 결과 DTO.
 *
 * <p>ProductGroup + Seller + Brand + Category + ShippingPolicy + RefundPolicy + Description(1:1) +
 * Notice(1:1) 크로스 도메인 JOIN 결과입니다.
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
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 * @param shippingPolicy 배송 정책 상세
 * @param refundPolicy 환불 정책 상세
 * @param descriptionId 상세설명 ID (nullable)
 * @param descriptionContent 상세설명 HTML (nullable)
 * @param descriptionCdnPath 상세설명 CDN 경로 (nullable)
 * @param noticeId 고시정보 ID (nullable)
 * @param noticeCreatedAt 고시정보 생성일시 (nullable)
 * @param noticeUpdatedAt 고시정보 수정일시 (nullable)
 */
public record ProductGroupDetailCompositeQueryResult(
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
        Instant createdAt,
        Instant updatedAt,
        ShippingPolicyResult shippingPolicy,
        RefundPolicyResult refundPolicy,
        Long descriptionId,
        String descriptionContent,
        String descriptionCdnPath,
        Long noticeId,
        Instant noticeCreatedAt,
        Instant noticeUpdatedAt) {}
