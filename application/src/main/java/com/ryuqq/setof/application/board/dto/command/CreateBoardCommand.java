package com.ryuqq.setof.application.board.dto.command;

import java.time.Instant;

/**
 * Board 생성 Command
 *
 * @param boardType 게시판 타입 (NOTICE, EVENT, FAQ 등)
 * @param title 제목
 * @param content 내용
 * @param summary 요약
 * @param thumbnailUrl 썸네일 URL
 * @param displayStartAt 노출 시작일시
 * @param displayEndAt 노출 종료일시
 * @param createdBy 작성자 ID
 * @author development-team
 * @since 1.0.0
 */
public record CreateBoardCommand(
        String boardType,
        String title,
        String content,
        String summary,
        String thumbnailUrl,
        Instant displayStartAt,
        Instant displayEndAt,
        Long createdBy) {

    /** 유효성 검증 */
    public CreateBoardCommand {
        if (boardType == null || boardType.isBlank()) {
            throw new IllegalArgumentException("게시판 타입은 필수입니다");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다");
        }
        if (createdBy == null) {
            throw new IllegalArgumentException("작성자 ID는 필수입니다");
        }
    }
}
