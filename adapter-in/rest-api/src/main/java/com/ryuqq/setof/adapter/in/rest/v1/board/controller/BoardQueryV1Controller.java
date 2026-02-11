package com.ryuqq.setof.adapter.in.rest.v1.board.controller;

import com.ryuqq.setof.adapter.in.rest.v1.board.BoardV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.board.dto.request.SearchBoardsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.board.dto.response.BoardV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.board.mapper.BoardV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.application.board.dto.query.BoardSearchParams;
import com.ryuqq.setof.application.board.dto.response.BoardPageResult;
import com.ryuqq.setof.application.board.port.in.query.SearchBoardByOffsetUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BoardQueryV1Controller - 공지사항 조회 V1 Public API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * <p>API-CTR-005: Controller @Transactional 금지.
 *
 * <p>API-CTR-007: Controller 비즈니스 로직 금지.
 *
 * <p>레거시 NewsController.fetchBoard 흐름 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "공지사항 조회 V1", description = "공지사항 조회 V1 Public API")
@RestController
@RequestMapping(BoardV1Endpoints.BOARDS)
public class BoardQueryV1Controller {

    private final SearchBoardByOffsetUseCase searchBoardByOffsetUseCase;
    private final BoardV1ApiMapper mapper;

    public BoardQueryV1Controller(
            SearchBoardByOffsetUseCase searchBoardByOffsetUseCase, BoardV1ApiMapper mapper) {
        this.searchBoardByOffsetUseCase = searchBoardByOffsetUseCase;
        this.mapper = mapper;
    }

    /**
     * 공지사항 목록 조회 API.
     *
     * <p>GET /api/v1/board - 페이지네이션 기반 공지사항 목록 조회.
     *
     * @param request 검색 요청 (page, size 옵션)
     * @return 공지사항 목록 (페이징)
     */
    @Operation(summary = "공지사항 목록 조회", description = "페이지네이션 기반으로 공지사항 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<V1ApiResponse<CustomPageableV1ApiResponse<BoardV1ApiResponse>>> getBoards(
            @ParameterObject @Valid SearchBoardsV1ApiRequest request) {
        BoardSearchParams params = mapper.toSearchParams(request);
        BoardPageResult pageResult = searchBoardByOffsetUseCase.execute(params);
        CustomPageableV1ApiResponse<BoardV1ApiResponse> response =
                mapper.toPageResponse(pageResult);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
