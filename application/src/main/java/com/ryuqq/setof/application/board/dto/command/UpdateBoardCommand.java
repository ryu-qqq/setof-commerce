package com.ryuqq.setof.application.board.dto.command;

import java.time.Instant;

/**
 * Board 수정 Command
 *
 * @param boardId 게시글 ID
 * @param title 제목
 * @param content 내용
 * @param summary 요약
 * @param thumbnailUrl 썸네일 URL
 * @param displayStartAt 노출 시작일시
 * @param displayEndAt 노출 종료일시
 * @param updatedBy 수정자 ID
 * @author development-team
 * @since 1.0.0
 */
public record UpdateBoardCommand(
        Long boardId,
        String title,
        String content,
        String summary,
        String thumbnailUrl,
        Instant displayStartAt,
        Instant displayEndAt,
        Long updatedBy) {

    /** 유효성 검증 */
    public UpdateBoardCommand {
        if (boardId == null) {
            throw new IllegalArgumentException("게시글 ID는 필수입니다");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다");
        }
        if (updatedBy == null) {
            throw new IllegalArgumentException("수정자 ID는 필수입니다");
        }
    }
}
