package com.ryuqq.setof.adapter.in.rest.admin.v2.noticetemplate.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.noticetemplate.dto.command.RegisterNoticeTemplateV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.noticetemplate.dto.command.UpdateNoticeTemplateV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.noticetemplate.mapper.NoticeTemplateAdminV2ApiMapper;
import com.ryuqq.setof.application.noticetemplate.dto.command.CreateNoticeTemplateCommand;
import com.ryuqq.setof.application.noticetemplate.dto.command.UpdateNoticeTemplateCommand;
import com.ryuqq.setof.application.noticetemplate.port.in.command.CreateNoticeTemplateUseCase;
import com.ryuqq.setof.application.noticetemplate.port.in.command.DeleteNoticeTemplateUseCase;
import com.ryuqq.setof.application.noticetemplate.port.in.command.UpdateNoticeTemplateUseCase;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
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
 * 상품고시 템플릿 관리 Command Controller
 *
 * <p>CQRS Command 담당: 생성, 수정, 삭제 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "NoticeTemplate Admin", description = "상품고시 템플릿 관리 API")
@RestController
@RequestMapping(ApiV2Paths.NoticeTemplates.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class NoticeTemplateAdminCommandController {

    private final CreateNoticeTemplateUseCase createNoticeTemplateUseCase;
    private final UpdateNoticeTemplateUseCase updateNoticeTemplateUseCase;
    private final DeleteNoticeTemplateUseCase deleteNoticeTemplateUseCase;
    private final NoticeTemplateAdminV2ApiMapper mapper;

    public NoticeTemplateAdminCommandController(
            CreateNoticeTemplateUseCase createNoticeTemplateUseCase,
            UpdateNoticeTemplateUseCase updateNoticeTemplateUseCase,
            DeleteNoticeTemplateUseCase deleteNoticeTemplateUseCase,
            NoticeTemplateAdminV2ApiMapper mapper) {
        this.createNoticeTemplateUseCase = createNoticeTemplateUseCase;
        this.updateNoticeTemplateUseCase = updateNoticeTemplateUseCase;
        this.deleteNoticeTemplateUseCase = deleteNoticeTemplateUseCase;
        this.mapper = mapper;
    }

    /**
     * 상품고시 템플릿 등록
     *
     * @param request 등록 요청
     * @return 생성된 템플릿 ID
     */
    @Operation(summary = "상품고시 템플릿 등록")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "등록 성공")
            })
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> register(
            @Valid @RequestBody RegisterNoticeTemplateV2ApiRequest request) {
        CreateNoticeTemplateCommand command = mapper.toCreateCommand(request);
        Long templateId = createNoticeTemplateUseCase.create(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess(templateId));
    }

    /**
     * 상품고시 템플릿 수정
     *
     * @param templateId 템플릿 ID
     * @param request 수정 요청
     * @return 성공 응답
     */
    @Operation(summary = "상품고시 템플릿 수정")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "수정 성공")
            })
    @PatchMapping(ApiV2Paths.NoticeTemplates.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> update(
            @Parameter(description = "템플릿 ID", required = true) @PathVariable Long templateId,
            @Valid @RequestBody UpdateNoticeTemplateV2ApiRequest request) {
        UpdateNoticeTemplateCommand command = mapper.toUpdateCommand(templateId, request);
        updateNoticeTemplateUseCase.update(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * 상품고시 템플릿 삭제
     *
     * @param templateId 템플릿 ID
     * @return 성공 응답
     */
    @Operation(summary = "상품고시 템플릿 삭제")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공")
            })
    @PostMapping(ApiV2Paths.NoticeTemplates.DELETE_PATH)
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "템플릿 ID", required = true) @PathVariable Long templateId) {
        deleteNoticeTemplateUseCase.delete(NoticeTemplateId.of(templateId));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
