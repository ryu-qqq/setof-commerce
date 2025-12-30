package com.ryuqq.setof.application.board.dto.command;

/**
 * Board 숨김 Command
 *
 * @param boardId 게시글 ID
 * @param updatedBy 수정자 ID
 * @author development-team
 * @since 1.0.0
 */
public record HideBoardCommand(Long boardId, Long updatedBy) {

    /** 유효성 검증 */
    public HideBoardCommand {
        if (boardId == null) {
            throw new IllegalArgumentException("게시글 ID는 필수입니다");
        }
        if (updatedBy == null) {
            throw new IllegalArgumentException("수정자 ID는 필수입니다");
        }
    }
}
