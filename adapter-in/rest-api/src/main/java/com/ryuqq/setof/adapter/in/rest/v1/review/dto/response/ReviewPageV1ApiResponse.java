package com.ryuqq.setof.adapter.in.rest.v1.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * ReviewPageV1ApiResponse - 리뷰 페이지 응답 DTO.
 *
 * <p>레거시 ReviewPage 기반 변환. 평균 평점 포함.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "리뷰 페이지 응답 (평균 평점 포함)")
public record ReviewPageV1ApiResponse(
        @Schema(description = "평균 평점", example = "4.5") double averageRating,
        @Schema(description = "리뷰 목록") List<ReviewV1ApiResponse> content,
        @Schema(description = "총 페이지 수", example = "10") int totalPages,
        @Schema(description = "총 리뷰 수", example = "100") long totalElements,
        @Schema(description = "마지막 페이지 여부", example = "false") boolean last,
        @Schema(description = "첫 페이지 여부", example = "true") boolean first,
        @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0") int number,
        @Schema(description = "페이지 크기", example = "20") int size,
        @Schema(description = "현재 페이지 요소 수", example = "20") int numberOfElements,
        @Schema(description = "빈 페이지 여부", example = "false") boolean empty) {}
