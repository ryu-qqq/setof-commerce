package com.ryuqq.setof.application.board.port.in.query;

import com.ryuqq.setof.application.board.dto.response.BoardResponse;
import com.ryuqq.setof.domain.board.vo.BoardId;

/**
 * Board 단건 조회 UseCase (Query)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetBoardUseCase {

    /**
     * 게시글 단건 조회
     *
     * @param boardId 게시글 ID
     * @return 게시글 응답
     */
    BoardResponse execute(BoardId boardId);
}
