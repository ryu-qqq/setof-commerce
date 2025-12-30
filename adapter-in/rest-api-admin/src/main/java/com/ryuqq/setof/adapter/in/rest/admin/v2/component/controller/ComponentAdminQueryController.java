package com.ryuqq.setof.adapter.in.rest.admin.v2.component.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.response.ComponentV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.component.mapper.ComponentAdminV2ApiMapper;
import com.ryuqq.setof.application.component.dto.response.ComponentResponse;
import com.ryuqq.setof.application.component.port.in.query.GetComponentUseCase;
import com.ryuqq.setof.application.component.port.in.query.GetComponentsByContentUseCase;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Component Admin Query Controller
 *
 * <p>CQRS Query 담당: 조회 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Component Admin", description = "컴포넌트 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Components.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class ComponentAdminQueryController {

    private final GetComponentUseCase getComponentUseCase;
    private final GetComponentsByContentUseCase getComponentsByContentUseCase;
    private final ComponentAdminV2ApiMapper mapper;

    public ComponentAdminQueryController(
            GetComponentUseCase getComponentUseCase,
            GetComponentsByContentUseCase getComponentsByContentUseCase,
            ComponentAdminV2ApiMapper mapper) {
        this.getComponentUseCase = getComponentUseCase;
        this.getComponentsByContentUseCase = getComponentsByContentUseCase;
        this.mapper = mapper;
    }

    /**
     * 컨텐츠별 컴포넌트 목록 조회
     *
     * @param contentId 컨텐츠 ID
     * @return 컴포넌트 목록
     */
    @Operation(summary = "컨텐츠별 컴포넌트 목록 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ComponentV2ApiResponse>>> getByContent(
            @Parameter(description = "컨텐츠 ID", required = true) @PathVariable Long contentId) {
        List<ComponentResponse> responses =
                getComponentsByContentUseCase.getByContentId(ContentId.of(contentId));
        return ResponseEntity.ok(ApiResponse.ofSuccess(mapper.toApiResponses(responses)));
    }

    /**
     * 컴포넌트 상세 조회
     *
     * @param contentId 컨텐츠 ID
     * @param componentId 컴포넌트 ID
     * @return 컴포넌트 상세 정보
     */
    @Operation(summary = "컴포넌트 상세 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping(ApiV2Paths.Components.ID_PATH)
    public ResponseEntity<ApiResponse<ComponentV2ApiResponse>> getById(
            @Parameter(description = "컨텐츠 ID", required = true) @PathVariable Long contentId,
            @Parameter(description = "컴포넌트 ID", required = true) @PathVariable Long componentId) {
        ComponentResponse response = getComponentUseCase.execute(ComponentId.of(componentId));
        return ResponseEntity.ok(ApiResponse.ofSuccess(mapper.toApiResponse(response)));
    }
}
