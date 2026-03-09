package com.ryuqq.setof.adapter.in.rest.v1.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * RegisterReviewV1ApiResponse - 리뷰 등록 V1 응답 DTO.
 *
 * <p>레거시 Review 엔티티 직렬화 호환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "리뷰 등록 응답")
public record RegisterReviewV1ApiResponse(
        @Schema(description = "리뷰 ID") Long id,
        @Schema(description = "상품그룹 ID") long productGroupId,
        @Schema(description = "사용자 ID") long userId,
        @Schema(description = "주문 ID") long orderId,
        @Schema(description = "평점") double rating,
        @Schema(description = "리뷰 내용") String content,
        @Schema(description = "삭제 여부") String deleteYn,
        @Schema(description = "등록일시") String insertDate,
        @Schema(description = "수정일시") String updateDate,
        @Schema(description = "등록자") String insertOperator,
        @Schema(description = "수정자") String updateOperator) {}
