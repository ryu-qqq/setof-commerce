package com.ryuqq.setof.application.board.port.out.command;

import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.vo.BoardId;

/**
 * Board 영속성 Outbound Port (Command)
 *
 * <p>저장/수정은 persist()로 통합 (JPA merge 활용)
 *
 * <p>삭제는 Soft Delete (도메인에서 상태 변경 후 persist) 권장
 *
 * @author development-team
 * @since 1.0.0
 */
public interface BoardPersistencePort {

    /**
     * 게시물 저장/수정 (JPA merge 활용)
     *
     * <p>ID가 없으면 INSERT, 있으면 UPDATE
     *
     * @param board 저장/수정할 게시물
     * @return 저장된 Board ID
     */
    BoardId persist(Board board);
}
