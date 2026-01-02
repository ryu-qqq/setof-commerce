package com.ryuqq.setof.adapter.in.rest.admin.v2.noticetemplate.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.noticetemplate.dto.response.NoticeTemplateV2ApiResponse;
import com.ryuqq.setof.application.noticetemplate.dto.response.NoticeTemplateResponse;
import com.ryuqq.setof.application.noticetemplate.port.in.query.GetNoticeTemplateUseCase;
import com.ryuqq.setof.application.noticetemplate.port.in.query.GetNoticeTemplatesUseCase;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 상품고시 템플릿 관리 조회 Controller
 *
 * <p>CQRS Query 담당: 조회 전용 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "NoticeTemplate Admin", description = "상품고시 템플릿 관리 API")
@RestController
@RequestMapping(ApiV2Paths.NoticeTemplates.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class NoticeTemplateAdminQueryController {

    private final GetNoticeTemplateUseCase getNoticeTemplateUseCase;
    private final GetNoticeTemplatesUseCase getNoticeTemplatesUseCase;

    public NoticeTemplateAdminQueryController(
            GetNoticeTemplateUseCase getNoticeTemplateUseCase,
            GetNoticeTemplatesUseCase getNoticeTemplatesUseCase) {
        this.getNoticeTemplateUseCase = getNoticeTemplateUseCase;
        this.getNoticeTemplatesUseCase = getNoticeTemplatesUseCase;
    }

    /**
     * 상품고시 템플릿 전체 목록 조회
     *
     * @return 템플릿 목록
     */
    @Operation(summary = "상품고시 템플릿 전체 목록 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<NoticeTemplateV2ApiResponse>>> getAll() {
        List<NoticeTemplateResponse> responses = getNoticeTemplatesUseCase.getAll();
        List<NoticeTemplateV2ApiResponse> apiResponses =
                responses.stream().map(NoticeTemplateV2ApiResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }

    /**
     * 상품고시 템플릿 단건 조회
     *
     * @param templateId 템플릿 ID
     * @return 템플릿 정보
     */
    @Operation(summary = "상품고시 템플릿 단건 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping(ApiV2Paths.NoticeTemplates.ID_PATH)
    public ResponseEntity<ApiResponse<NoticeTemplateV2ApiResponse>> getById(
            @Parameter(description = "템플릿 ID", required = true) @PathVariable Long templateId) {
        NoticeTemplateResponse response =
                getNoticeTemplateUseCase.getById(NoticeTemplateId.of(templateId));
        return ResponseEntity.ok(ApiResponse.ofSuccess(NoticeTemplateV2ApiResponse.from(response)));
    }

    /**
     * 카테고리별 상품고시 템플릿 조회
     *
     * @param categoryId 카테고리 ID
     * @return 템플릿 정보 (없으면 404)
     */
    @Operation(summary = "카테고리별 상품고시 템플릿 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping(ApiV2Paths.NoticeTemplates.BY_CATEGORY_PATH)
    public ResponseEntity<ApiResponse<NoticeTemplateV2ApiResponse>> getByCategoryId(
            @Parameter(description = "카테고리 ID", required = true) @PathVariable Long categoryId) {
        Optional<NoticeTemplateResponse> response =
                getNoticeTemplateUseCase.getByCategoryId(CategoryId.of(categoryId));
        return response.map(
                        r ->
                                ResponseEntity.ok(
                                        ApiResponse.ofSuccess(NoticeTemplateV2ApiResponse.from(r))))
                .orElse(ResponseEntity.notFound().build());
    }
}
