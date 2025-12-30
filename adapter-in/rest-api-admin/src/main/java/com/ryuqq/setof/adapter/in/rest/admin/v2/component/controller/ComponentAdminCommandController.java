package com.ryuqq.setof.adapter.in.rest.admin.v2.component.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.command.CreateComponentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.command.UpdateComponentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.component.mapper.ComponentAdminV2ApiMapper;
import com.ryuqq.setof.application.component.dto.command.CreateComponentCommand;
import com.ryuqq.setof.application.component.dto.command.DeleteComponentCommand;
import com.ryuqq.setof.application.component.dto.command.UpdateComponentCommand;
import com.ryuqq.setof.application.component.port.in.command.CreateComponentUseCase;
import com.ryuqq.setof.application.component.port.in.command.DeleteComponentUseCase;
import com.ryuqq.setof.application.component.port.in.command.UpdateComponentUseCase;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
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
 * Component Admin Command Controller
 *
 * <p>CQRS Command 담당: 생성, 수정, 삭제 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Component Admin", description = "컴포넌트 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Components.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class ComponentAdminCommandController {

    private final CreateComponentUseCase createComponentUseCase;
    private final UpdateComponentUseCase updateComponentUseCase;
    private final DeleteComponentUseCase deleteComponentUseCase;
    private final ComponentAdminV2ApiMapper mapper;

    public ComponentAdminCommandController(
            CreateComponentUseCase createComponentUseCase,
            UpdateComponentUseCase updateComponentUseCase,
            DeleteComponentUseCase deleteComponentUseCase,
            ComponentAdminV2ApiMapper mapper) {
        this.createComponentUseCase = createComponentUseCase;
        this.updateComponentUseCase = updateComponentUseCase;
        this.deleteComponentUseCase = deleteComponentUseCase;
        this.mapper = mapper;
    }

    /**
     * 컴포넌트 생성
     *
     * @param contentId 컨텐츠 ID
     * @param request 생성 요청
     * @return 생성된 컴포넌트 ID
     */
    @Operation(summary = "컴포넌트 생성")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "생성 성공")
            })
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Parameter(description = "컨텐츠 ID", required = true) @PathVariable Long contentId,
            @Valid @RequestBody CreateComponentV2ApiRequest request) {
        CreateComponentCommand command = mapper.toCreateCommand(contentId, request);
        Long componentId = createComponentUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess(componentId));
    }

    /**
     * 컴포넌트 수정
     *
     * @param contentId 컨텐츠 ID
     * @param componentId 컴포넌트 ID
     * @param request 수정 요청
     * @return 성공 응답
     */
    @Operation(summary = "컴포넌트 수정")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "수정 성공")
            })
    @PatchMapping(ApiV2Paths.Components.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> update(
            @Parameter(description = "컨텐츠 ID", required = true) @PathVariable Long contentId,
            @Parameter(description = "컴포넌트 ID", required = true) @PathVariable Long componentId,
            @Valid @RequestBody UpdateComponentV2ApiRequest request) {
        UpdateComponentCommand command = mapper.toUpdateCommand(componentId, request);
        updateComponentUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 컴포넌트 삭제
     *
     * @param contentId 컨텐츠 ID
     * @param componentId 컴포넌트 ID
     * @return 성공 응답
     */
    @Operation(summary = "컴포넌트 삭제")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공")
            })
    @PostMapping(ApiV2Paths.Components.DELETE_PATH)
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "컨텐츠 ID", required = true) @PathVariable Long contentId,
            @Parameter(description = "컴포넌트 ID", required = true) @PathVariable Long componentId) {
        deleteComponentUseCase.execute(new DeleteComponentCommand(ComponentId.of(componentId)));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
