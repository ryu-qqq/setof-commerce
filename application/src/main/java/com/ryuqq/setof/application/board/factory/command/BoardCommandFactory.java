package com.ryuqq.setof.application.board.factory.command;

import com.ryuqq.setof.application.board.dto.command.CreateBoardCommand;
import com.ryuqq.setof.application.board.dto.command.UpdateBoardCommand;
import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.vo.BoardContent;
import com.ryuqq.setof.domain.board.vo.BoardType;
import com.ryuqq.setof.domain.board.vo.DisplayPeriod;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Component;

/**
 * Board Command Factory
 *
 * <p>Command DTO를 Domain 객체로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BoardCommandFactory {

    private final ClockHolder clockHolder;

    public BoardCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * CreateBoardCommand → Board 도메인 변환
     *
     * @param command 생성 커맨드
     * @return Board 도메인 객체
     */
    public Board createBoard(CreateBoardCommand command) {
        BoardType boardType = BoardType.valueOf(command.boardType());
        BoardContent content =
                new BoardContent(
                        command.title(),
                        command.content(),
                        command.summary(),
                        command.thumbnailUrl());
        DisplayPeriod displayPeriod =
                command.displayStartAt() != null && command.displayEndAt() != null
                        ? new DisplayPeriod(command.displayStartAt(), command.displayEndAt())
                        : null;

        return Board.forNew(
                boardType,
                content,
                displayPeriod,
                command.createdBy(),
                clockHolder.getClock().instant());
    }

    /**
     * UpdateBoardCommand로 기존 Board 업데이트
     *
     * @param existing 기존 게시글
     * @param command 수정 커맨드
     * @return 수정된 Board
     */
    public Board applyUpdate(Board existing, UpdateBoardCommand command) {
        BoardContent content =
                new BoardContent(
                        command.title(),
                        command.content(),
                        command.summary(),
                        command.thumbnailUrl());
        DisplayPeriod displayPeriod =
                command.displayStartAt() != null && command.displayEndAt() != null
                        ? new DisplayPeriod(command.displayStartAt(), command.displayEndAt())
                        : null;

        return existing.update(
                content, displayPeriod, command.updatedBy(), clockHolder.getClock().instant());
    }
}
