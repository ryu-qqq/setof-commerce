package com.ryuqq.setof.adapter.out.persistence.board.adapter;

import com.ryuqq.setof.adapter.out.persistence.board.entity.BoardJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.board.mapper.BoardJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.board.repository.BoardJpaRepository;
import com.ryuqq.setof.application.board.port.out.command.BoardPersistencePort;
import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.vo.BoardId;
import org.springframework.stereotype.Repository;

/**
 * BoardPersistenceAdapter - Board 영속성 Adapter (Command)
 *
 * <p>BoardPersistencePort를 구현하여 Board의 영속성 작업을 처리합니다.
 *
 * <p>저장/수정은 persist()로 통합 (JPA merge 활용)
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class BoardPersistenceAdapter implements BoardPersistencePort {

    private final BoardJpaRepository jpaRepository;
    private final BoardJpaEntityMapper mapper;

    public BoardPersistenceAdapter(BoardJpaRepository jpaRepository, BoardJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public BoardId persist(Board board) {
        BoardJpaEntity entity = mapper.toEntity(board);
        BoardJpaEntity saved = jpaRepository.save(entity);
        return BoardId.of(saved.getId());
    }
}
