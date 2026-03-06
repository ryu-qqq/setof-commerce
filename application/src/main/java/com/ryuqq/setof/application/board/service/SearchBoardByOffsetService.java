package com.ryuqq.setof.application.board.service;

import com.ryuqq.setof.application.board.assembler.BoardAssembler;
import com.ryuqq.setof.application.board.dto.query.BoardSearchParams;
import com.ryuqq.setof.application.board.dto.response.BoardPageResult;
import com.ryuqq.setof.application.board.factory.BoardQueryFactory;
import com.ryuqq.setof.application.board.manager.BoardReadManager;
import com.ryuqq.setof.application.board.port.in.SearchBoardByOffsetUseCase;
import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.query.BoardSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 공지사항 검색 Service (Offset 기반 페이징).
 *
 * <p>APP-UC-001: Offset 페이징은 Search{Domain}ByOffsetService
 *
 * <p>QueryFactory를 통해 Params → Criteria 변환
 *
 * <p>Assembler를 통해 BoardPageResult 생성
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class SearchBoardByOffsetService implements SearchBoardByOffsetUseCase {

    private final BoardReadManager readManager;
    private final BoardQueryFactory queryFactory;
    private final BoardAssembler assembler;

    public SearchBoardByOffsetService(
            BoardReadManager readManager,
            BoardQueryFactory queryFactory,
            BoardAssembler assembler) {
        this.readManager = readManager;
        this.queryFactory = queryFactory;
        this.assembler = assembler;
    }

    @Override
    public BoardPageResult execute(BoardSearchParams params) {
        BoardSearchCriteria criteria = queryFactory.createCriteria(params);

        List<Board> boards = readManager.findByCriteria(criteria);
        long totalElements = readManager.countByCriteria(criteria);

        return assembler.toPageResult(boards, params.page(), params.size(), totalElements);
    }
}
