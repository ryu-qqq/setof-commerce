package com.ryuqq.setof.application.board.assembler;

import com.ryuqq.setof.application.board.dto.response.BoardPageResult;
import com.ryuqq.setof.application.board.dto.response.BoardResult;
import com.ryuqq.setof.domain.board.aggregate.Board;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Board Assembler.
 *
 * <p>Domain → Result 변환 및 PageResult 생성을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class BoardAssembler {

    /**
     * Domain → BoardResult 변환.
     *
     * @param board Board 도메인 객체
     * @return BoardResult
     */
    public BoardResult toResult(Board board) {
        return BoardResult.of(board.idValue(), board.titleValue(), board.contentsValue());
    }

    /**
     * Domain List → BoardResult List 변환.
     *
     * @param boards Board 도메인 객체 목록
     * @return BoardResult 목록
     */
    public List<BoardResult> toResults(List<Board> boards) {
        return boards.stream().map(this::toResult).toList();
    }

    /**
     * 페이지 결과 생성.
     *
     * @param boards Board 도메인 객체 목록
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param totalCount 전체 개수
     * @return BoardPageResult
     */
    public BoardPageResult toPageResult(List<Board> boards, int page, int size, long totalCount) {
        List<BoardResult> results = toResults(boards);
        return BoardPageResult.of(results, page, size, totalCount);
    }
}
