package com.ryuqq.setof.adapter.in.rest.v2.board.dto.response;

import com.ryuqq.setof.application.board.dto.response.BoardResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * Board V2 API Response
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "게시글 응답")
public record BoardV2ApiResponse(
        @Schema(description = "게시글 ID", example = "1") Long id,
        @Schema(description = "게시판 타입", example = "NOTICE") String boardType,
        @Schema(description = "제목", example = "서비스 점검 안내") String title,
        @Schema(description = "내용", example = "12월 26일 서비스 점검이 있습니다.") String content,
        @Schema(description = "요약", example = "서비스 점검 안내") String summary,
        @Schema(description = "썸네일 URL", example = "https://cdn.example.com/thumb.jpg")
                String thumbnailUrl,
        @Schema(description = "상단 고정 여부", example = "true") boolean pinned,
        @Schema(description = "상단 고정 순서", example = "1") int pinOrder,
        @Schema(description = "조회수", example = "1024") long viewCount,
        @Schema(description = "노출 시작일시") Instant displayStartAt,
        @Schema(description = "노출 종료일시") Instant displayEndAt,
        @Schema(description = "생성일시") Instant createdAt) {

    /**
     * Application Response → API Response 변환
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
                response.viewCount(),
                response.displayStartAt(),
                response.displayEndAt(),
                response.createdAt());
    }
}
