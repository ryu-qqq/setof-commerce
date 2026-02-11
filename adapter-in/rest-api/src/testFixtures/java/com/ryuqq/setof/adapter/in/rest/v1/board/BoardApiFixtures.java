package com.ryuqq.setof.adapter.in.rest.v1.board;

import com.ryuqq.setof.adapter.in.rest.v1.board.dto.request.SearchBoardsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.board.dto.response.BoardV1ApiResponse;
import com.ryuqq.setof.application.board.dto.response.BoardPageResult;
import com.ryuqq.setof.application.board.dto.response.BoardResult;
import java.util.List;

/**
 * Board V1 API 테스트 Fixtures.
 *
 * <p>공지사항 관련 API Request/Response 및 Application Result 객체를 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class BoardApiFixtures {

    private BoardApiFixtures() {}

    // ===== SearchBoardsV1ApiRequest =====

    public static SearchBoardsV1ApiRequest searchRequest() {
        return new SearchBoardsV1ApiRequest(0, 20);
    }

    public static SearchBoardsV1ApiRequest searchRequest(Integer page, Integer size) {
        return new SearchBoardsV1ApiRequest(page, size);
    }

    public static SearchBoardsV1ApiRequest searchRequestNullPageSize() {
        return new SearchBoardsV1ApiRequest(null, null);
    }

    // ===== BoardV1ApiResponse =====

    public static BoardV1ApiResponse boardResponse(long boardId) {
        return new BoardV1ApiResponse(boardId, "공지사항 제목입니다", "공지사항 내용입니다");
    }

    public static BoardV1ApiResponse boardResponse(long boardId, String title, String contents) {
        return new BoardV1ApiResponse(boardId, title, contents);
    }

    public static List<BoardV1ApiResponse> boardResponseList() {
        return List.of(boardResponse(1L), boardResponse(2L, "두 번째 공지", "두 번째 공지 내용입니다"));
    }

    // ===== BoardResult =====

    public static BoardResult boardResult(long boardId) {
        return BoardResult.of(boardId, "공지사항 제목입니다", "공지사항 내용입니다");
    }

    public static BoardResult boardResult(long boardId, String title, String contents) {
        return BoardResult.of(boardId, title, contents);
    }

    public static List<BoardResult> boardResultList() {
        return List.of(boardResult(1L), boardResult(2L, "두 번째 공지", "두 번째 공지 내용입니다"));
    }

    // ===== BoardPageResult =====

    public static BoardPageResult boardPageResult() {
        return BoardPageResult.of(boardResultList(), 0, 20, 2L);
    }

    public static BoardPageResult boardPageResult(
            List<BoardResult> content, int page, int size, long totalElements) {
        return BoardPageResult.of(content, page, size, totalElements);
    }

    public static BoardPageResult boardPageResultEmpty() {
        return BoardPageResult.empty();
    }
}
