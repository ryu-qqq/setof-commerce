package com.ryuqq.setof.adapter.in.rest.admin.v2.board.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.board.dto.command.CreateBoardV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.board.dto.command.UpdateBoardV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.board.mapper.BoardAdminV2ApiMapper;
import com.ryuqq.setof.application.board.dto.command.CreateBoardCommand;
import com.ryuqq.setof.application.board.dto.command.DeleteBoardCommand;
import com.ryuqq.setof.application.board.dto.command.HideBoardCommand;
import com.ryuqq.setof.application.board.dto.command.PinBoardCommand;
import com.ryuqq.setof.application.board.dto.command.PublishBoardCommand;
import com.ryuqq.setof.application.board.dto.command.UnpinBoardCommand;
import com.ryuqq.setof.application.board.dto.command.UpdateBoardCommand;
import com.ryuqq.setof.application.board.port.in.command.CreateBoardUseCase;
import com.ryuqq.setof.application.board.port.in.command.DeleteBoardUseCase;
import com.ryuqq.setof.application.board.port.in.command.HideBoardUseCase;
import com.ryuqq.setof.application.board.port.in.command.PinBoardUseCase;
import com.ryuqq.setof.application.board.port.in.command.PublishBoardUseCase;
import com.ryuqq.setof.application.board.port.in.command.UnpinBoardUseCase;
import com.ryuqq.setof.application.board.port.in.command.UpdateBoardUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Board Admin Command Controller
 *
 * <p>CQRS Command 담당: 생성, 수정, 삭제, 상태 변경 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Board Admin", description = "게시판 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Boards.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class BoardAdminCommandController {

    private final CreateBoardUseCase createBoardUseCase;
    private final UpdateBoardUseCase updateBoardUseCase;
    private final DeleteBoardUseCase deleteBoardUseCase;
    private final PublishBoardUseCase publishBoardUseCase;
    private final HideBoardUseCase hideBoardUseCase;
    private final PinBoardUseCase pinBoardUseCase;
    private final UnpinBoardUseCase unpinBoardUseCase;
    private final BoardAdminV2ApiMapper mapper;

    public BoardAdminCommandController(
            CreateBoardUseCase createBoardUseCase,
            UpdateBoardUseCase updateBoardUseCase,
            DeleteBoardUseCase deleteBoardUseCase,
            PublishBoardUseCase publishBoardUseCase,
            HideBoardUseCase hideBoardUseCase,
            PinBoardUseCase pinBoardUseCase,
            UnpinBoardUseCase unpinBoardUseCase,
            BoardAdminV2ApiMapper mapper) {
        this.createBoardUseCase = createBoardUseCase;
        this.updateBoardUseCase = updateBoardUseCase;
        this.deleteBoardUseCase = deleteBoardUseCase;
        this.publishBoardUseCase = publishBoardUseCase;
        this.hideBoardUseCase = hideBoardUseCase;
        this.pinBoardUseCase = pinBoardUseCase;
        this.unpinBoardUseCase = unpinBoardUseCase;
        this.mapper = mapper;
    }

    /**
     * 게시글 생성
     *
     * @param request 생성 요청
     * @return 생성된 게시글 ID
     */
    @Operation(summary = "게시글 생성")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "생성 성공")
            })
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Valid @RequestBody CreateBoardV2ApiRequest request) {
        // TODO: 인증 정보에서 작성자 ID 추출
        Long createdBy = 1L;
        CreateBoardCommand command = mapper.toCreateCommand(request, createdBy);
        Long boardId = createBoardUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess(boardId));
    }

    /**
     * 게시글 수정
     *
     * @param boardId 게시글 ID
     * @param request 수정 요청
     * @return 성공 응답
     */
    @Operation(summary = "게시글 수정")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "수정 성공")
            })
    @PatchMapping(ApiV2Paths.Boards.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> update(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId,
            @Valid @RequestBody UpdateBoardV2ApiRequest request) {
        // TODO: 인증 정보에서 수정자 ID 추출
        Long updatedBy = 1L;
        UpdateBoardCommand command = mapper.toUpdateCommand(boardId, request, updatedBy);
        updateBoardUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 게시글 발행
     *
     * @param boardId 게시글 ID
     * @return 성공 응답
     */
    @Operation(summary = "게시글 발행")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "발행 성공")
            })
    @PostMapping(ApiV2Paths.Boards.PUBLISH_PATH)
    public ResponseEntity<ApiResponse<Void>> publish(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId) {
        // TODO: 인증 정보에서 수정자 ID 추출
        Long updatedBy = 1L;
        publishBoardUseCase.execute(new PublishBoardCommand(boardId, updatedBy));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 게시글 숨김
     *
     * @param boardId 게시글 ID
     * @return 성공 응답
     */
    @Operation(summary = "게시글 숨김")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "숨김 성공")
            })
    @PostMapping(ApiV2Paths.Boards.HIDE_PATH)
    public ResponseEntity<ApiResponse<Void>> hide(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId) {
        // TODO: 인증 정보에서 수정자 ID 추출
        Long updatedBy = 1L;
        hideBoardUseCase.execute(new HideBoardCommand(boardId, updatedBy));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 게시글 상단 고정
     *
     * @param boardId 게시글 ID
     * @param pinOrder 상단 고정 순서 (기본값: 0)
     * @return 성공 응답
     */
    @Operation(summary = "게시글 상단 고정")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "상단 고정 성공")
            })
    @PostMapping(ApiV2Paths.Boards.PIN_PATH)
    public ResponseEntity<ApiResponse<Void>> pin(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId,
            @Parameter(description = "상단 고정 순서", example = "0") @RequestParam(defaultValue = "0")
                    int pinOrder) {
        // TODO: 인증 정보에서 수정자 ID 추출
        Long updatedBy = 1L;
        pinBoardUseCase.execute(new PinBoardCommand(boardId, pinOrder, updatedBy));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 게시글 상단 고정 해제
     *
     * @param boardId 게시글 ID
     * @return 성공 응답
     */
    @Operation(summary = "게시글 상단 고정 해제")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "상단 고정 해제 성공")
            })
    @PostMapping(ApiV2Paths.Boards.UNPIN_PATH)
    public ResponseEntity<ApiResponse<Void>> unpin(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId) {
        // TODO: 인증 정보에서 수정자 ID 추출
        Long updatedBy = 1L;
        unpinBoardUseCase.execute(new UnpinBoardCommand(boardId, updatedBy));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 게시글 삭제
     *
     * @param boardId 게시글 ID
     * @return 성공 응답
     */
    @Operation(summary = "게시글 삭제")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공")
            })
    @PostMapping(ApiV2Paths.Boards.DELETE_PATH)
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId) {
        // TODO: 인증 정보에서 삭제자 ID 추출
        Long deletedBy = 1L;
        deleteBoardUseCase.execute(new DeleteBoardCommand(boardId, deletedBy));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
