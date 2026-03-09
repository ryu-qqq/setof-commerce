package com.ryuqq.setof.adapter.in.rest.v1.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DeleteReviewV1ApiResponse - 리뷰 삭제 V1 응답 DTO.
 *
 * <p>레거시 Review 엔티티 직렬화 호환 (deleteYn = "Y").
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "리뷰 삭제 응답")
public record DeleteReviewV1ApiResponse(
        @Schema(description = "리뷰 ID") long id,
        @Schema(description = "상품그룹 ID") long productGroupId,
        @Schema(description = "사용자 ID") long userId,
        @Schema(description = "주문 ID") long orderId,
        @Schema(description = "평점") double rating,
        @Schema(description = "리뷰 내용") String content,
        @Schema(description = "삭제 여부") String deleteYn,
        @Schema(description = "등록일시") String insertDate,
        @Schema(description = "수정일시") String updateDate) {}
