package com.ryuqq.setof.application.board.port.out;

import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.query.BoardSearchCriteria;
import java.util.List;

/**
 * Board Query Port.
 *
 * <p>공지사항 조회 관련 Port-Out 인터페이스입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface BoardQueryPort {

    List<Board> findByCriteria(BoardSearchCriteria criteria);

    long countByCriteria(BoardSearchCriteria criteria);
}
