package com.ryuqq.setof.adapter.in.rest.v1.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 리뷰 등록 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "리뷰 등록 응답")
public record CreateReviewV1ApiResponse(@Schema(description = "리뷰 ID", example = "1") Long id) {}
