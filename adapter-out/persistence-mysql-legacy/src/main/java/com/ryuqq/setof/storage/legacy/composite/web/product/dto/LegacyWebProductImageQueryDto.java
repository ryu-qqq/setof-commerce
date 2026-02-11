package com.ryuqq.setof.storage.legacy.composite.web.product.dto;

/**
 * LegacyWebProductImageQueryDto - 레거시 웹 상품 이미지 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * <p>상품그룹 상세 조회의 Query 3: 이미지 (1개 테이블).
 *
 * @param productGroupImageId 이미지 ID
 * @param productGroupId 상품그룹 ID
 * @param imageType 이미지 타입 (MAIN/SUB/DETAIL)
 * @param imageUrl 이미지 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebProductImageQueryDto(
        long productGroupImageId, long productGroupId, String imageType, String imageUrl) {}
