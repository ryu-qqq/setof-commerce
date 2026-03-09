package com.ryuqq.setof.storage.legacy.composite.review.dto;

/**
 * 레거시 Web Review Option 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param reviewId 리뷰 ID (또는 orderId)
 * @param optionGroupId 옵션그룹 ID
 * @param optionDetailId 옵션상세 ID
 * @param optionName 옵션명
 * @param optionValue 옵션값
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebReviewOptionQueryDto(
        long reviewId,
        long optionGroupId,
        long optionDetailId,
        String optionName,
        String optionValue) {}
