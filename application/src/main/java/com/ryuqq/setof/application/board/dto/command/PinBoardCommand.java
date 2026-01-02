package com.ryuqq.setof.application.board.dto.command;

/**
 * Board 상단 고정 Command
 *
 * @param boardId 게시글 ID
 * @param pinOrder 상단 고정 순서
 * @param updatedBy 수정자 ID
 * @author development-team
 * @since 1.0.0
 */
public record PinBoardCommand(Long boardId, int pinOrder, Long updatedBy) {

    /** 유효성 검증 */
    public PinBoardCommand {
        if (boardId == null) {
            throw new IllegalArgumentException("게시글 ID는 필수입니다");
        }
        if (pinOrder < 0) {
            throw new IllegalArgumentException("상단 고정 순서는 0 이상이어야 합니다");
        }
        if (updatedBy == null) {
            throw new IllegalArgumentException("수정자 ID는 필수입니다");
        }
    }
}
