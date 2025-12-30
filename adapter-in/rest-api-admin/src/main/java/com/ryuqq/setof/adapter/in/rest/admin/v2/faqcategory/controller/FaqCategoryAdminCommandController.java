package com.ryuqq.setof.adapter.in.rest.admin.v2.faqcategory.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faqcategory.dto.command.CreateFaqCategoryV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faqcategory.dto.command.UpdateFaqCategoryV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faqcategory.mapper.FaqCategoryAdminV2ApiMapper;
import com.ryuqq.setof.application.faqcategory.dto.command.ActivateFaqCategoryCommand;
import com.ryuqq.setof.application.faqcategory.dto.command.CreateFaqCategoryCommand;
import com.ryuqq.setof.application.faqcategory.dto.command.DeactivateFaqCategoryCommand;
import com.ryuqq.setof.application.faqcategory.dto.command.DeleteFaqCategoryCommand;
import com.ryuqq.setof.application.faqcategory.dto.command.UpdateFaqCategoryCommand;
import com.ryuqq.setof.application.faqcategory.port.in.command.ActivateFaqCategoryUseCase;
import com.ryuqq.setof.application.faqcategory.port.in.command.CreateFaqCategoryUseCase;
import com.ryuqq.setof.application.faqcategory.port.in.command.DeactivateFaqCategoryUseCase;
import com.ryuqq.setof.application.faqcategory.port.in.command.DeleteFaqCategoryUseCase;
import com.ryuqq.setof.application.faqcategory.port.in.command.UpdateFaqCategoryUseCase;
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
 * FAQ Category Admin Command Controller
 *
 * <p>CQRS Command 담당: 생성, 수정, 삭제, 상태 변경 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "FAQ Category Admin", description = "FAQ 카테고리 관리 API")
@RestController
@RequestMapping(ApiV2Paths.FaqCategories.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class FaqCategoryAdminCommandController {

    private final CreateFaqCategoryUseCase createFaqCategoryUseCase;
    private final UpdateFaqCategoryUseCase updateFaqCategoryUseCase;
    private final ActivateFaqCategoryUseCase activateFaqCategoryUseCase;
    private final DeactivateFaqCategoryUseCase deactivateFaqCategoryUseCase;
    private final DeleteFaqCategoryUseCase deleteFaqCategoryUseCase;
    private final FaqCategoryAdminV2ApiMapper mapper;

    public FaqCategoryAdminCommandController(
            CreateFaqCategoryUseCase createFaqCategoryUseCase,
            UpdateFaqCategoryUseCase updateFaqCategoryUseCase,
            ActivateFaqCategoryUseCase activateFaqCategoryUseCase,
            DeactivateFaqCategoryUseCase deactivateFaqCategoryUseCase,
            DeleteFaqCategoryUseCase deleteFaqCategoryUseCase,
            FaqCategoryAdminV2ApiMapper mapper) {
        this.createFaqCategoryUseCase = createFaqCategoryUseCase;
        this.updateFaqCategoryUseCase = updateFaqCategoryUseCase;
        this.activateFaqCategoryUseCase = activateFaqCategoryUseCase;
        this.deactivateFaqCategoryUseCase = deactivateFaqCategoryUseCase;
        this.deleteFaqCategoryUseCase = deleteFaqCategoryUseCase;
        this.mapper = mapper;
    }

    /**
     * FAQ 카테고리 생성
     *
     * @param request 생성 요청
     * @return 생성된 카테고리 ID
     */
    @Operation(summary = "FAQ 카테고리 생성")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "생성 성공")
            })
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Valid @RequestBody CreateFaqCategoryV2ApiRequest request) {
        // TODO: 인증 정보에서 작성자 ID 추출
        Long createdBy = 1L;
        CreateFaqCategoryCommand command = mapper.toCreateCommand(request, createdBy);
        Long categoryId = createFaqCategoryUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess(categoryId));
    }

    /**
     * FAQ 카테고리 수정
     *
     * @param categoryId 카테고리 ID
     * @param request 수정 요청
     * @return 성공 응답
     */
    @Operation(summary = "FAQ 카테고리 수정")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "수정 성공")
            })
    @PatchMapping(ApiV2Paths.FaqCategories.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> update(
            @Parameter(description = "카테고리 ID", required = true) @PathVariable Long categoryId,
            @Valid @RequestBody UpdateFaqCategoryV2ApiRequest request) {
        // TODO: 인증 정보에서 수정자 ID 추출
        Long updatedBy = 1L;
        UpdateFaqCategoryCommand command = mapper.toUpdateCommand(categoryId, request, updatedBy);
        updateFaqCategoryUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * FAQ 카테고리 활성화
     *
     * @param categoryId 카테고리 ID
     * @return 성공 응답
     */
    @Operation(summary = "FAQ 카테고리 활성화")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "활성화 성공")
            })
    @PostMapping(ApiV2Paths.FaqCategories.ACTIVATE_PATH)
    public ResponseEntity<ApiResponse<Void>> activate(
            @Parameter(description = "카테고리 ID", required = true) @PathVariable Long categoryId) {
        // TODO: 인증 정보에서 수정자 ID 추출
        Long updatedBy = 1L;
        activateFaqCategoryUseCase.execute(new ActivateFaqCategoryCommand(categoryId, updatedBy));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * FAQ 카테고리 비활성화
     *
     * @param categoryId 카테고리 ID
     * @return 성공 응답
     */
    @Operation(summary = "FAQ 카테고리 비활성화")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "비활성화 성공")
            })
    @PostMapping(ApiV2Paths.FaqCategories.DEACTIVATE_PATH)
    public ResponseEntity<ApiResponse<Void>> deactivate(
            @Parameter(description = "카테고리 ID", required = true) @PathVariable Long categoryId) {
        // TODO: 인증 정보에서 수정자 ID 추출
        Long updatedBy = 1L;
        deactivateFaqCategoryUseCase.execute(
                new DeactivateFaqCategoryCommand(categoryId, updatedBy));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * FAQ 카테고리 삭제
     *
     * @param categoryId 카테고리 ID
     * @return 성공 응답
     */
    @Operation(summary = "FAQ 카테고리 삭제")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공")
            })
    @PostMapping(ApiV2Paths.FaqCategories.DELETE_PATH)
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "카테고리 ID", required = true) @PathVariable Long categoryId) {
        // TODO: 인증 정보에서 삭제자 ID 추출
        Long deletedBy = 1L;
        deleteFaqCategoryUseCase.execute(new DeleteFaqCategoryCommand(categoryId, deletedBy));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
