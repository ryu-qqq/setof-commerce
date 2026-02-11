package com.ryuqq.setof.storage.legacy.board.adapter;

import com.ryuqq.setof.application.board.port.out.query.BoardQueryPort;
import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.query.BoardSearchCriteria;
import com.ryuqq.setof.storage.legacy.board.entity.LegacyBoardEntity;
import com.ryuqq.setof.storage.legacy.board.mapper.LegacyBoardEntityMapper;
import com.ryuqq.setof.storage.legacy.board.repository.LegacyBoardQueryDslRepository;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * LegacyBoardQueryAdapter - 레거시 게시판 Query 어댑터.
 *
 * <p>BoardQueryPort를 구현하여 레거시 DB와 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>활성화 조건: persistence.legacy.board.enabled=true
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.board.enabled", havingValue = "true")
public class LegacyBoardQueryAdapter implements BoardQueryPort {

    private final LegacyBoardQueryDslRepository queryDslRepository;
    private final LegacyBoardEntityMapper mapper;

    public LegacyBoardQueryAdapter(
            LegacyBoardQueryDslRepository queryDslRepository, LegacyBoardEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Board> findByCriteria(BoardSearchCriteria criteria) {
        List<LegacyBoardEntity> entities = queryDslRepository.findByCriteria(criteria);
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public long countByCriteria(BoardSearchCriteria criteria) {
        return queryDslRepository.countByCriteria(criteria);
    }
}
