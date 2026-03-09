package com.ryuqq.setof.storage.legacy.composite.review.dto;

/**
 * 레거시 Web Review Image 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param reviewImageId 리뷰 이미지 ID
 * @param reviewId 리뷰 ID
 * @param reviewImageType 이미지 타입
 * @param imageUrl 이미지 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebReviewImageQueryDto(
        long reviewImageId, long reviewId, String reviewImageType, String imageUrl) {}
