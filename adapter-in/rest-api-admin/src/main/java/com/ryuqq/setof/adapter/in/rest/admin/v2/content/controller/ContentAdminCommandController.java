package com.ryuqq.setof.adapter.in.rest.admin.v2.content.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.command.CreateContentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.command.UpdateContentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.content.mapper.ContentAdminV2ApiMapper;
import com.ryuqq.setof.application.content.dto.command.ActivateContentCommand;
import com.ryuqq.setof.application.content.dto.command.CreateContentCommand;
import com.ryuqq.setof.application.content.dto.command.DeactivateContentCommand;
import com.ryuqq.setof.application.content.dto.command.DeleteContentCommand;
import com.ryuqq.setof.application.content.dto.command.UpdateContentCommand;
import com.ryuqq.setof.application.content.port.in.command.ActivateContentUseCase;
import com.ryuqq.setof.application.content.port.in.command.CreateContentUseCase;
import com.ryuqq.setof.application.content.port.in.command.DeactivateContentUseCase;
import com.ryuqq.setof.application.content.port.in.command.DeleteContentUseCase;
import com.ryuqq.setof.application.content.port.in.command.UpdateContentUseCase;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * Content Admin Command Controller
 *
 * <p>CQRS Command 담당: 생성, 수정, 삭제 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Content Admin", description = "콘텐츠 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Contents.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class ContentAdminCommandController {

    private final CreateContentUseCase createContentUseCase;
    private final UpdateContentUseCase updateContentUseCase;
    private final ActivateContentUseCase activateContentUseCase;
    private final DeactivateContentUseCase deactivateContentUseCase;
    private final DeleteContentUseCase deleteContentUseCase;
    private final ContentAdminV2ApiMapper mapper;

    public ContentAdminCommandController(
            CreateContentUseCase createContentUseCase,
            UpdateContentUseCase updateContentUseCase,
            ActivateContentUseCase activateContentUseCase,
            DeactivateContentUseCase deactivateContentUseCase,
            DeleteContentUseCase deleteContentUseCase,
            ContentAdminV2ApiMapper mapper) {
        this.createContentUseCase = createContentUseCase;
        this.updateContentUseCase = updateContentUseCase;
        this.activateContentUseCase = activateContentUseCase;
        this.deactivateContentUseCase = deactivateContentUseCase;
        this.deleteContentUseCase = deleteContentUseCase;
        this.mapper = mapper;
    }

    /**
     * 콘텐츠 생성
     *
     * @param request 생성 요청
     * @return 생성된 콘텐츠 ID
     */
    @Operation(summary = "콘텐츠 생성")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "생성 성공")
            })
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Valid @RequestBody CreateContentV2ApiRequest request) {
        CreateContentCommand command = mapper.toCreateCommand(request);
        Long contentId = createContentUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess(contentId));
    }

    /**
     * 콘텐츠 수정
     *
     * @param contentId 콘텐츠 ID
     * @param request 수정 요청
     * @return 성공 응답
     */
    @Operation(summary = "콘텐츠 수정")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "수정 성공")
            })
    @PatchMapping(ApiV2Paths.Contents.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> update(
            @Parameter(description = "콘텐츠 ID", required = true) @PathVariable Long contentId,
            @Valid @RequestBody UpdateContentV2ApiRequest request) {
        UpdateContentCommand command = mapper.toUpdateCommand(contentId, request);
        updateContentUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 콘텐츠 활성화
     *
     * @param contentId 콘텐츠 ID
     * @return 성공 응답
     */
    @Operation(summary = "콘텐츠 활성화")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "활성화 성공")
            })
    @PostMapping(ApiV2Paths.Contents.ACTIVATE_PATH)
    public ResponseEntity<ApiResponse<Void>> activate(
            @Parameter(description = "콘텐츠 ID", required = true) @PathVariable Long contentId) {
        activateContentUseCase.execute(new ActivateContentCommand(contentId));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 콘텐츠 비활성화
     *
     * @param contentId 콘텐츠 ID
     * @return 성공 응답
     */
    @Operation(summary = "콘텐츠 비활성화")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "비활성화 성공")
            })
    @PostMapping(ApiV2Paths.Contents.DEACTIVATE_PATH)
    public ResponseEntity<ApiResponse<Void>> deactivate(
            @Parameter(description = "콘텐츠 ID", required = true) @PathVariable Long contentId) {
        deactivateContentUseCase.execute(new DeactivateContentCommand(contentId));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 콘텐츠 삭제
     *
     * @param contentId 콘텐츠 ID
     * @return 성공 응답
     */
    @Operation(summary = "콘텐츠 삭제")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공")
            })
    @PostMapping(ApiV2Paths.Contents.DELETE_PATH)
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "콘텐츠 ID", required = true) @PathVariable Long contentId) {
        deleteContentUseCase.execute(new DeleteContentCommand(contentId));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
