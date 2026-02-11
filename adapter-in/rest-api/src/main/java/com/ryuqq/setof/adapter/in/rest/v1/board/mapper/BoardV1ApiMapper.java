package com.ryuqq.setof.adapter.in.rest.v1.board.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.board.dto.request.SearchBoardsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.board.dto.response.BoardV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.application.board.dto.query.BoardSearchParams;
import com.ryuqq.setof.application.board.dto.response.BoardPageResult;
import com.ryuqq.setof.application.board.dto.response.BoardResult;
import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * BoardV1ApiMapper - 공지사항 V1 API Request/Response 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-002: 양방향 변환 지원.
 *
 * <p>API-MAP-003: Application Result → API Response 변환.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class BoardV1ApiMapper {

    private static final String DEFAULT_SORT_KEY = "createdAt";
    private static final String DEFAULT_SORT_DIRECTION = "DESC";
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * SearchBoardsV1ApiRequest → BoardSearchParams 변환.
     *
     * <p>page/size null인 경우 기본값 적용 (page=0, size=20).
     *
     * @param request 공지사항 검색 요청 DTO
     * @return BoardSearchParams
     */
    public BoardSearchParams toSearchParams(SearchBoardsV1ApiRequest request) {
        int page = request.page() != null ? request.page() : DEFAULT_PAGE;
        int size = request.size() != null ? request.size() : DEFAULT_PAGE_SIZE;

        CommonSearchParams searchParams =
                CommonSearchParams.of(
                        false, null, null, DEFAULT_SORT_KEY, DEFAULT_SORT_DIRECTION, page, size);

        return BoardSearchParams.of(searchParams);
    }

    /**
     * BoardPageResult → CustomPageableV1ApiResponse 변환.
     *
     * <p>레거시 CustomPageable 구조 호환.
     *
     * @param pageResult Application 페이지 결과
     * @return CustomPageableV1ApiResponse
     */
    public CustomPageableV1ApiResponse<BoardV1ApiResponse> toPageResponse(
            BoardPageResult pageResult) {
        List<BoardV1ApiResponse> content =
                pageResult.content().stream().map(this::toResponse).toList();
        return CustomPageableV1ApiResponse.of(
                content,
                pageResult.pageMeta().page(),
                pageResult.pageMeta().size(),
                pageResult.pageMeta().totalElements());
    }

    /**
     * BoardResult → BoardV1ApiResponse 변환.
     *
     * @param result BoardResult
     * @return BoardV1ApiResponse
     */
    public BoardV1ApiResponse toResponse(BoardResult result) {
        return new BoardV1ApiResponse(result.boardId(), result.title(), result.contents());
    }
}
