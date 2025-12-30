package com.ryuqq.setof.application.board.assembler;

import com.ryuqq.setof.application.board.dto.response.BoardResponse;
import com.ryuqq.setof.domain.board.aggregate.Board;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Board Assembler
 *
 * <p>Domain → Response DTO 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BoardAssembler {

    /**
     * Board 도메인 → BoardResponse 변환
     *
     * @param board 게시글 도메인
     * @return BoardResponse DTO
     */
    public BoardResponse toResponse(Board board) {
        return BoardResponse.of(
                board.getIdValue(),
                board.getBoardType().name(),
                board.getTitle(),
                board.getContentValue(),
                board.getSummary(),
                board.getThumbnailUrl(),
                board.isPinned(),
                board.getPinOrder(),
                board.getStatus().name(),
                board.getViewCount(),
                board.getDisplayStartAt(),
                board.getDisplayEndAt(),
                board.getCreatedAt(),
                board.getUpdatedAt());
    }

    /**
     * Board 목록 → BoardResponse 목록 변환
     *
     * @param boards 게시글 목록
     * @return BoardResponse 목록
     */
    public List<BoardResponse> toResponses(List<Board> boards) {
        return boards.stream().map(this::toResponse).toList();
    }
}
