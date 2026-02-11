package com.ryuqq.setof.application.board.port.in.query;

import com.ryuqq.setof.application.board.dto.query.BoardSearchParams;
import com.ryuqq.setof.application.board.dto.response.BoardPageResult;

/**
 * 공지사항 검색 UseCase (Offset 기반 페이징).
 *
 * <p>APP-UC-001: Offset 페이징은 Search{Domain}ByOffsetUseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface SearchBoardByOffsetUseCase {

    /**
     * 공지사항을 검색합니다.
     *
     * @param params 검색 파라미터
     * @return 공지사항 페이지 결과
     */
    BoardPageResult execute(BoardSearchParams params);
}
