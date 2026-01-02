package com.ryuqq.setof.adapter.in.rest.admin.v2.board.dto.response;

import com.ryuqq.setof.application.board.dto.response.BoardResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * Board 응답 DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "게시글 응답")
public record BoardV2ApiResponse(
        @Schema(description = "게시글 ID", example = "1") Long id,
        @Schema(description = "게시판 타입", example = "NOTICE") String boardType,
        @Schema(description = "제목", example = "공지사항 제목") String title,
        @Schema(description = "내용", example = "공지사항 내용입니다.") String content,
        @Schema(description = "요약", example = "공지사항 요약") String summary,
        @Schema(description = "썸네일 URL", example = "https://example.com/thumbnail.jpg")
                String thumbnailUrl,
        @Schema(description = "상단 고정 여부", example = "false") boolean pinned,
        @Schema(description = "상단 고정 순서", example = "0") int pinOrder,
        @Schema(description = "상태", example = "PUBLISHED") String status,
        @Schema(description = "조회수", example = "100") long viewCount,
        @Schema(description = "노출 시작일시", example = "2024-01-01T00:00:00Z") Instant displayStartAt,
        @Schema(description = "노출 종료일시", example = "2024-12-31T23:59:59Z") Instant displayEndAt,
        @Schema(description = "생성일시", example = "2024-01-01T00:00:00Z") Instant createdAt,
        @Schema(description = "수정일시", example = "2024-01-01T00:00:00Z") Instant updatedAt) {

    /**
     * Application BoardResponse → API Response 변환
     *
     * @param response Application Response
     * @return API Response
     */
    public static BoardV2ApiResponse from(BoardResponse response) {
        return new BoardV2ApiResponse(
                response.id(),
                response.boardType(),
                response.title(),
                response.content(),
                response.summary(),
                response.thumbnailUrl(),
                response.pinned(),
                response.pinOrder(),
                response.status(),
                response.viewCount(),
                response.displayStartAt(),
                response.displayEndAt(),
                response.createdAt(),
                response.updatedAt());
    }
}
