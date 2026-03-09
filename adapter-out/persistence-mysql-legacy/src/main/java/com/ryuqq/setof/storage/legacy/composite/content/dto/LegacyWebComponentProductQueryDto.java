package com.ryuqq.setof.storage.legacy.composite.content.dto;

import java.time.LocalDateTime;

/**
 * LegacyWebComponentProductQueryDto - 컴포넌트 상품 썸네일 조회 DTO.
 *
 * <p>component -> component_target -> component_item -> product_group 조인 결과를 담는 Projection DTO.
 *
 * @param componentId 컴포넌트 ID
 * @param tabId 탭 ID
 * @param sortType 정렬 타입
 * @param productGroupId 상품 그룹 ID
 * @param sellerId 판매자 ID
 * @param productGroupName 상품 그룹명
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param productImageUrl 상품 이미지 URL
 * @param regularPrice 정가
 * @param currentPrice 현재가
 * @param salePrice 판매가
 * @param directDiscountRate 직접 할인율
 * @param directDiscountPrice 직접 할인 금액
 * @param discountRate 할인율
 * @param insertDate 등록일시
 * @param averageRating 평균 평점
 * @param reviewCount 리뷰 수
 * @param score 점수
 * @param displayYn 노출 여부
 * @param soldOutYn 품절 여부
 * @param displayOrder 노출 순서
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebComponentProductQueryDto(
        long componentId,
        long tabId,
        String sortType,
        long productGroupId,
        long sellerId,
        String productGroupName,
        long brandId,
        String brandName,
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
        String soldOutYn,
        Long displayOrder) {}
