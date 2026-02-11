package com.ryuqq.setof.application.board.manager;

import com.ryuqq.setof.application.board.port.out.query.BoardQueryPort;
import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.query.BoardSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Board Read Manager.
 *
 * <p>공지사항 조회 관련 Manager입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class BoardReadManager {

    private final BoardQueryPort queryPort;

    public BoardReadManager(BoardQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public List<Board> findByCriteria(BoardSearchCriteria criteria) {
        return queryPort.findByCriteria(criteria);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(BoardSearchCriteria criteria) {
        return queryPort.countByCriteria(criteria);
    }
}
