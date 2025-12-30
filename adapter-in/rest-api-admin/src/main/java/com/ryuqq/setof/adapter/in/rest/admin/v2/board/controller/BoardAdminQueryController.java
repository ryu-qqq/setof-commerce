package com.ryuqq.setof.adapter.in.rest.admin.v2.board.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.board.dto.query.SearchBoardV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.board.dto.response.BoardV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.board.mapper.BoardAdminV2ApiMapper;
import com.ryuqq.setof.application.board.dto.query.SearchBoardQuery;
import com.ryuqq.setof.application.board.dto.response.BoardResponse;
import com.ryuqq.setof.application.board.port.in.query.GetBoardUseCase;
import com.ryuqq.setof.application.board.port.in.query.SearchBoardUseCase;
import com.ryuqq.setof.domain.board.vo.BoardId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Board Admin 조회 Controller
 *
 * <p>CQRS Query 담당: 조회 전용 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Board Admin", description = "게시판 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Boards.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class BoardAdminQueryController {

    private final GetBoardUseCase getBoardUseCase;
    private final SearchBoardUseCase searchBoardUseCase;
    private final BoardAdminV2ApiMapper mapper;

    public BoardAdminQueryController(
            GetBoardUseCase getBoardUseCase,
            SearchBoardUseCase searchBoardUseCase,
            BoardAdminV2ApiMapper mapper) {
        this.getBoardUseCase = getBoardUseCase;
        this.searchBoardUseCase = searchBoardUseCase;
        this.mapper = mapper;
    }

    /**
     * 게시글 목록 조회
     *
     * @param request 검색 조건
     * @return 게시글 목록
     */
    @Operation(summary = "게시글 목록 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
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
    @Operation(summary = "게시글 단건 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping(ApiV2Paths.Boards.ID_PATH)
    public ResponseEntity<ApiResponse<BoardV2ApiResponse>> getById(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId) {
        BoardResponse response = getBoardUseCase.execute(BoardId.of(boardId));
        return ResponseEntity.ok(ApiResponse.ofSuccess(BoardV2ApiResponse.from(response)));
    }
}
