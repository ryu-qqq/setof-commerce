package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * QnaSliceV1ApiResponse - Q&A 슬라이스 응답 DTO (커서 페이징).
 *
 * <p>레거시 Slice 직렬화 형식과 동일한 필드 구조를 유지합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "Q&A 슬라이스 응답 (커서 페이징)")
public record QnaSliceV1ApiResponse(
        @Schema(description = "Q&A 목록") List<QnaV1ApiResponse> content,
        @JsonProperty("last") @Schema(description = "마지막 페이지 여부") boolean last,
        @JsonProperty("first") @Schema(description = "첫 페이지 여부") boolean first,
        @Schema(description = "페이지 번호") int number,
        @Schema(description = "정렬 정보") SortInfo sort,
        @Schema(description = "페이지 크기") int size,
        @Schema(description = "현재 페이지 요소 수") int numberOfElements,
        @Schema(description = "비어있는지 여부") boolean empty,
        @Schema(description = "마지막 도메인 ID (커서)") Long lastDomainId,
        @Schema(description = "커서 값") Long cursorValue,
        @Schema(description = "전체 요소 수") Long totalElements) {

    public static QnaSliceV1ApiResponse of(
            List<QnaV1ApiResponse> content, int size, boolean hasNext, Long lastQnaId) {
        SortInfo sortInfo = new SortInfo(true, false, true);
        boolean isEmpty = content.isEmpty();
        boolean isLast = !hasNext;
        boolean isFirst = true;
        int numberOfElements = content.size();
        return new QnaSliceV1ApiResponse(
                content,
                isLast,
                isFirst,
                0,
                sortInfo,
                size,
                numberOfElements,
                isEmpty,
                lastQnaId,
                lastQnaId,
                null);
    }

    @Schema(description = "정렬 정보")
    public record SortInfo(boolean unsorted, boolean sorted, boolean empty) {}
}
