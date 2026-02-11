package com.ryuqq.setof.storage.legacy.board.mapper;

import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.id.BoardId;
import com.ryuqq.setof.domain.board.vo.BoardContents;
import com.ryuqq.setof.domain.board.vo.BoardTitle;
import com.ryuqq.setof.storage.legacy.board.entity.LegacyBoardEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * LegacyBoardEntityMapper - 레거시 게시판 Entity-Domain 매퍼.
 *
 * <p>레거시 Entity → 새 Domain 변환을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyBoardEntityMapper {

    /**
     * 레거시 Entity → Domain 변환.
     *
     * @param entity LegacyBoardEntity
     * @return Board 도메인 객체
     */
    public Board toDomain(LegacyBoardEntity entity) {
        Instant createdAt = toInstant(entity.getInsertDate());
        Instant updatedAt = toInstant(entity.getUpdateDate());

        return Board.reconstitute(
                BoardId.of(entity.getId()),
                BoardTitle.of(entity.getTitle()),
                BoardContents.of(entity.getContents()),
                createdAt,
                updatedAt);
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return Instant.now();
        }
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}
