package com.ryuqq.setof.application.review.dto.command;

/**
 * DeleteReviewCommand - 리뷰 삭제 커맨드.
 *
 * @param reviewId 리뷰 ID
 * @param userId 레거시 회원 ID (본인 확인용)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DeleteReviewCommand(long reviewId, long userId) {}
