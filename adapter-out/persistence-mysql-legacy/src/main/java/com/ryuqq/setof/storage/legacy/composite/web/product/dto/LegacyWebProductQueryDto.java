package com.ryuqq.setof.storage.legacy.composite.web.product.dto;

/**
 * LegacyWebProductQueryDto - 레거시 웹 상품 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * <p>상품그룹 상세 조회의 Query 2: 상품+옵션 (5개 테이블).
 *
 * @param productId 상품 ID
 * @param productGroupId 상품그룹 ID
 * @param additionalPrice 추가금
 * @param stockQuantity 재고 수량
 * @param soldOutYn 품절 여부
 * @param optionGroupId 옵션 그룹 ID (nullable)
 * @param optionGroupName 옵션 그룹명 (nullable)
 * @param optionDetailId 옵션 상세 ID (nullable)
 * @param optionValue 옵션 값 (nullable)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebProductQueryDto(
        long productId,
        long productGroupId,
        int additionalPrice,
        int stockQuantity,
        String soldOutYn,
        Long optionGroupId,
        String optionGroupName,
        Long optionDetailId,
        String optionValue) {}
