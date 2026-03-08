package com.ryuqq.setof.adapter.in.rest.v1.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * ReviewSliceV1ApiResponse - 내가 작성한 리뷰 커서 기반 페이징 응답 DTO.
 *
 * <p>레거시 CustomSlice 응답 포맷과 동일한 구조로 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-003: Response는 @Schema 어노테이션.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "내가 작성한 리뷰 커서 기반 페이징 응답")
public record ReviewSliceV1ApiResponse(
        @Schema(description = "리뷰 목록") List<ReviewV1ApiResponse> content,
        @Schema(description = "마지막 페이지 여부") boolean last,
        @Schema(description = "첫 페이지 여부") boolean first,
        @Schema(description = "현재 페이지 번호") int number,
        @Schema(description = "정렬 정보") ReviewPageV1ApiResponse.SortResponse sort,
        @Schema(description = "페이지 크기") int size,
        @Schema(description = "현재 페이지 요소 수") int numberOfElements,
        @Schema(description = "빈 페이지 여부") boolean empty,
        @Schema(description = "마지막 도메인 ID") Long lastDomainId,
        @Schema(description = "커서 값") Long cursorValue,
        @Schema(description = "전체 요소 수") long totalElements) {}
