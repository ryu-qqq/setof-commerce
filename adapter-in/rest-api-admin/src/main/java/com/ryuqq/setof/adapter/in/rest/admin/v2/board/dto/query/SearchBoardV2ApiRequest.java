package com.ryuqq.setof.adapter.in.rest.admin.v2.board.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Board 검색 요청 DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "게시글 검색 요청")
public record SearchBoardV2ApiRequest(
        @Schema(description = "게시판 타입", example = "NOTICE") String boardType,
        @Schema(description = "상태 (DRAFT, PUBLISHED, HIDDEN)", example = "PUBLISHED") String status,
        @Schema(description = "상단 고정 여부", example = "true") Boolean pinned,
        @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0") @Min(0)
                Integer page,
        @Schema(description = "페이지 크기", example = "20", defaultValue = "20") @Min(1) @Max(100)
                Integer size) {

    public SearchBoardV2ApiRequest {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 20;
        }
    }

    /**
     * 페이징 offset 계산
     *
     * @return offset
     */
    public long getOffset() {
        return (long) page * size;
    }

    /**
     * 페이징 limit 반환
     *
     * @return limit
     */
    public int getSize() {
        return size;
    }
}
