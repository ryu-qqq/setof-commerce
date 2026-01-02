package com.ryuqq.setof.adapter.in.rest.v2.board.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.board.dto.query.SearchBoardV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.board.dto.response.BoardV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.board.mapper.BoardV2ApiMapper;
import com.ryuqq.setof.application.board.dto.query.SearchBoardQuery;
import com.ryuqq.setof.application.board.dto.response.BoardResponse;
import com.ryuqq.setof.application.board.port.in.query.GetBoardUseCase;
import com.ryuqq.setof.application.board.port.in.query.SearchBoardUseCase;
import com.ryuqq.setof.domain.board.vo.BoardId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Board V2 Controller
 *
 * <p>게시판 정보 조회 API 엔드포인트 (Client용)
 *
 * <p>PUBLISHED 상태이고 표시 기간 내의 게시글만 조회합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Board", description = "게시판 정보 조회 API")
@RestController
@RequestMapping(ApiV2Paths.Boards.BASE)
public class BoardV2Controller {

    private final GetBoardUseCase getBoardUseCase;
    private final SearchBoardUseCase searchBoardUseCase;
    private final BoardV2ApiMapper mapper;

    public BoardV2Controller(
            GetBoardUseCase getBoardUseCase,
            SearchBoardUseCase searchBoardUseCase,
            BoardV2ApiMapper mapper) {
        this.getBoardUseCase = getBoardUseCase;
        this.searchBoardUseCase = searchBoardUseCase;
        this.mapper = mapper;
    }

    /**
     * 게시글 목록 조회
     *
     * <p>PUBLISHED 상태이고 표시 기간 내의 게시글 목록을 조회합니다.
     *
     * @param request 검색 조건
     * @return 게시글 목록
     */
    @Operation(summary = "게시글 목록 조회", description = "PUBLISHED 상태이고 표시 기간 내의 게시글 목록을 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "500",
                        description = "서버 오류",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<BoardV2ApiResponse>>> search(
            @ParameterObject SearchBoardV2ApiRequest request) {
        SearchBoardQuery query = mapper.toSearchQuery(request);
        List<BoardResponse> responses = searchBoardUseCase.execute(query);
        List<BoardV2ApiResponse> apiResponses =
                responses.stream().map(BoardV2ApiResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }

    /**
     * 게시글 단건 조회
     *
     * @param boardId 게시글 ID
     * @return 게시글 정보
     */
    @Operation(summary = "게시글 단건 조회", description = "게시글 ID로 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "게시글을 찾을 수 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "500",
                        description = "서버 오류",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Boards.ID_PATH)
    public ResponseEntity<ApiResponse<BoardV2ApiResponse>> getById(
            @Parameter(description = "게시글 ID", example = "1") @PathVariable Long boardId) {
        BoardResponse response = getBoardUseCase.execute(BoardId.of(boardId));
        return ResponseEntity.ok(ApiResponse.ofSuccess(BoardV2ApiResponse.from(response)));
    }

    /**
     * 상단 고정 게시글 목록 조회
     *
     * <p>상단 고정된 게시글 목록을 조회합니다.
     *
     * @param boardType 게시판 타입 (optional)
     * @param limit 조회 개수 (기본값: 5)
     * @return 상단 고정 게시글 목록
     */
    @Operation(summary = "상단 고정 게시글 조회", description = "상단 고정된 게시글 목록을 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "500",
                        description = "서버 오류",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Boards.PINNED_PATH)
    public ResponseEntity<ApiResponse<List<BoardV2ApiResponse>>> getPinned(
            @Parameter(description = "게시판 타입") @RequestParam(required = false) String boardType,
            @Parameter(description = "조회 개수", example = "5") @RequestParam(defaultValue = "5")
                    int limit) {
        SearchBoardQuery query = mapper.toPinnedQuery(boardType, limit);
        List<BoardResponse> responses = searchBoardUseCase.execute(query);
        List<BoardV2ApiResponse> apiResponses =
                responses.stream().map(BoardV2ApiResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }
}
