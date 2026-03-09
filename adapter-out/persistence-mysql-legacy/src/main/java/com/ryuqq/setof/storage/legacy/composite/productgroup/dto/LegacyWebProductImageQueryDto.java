package com.ryuqq.setof.storage.legacy.composite.productgroup.dto;

/**
 * LegacyWebProductImageQueryDto - 레거시 Web 상품그룹 이미지 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * <p>fetchImages 쿼리(쿼리 3)에서 product_group_image 조회 결과를 담습니다.
 *
 * @param productGroupImageId 이미지 ID
 * @param imageType 이미지 타입 (MAIN/SUB/DETAIL)
 * @param imageUrl 이미지 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebProductImageQueryDto(
        long productGroupImageId, String imageType, String imageUrl) {}
