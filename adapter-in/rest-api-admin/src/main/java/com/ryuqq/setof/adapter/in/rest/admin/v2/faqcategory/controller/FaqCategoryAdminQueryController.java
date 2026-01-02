package com.ryuqq.setof.adapter.in.rest.admin.v2.faqcategory.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faqcategory.dto.query.SearchFaqCategoryV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faqcategory.dto.response.FaqCategoryV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faqcategory.mapper.FaqCategoryAdminV2ApiMapper;
import com.ryuqq.setof.application.faqcategory.dto.query.SearchFaqCategoryQuery;
import com.ryuqq.setof.application.faqcategory.dto.response.FaqCategoryResponse;
import com.ryuqq.setof.application.faqcategory.port.in.query.GetFaqCategoryUseCase;
import com.ryuqq.setof.application.faqcategory.port.in.query.SearchFaqCategoryUseCase;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryId;
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
 * FAQ Category Admin 조회 Controller
 *
 * <p>CQRS Query 담당: 조회 전용 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "FAQ Category Admin", description = "FAQ 카테고리 관리 API")
@RestController
@RequestMapping(ApiV2Paths.FaqCategories.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class FaqCategoryAdminQueryController {

    private final GetFaqCategoryUseCase getFaqCategoryUseCase;
    private final SearchFaqCategoryUseCase searchFaqCategoryUseCase;
    private final FaqCategoryAdminV2ApiMapper mapper;

    public FaqCategoryAdminQueryController(
            GetFaqCategoryUseCase getFaqCategoryUseCase,
            SearchFaqCategoryUseCase searchFaqCategoryUseCase,
            FaqCategoryAdminV2ApiMapper mapper) {
        this.getFaqCategoryUseCase = getFaqCategoryUseCase;
        this.searchFaqCategoryUseCase = searchFaqCategoryUseCase;
        this.mapper = mapper;
    }

    /**
     * FAQ 카테고리 목록 조회
     *
     * @param request 검색 조건
     * @return FAQ 카테고리 목록
     */
    @Operation(summary = "FAQ 카테고리 목록 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<FaqCategoryV2ApiResponse>>> search(
            @ParameterObject SearchFaqCategoryV2ApiRequest request) {
        SearchFaqCategoryQuery query = mapper.toSearchQuery(request);
        List<FaqCategoryResponse> responses = searchFaqCategoryUseCase.execute(query);
        List<FaqCategoryV2ApiResponse> apiResponses =
                responses.stream().map(FaqCategoryV2ApiResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }

    /**
     * FAQ 카테고리 단건 조회
     *
     * @param categoryId 카테고리 ID
     * @return FAQ 카테고리 정보
     */
    @Operation(summary = "FAQ 카테고리 단건 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping(ApiV2Paths.FaqCategories.ID_PATH)
    public ResponseEntity<ApiResponse<FaqCategoryV2ApiResponse>> getById(
            @Parameter(description = "카테고리 ID", required = true) @PathVariable Long categoryId) {
        FaqCategoryResponse response = getFaqCategoryUseCase.execute(FaqCategoryId.of(categoryId));
        return ResponseEntity.ok(ApiResponse.ofSuccess(FaqCategoryV2ApiResponse.from(response)));
    }
}
