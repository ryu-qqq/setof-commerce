package com.ryuqq.setof.adapter.out.persistence.board.mapper;

import com.ryuqq.setof.adapter.out.persistence.board.entity.BoardJpaEntity;
import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.vo.BoardContent;
import com.ryuqq.setof.domain.board.vo.BoardId;
import com.ryuqq.setof.domain.board.vo.BoardStatus;
import com.ryuqq.setof.domain.board.vo.BoardType;
import com.ryuqq.setof.domain.board.vo.DisplayPeriod;
import com.ryuqq.setof.domain.board.vo.PinSetting;
import org.springframework.stereotype.Component;

/**
 * BoardJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer와 Domain Layer 간의 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BoardJpaEntityMapper {

    /** Domain -> Entity 변환 */
    public BoardJpaEntity toEntity(Board domain) {
        return BoardJpaEntity.of(
                domain.getIdValue(),
                domain.getBoardType().name(),
                domain.getTitle(),
                domain.getContentValue(),
                domain.getSummary(),
                domain.getThumbnailUrl(),
                domain.isPinned(),
                domain.getPinOrder(),
                domain.getDisplayStartAt(),
                domain.getDisplayEndAt(),
                domain.getStatus().name(),
                domain.getViewCount(),
                domain.getCreatedBy(),
                domain.getUpdatedBy(),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getDeletedAt());
    }

    /** Entity -> Domain 변환 */
    public Board toDomain(BoardJpaEntity entity) {
        BoardId boardId = BoardId.of(entity.getId());
        BoardType boardType = BoardType.valueOf(entity.getBoardType());
        BoardContent content =
                new BoardContent(
                        entity.getTitle(),
                        entity.getContent(),
                        entity.getSummary(),
                        entity.getThumbnailUrl());
        PinSetting pinSetting =
                entity.isPinned() ? PinSetting.pinned(entity.getPinOrder()) : PinSetting.unpinned();
        DisplayPeriod displayPeriod =
                new DisplayPeriod(entity.getDisplayStartAt(), entity.getDisplayEndAt());
        BoardStatus status = BoardStatus.valueOf(entity.getStatus());

        return Board.reconstitute(
                boardId,
                boardType,
                content,
                pinSetting,
                displayPeriod,
                status,
                entity.getViewCount(),
                entity.getCreatedBy(),
                entity.getUpdatedBy(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt());
    }
}
