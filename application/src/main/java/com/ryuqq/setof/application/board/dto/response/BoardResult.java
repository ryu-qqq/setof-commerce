package com.ryuqq.setof.application.board.dto.response;

/**
 * 공지사항 조회 결과.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param boardId 공지사항 ID
 * @param title 제목
 * @param contents 내용
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BoardResult(Long boardId, String title, String contents) {

    public static BoardResult of(Long boardId, String title, String contents) {
        return new BoardResult(boardId, title, contents);
    }
}
