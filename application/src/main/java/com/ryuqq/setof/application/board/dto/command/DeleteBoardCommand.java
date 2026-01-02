package com.ryuqq.setof.application.board.dto.command;

/**
 * Board 삭제 Command
 *
 * @param boardId 게시글 ID
 * @param deletedBy 삭제자 ID
 * @author development-team
 * @since 1.0.0
 */
public record DeleteBoardCommand(Long boardId, Long deletedBy) {

    /** 유효성 검증 */
    public DeleteBoardCommand {
        if (boardId == null) {
            throw new IllegalArgumentException("게시글 ID는 필수입니다");
        }
        if (deletedBy == null) {
            throw new IllegalArgumentException("삭제자 ID는 필수입니다");
        }
    }
}
