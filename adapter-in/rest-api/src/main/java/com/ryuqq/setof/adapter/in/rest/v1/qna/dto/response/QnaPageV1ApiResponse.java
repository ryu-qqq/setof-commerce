package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * QnaPageV1ApiResponse - Q&A 페이지 응답 DTO (Offset 페이징).
 *
 * <p>레거시 Spring Page 직렬화 형식과 동일한 필드 구조를 유지합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "Q&A 페이지 응답 (Offset 페이징)")
public record QnaPageV1ApiResponse(
        @Schema(description = "Q&A 목록") List<QnaV1ApiResponse> content,
        @Schema(description = "페이지 정보") PageableInfo pageable,
        @Schema(description = "전체 페이지 수") int totalPages,
        @Schema(description = "전체 요소 수") long totalElements,
        @JsonProperty("last") @Schema(description = "마지막 페이지 여부") boolean last,
        @Schema(description = "현재 페이지 요소 수") int numberOfElements,
        @JsonProperty("first") @Schema(description = "첫 페이지 여부") boolean first,
        @Schema(description = "페이지 크기") int size,
        @Schema(description = "페이지 번호") int number,
        @Schema(description = "정렬 정보") SortInfo sort,
        @Schema(description = "비어있는지 여부") boolean empty) {

    public static QnaPageV1ApiResponse of(
            List<QnaV1ApiResponse> content,
            int page,
            int size,
            long totalElements,
            int totalPages) {
        SortInfo sortInfo = new SortInfo(true, false, true);
        PageableInfo pageableInfo =
                new PageableInfo(page, size, sortInfo, (long) page * size, false, true);
        int numberOfElements = content.size();
        boolean isFirst = page == 0;
        boolean isLast = page >= totalPages - 1 || totalPages == 0;
        boolean isEmpty = content.isEmpty();
        return new QnaPageV1ApiResponse(
                content,
                pageableInfo,
                totalPages,
                totalElements,
                isLast,
                numberOfElements,
                isFirst,
                size,
                page,
                sortInfo,
                isEmpty);
    }

    @Schema(description = "페이지 정보")
    public record PageableInfo(
            int pageNumber,
            int pageSize,
            SortInfo sort,
            long offset,
            boolean unpaged,
            boolean paged) {}

    @Schema(description = "정렬 정보")
    public record SortInfo(boolean unsorted, boolean sorted, boolean empty) {}
}
