package com.ryuqq.setof.adapter.out.persistence.board.adapter;

import com.ryuqq.setof.adapter.out.persistence.board.mapper.BoardJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.board.repository.BoardQueryDslRepository;
import com.ryuqq.setof.application.board.port.out.query.BoardQueryPort;
import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.query.BoardSearchCriteria;
import com.ryuqq.setof.domain.board.vo.BoardId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * BoardQueryAdapter - Board 조회 Adapter (Query)
 *
 * <p>BoardQueryPort를 구현하여 Board의 조회 작업을 처리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class BoardQueryAdapter implements BoardQueryPort {

    private final BoardQueryDslRepository queryDslRepository;
    private final BoardJpaEntityMapper mapper;

    public BoardQueryAdapter(
            BoardQueryDslRepository queryDslRepository, BoardJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Board> findById(BoardId boardId) {
        return queryDslRepository.findById(boardId.value()).map(mapper::toDomain);
    }

    @Override
    public List<Board> findByCriteria(BoardSearchCriteria criteria) {
        return queryDslRepository.findByCriteria(criteria).stream().map(mapper::toDomain).toList();
    }

    @Override
    public long countByCriteria(BoardSearchCriteria criteria) {
        return queryDslRepository.countByCriteria(criteria);
    }

    @Override
    public boolean existsById(BoardId boardId) {
        return queryDslRepository.existsById(boardId.value());
    }
}
