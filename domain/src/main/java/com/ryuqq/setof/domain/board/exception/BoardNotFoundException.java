package com.ryuqq.setof.domain.board.exception;

import com.ryuqq.setof.domain.board.vo.BoardId;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 게시물 Not Found 예외
 *
 * <p>게시물을 찾을 수 없을 때 발생합니다. HTTP 응답: 404 NOT FOUND
 */
public class BoardNotFoundException extends DomainException {

    public BoardNotFoundException(BoardId boardId) {
        super(
                BoardErrorCode.BOARD_NOT_FOUND,
                String.format("게시물을 찾을 수 없습니다: %d", boardId.value()),
                Map.of("boardId", boardId.value()));
    }

    public BoardNotFoundException(Long boardId) {
        super(
                BoardErrorCode.BOARD_NOT_FOUND,
                String.format("게시물을 찾을 수 없습니다: %d", boardId),
                Map.of("boardId", boardId));
    }

    /**
     * ID 값으로 예외 생성 (Static Factory)
     *
     * @param boardId 게시물 ID
     * @return BoardNotFoundException
     */
    public static BoardNotFoundException byId(Long boardId) {
        return new BoardNotFoundException(boardId);
    }
}
