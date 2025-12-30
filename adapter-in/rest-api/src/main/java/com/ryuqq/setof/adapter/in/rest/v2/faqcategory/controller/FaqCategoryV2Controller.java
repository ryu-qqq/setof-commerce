package com.ryuqq.setof.adapter.in.rest.v2.faqcategory.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.faqcategory.dto.response.FaqCategoryV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.faqcategory.mapper.FaqCategoryV2ApiMapper;
import com.ryuqq.setof.application.faqcategory.dto.query.SearchFaqCategoryQuery;
import com.ryuqq.setof.application.faqcategory.dto.response.FaqCategoryResponse;
import com.ryuqq.setof.application.faqcategory.port.in.query.GetFaqCategoryUseCase;
import com.ryuqq.setof.application.faqcategory.port.in.query.SearchFaqCategoryUseCase;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * FAQ Category V2 Controller
 *
 * <p>FAQ 카테고리 정보 조회 API 엔드포인트 (Client용)
 *
 * <p>ACTIVE 상태의 FAQ 카테고리만 조회합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "FAQ Category", description = "FAQ 카테고리 정보 조회 API")
@RestController
@RequestMapping(ApiV2Paths.FaqCategories.BASE)
public class FaqCategoryV2Controller {

    private final GetFaqCategoryUseCase getFaqCategoryUseCase;
    private final SearchFaqCategoryUseCase searchFaqCategoryUseCase;
    private final FaqCategoryV2ApiMapper mapper;

    public FaqCategoryV2Controller(
            GetFaqCategoryUseCase getFaqCategoryUseCase,
            SearchFaqCategoryUseCase searchFaqCategoryUseCase,
            FaqCategoryV2ApiMapper mapper) {
        this.getFaqCategoryUseCase = getFaqCategoryUseCase;
        this.searchFaqCategoryUseCase = searchFaqCategoryUseCase;
        this.mapper = mapper;
    }

    /**
     * FAQ 카테고리 목록 조회
     *
     * <p>ACTIVE 상태의 FAQ 카테고리 목록을 조회합니다.
     *
     * @param page 페이지 번호 (기본값: 0)
     * @param size 페이지 크기 (기본값: 100)
     * @return FAQ 카테고리 목록
     */
    @Operation(summary = "FAQ 카테고리 목록 조회", description = "ACTIVE 상태의 FAQ 카테고리 목록을 조회합니다.")
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
    public ResponseEntity<ApiResponse<List<FaqCategoryV2ApiResponse>>> getAll(
            @Parameter(description = "페이지 번호", example = "0") @RequestParam(defaultValue = "0")
                    int page,
            @Parameter(description = "페이지 크기", example = "100") @RequestParam(defaultValue = "100")
                    int size) {
        SearchFaqCategoryQuery query = mapper.toSearchQuery(page, size);
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
    @Operation(summary = "FAQ 카테고리 단건 조회", description = "FAQ 카테고리 ID로 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "FAQ 카테고리를 찾을 수 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "500",
                        description = "서버 오류",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.FaqCategories.ID_PATH)
    public ResponseEntity<ApiResponse<FaqCategoryV2ApiResponse>> getById(
            @Parameter(description = "카테고리 ID", example = "1") @PathVariable Long categoryId) {
        FaqCategoryResponse response = getFaqCategoryUseCase.execute(FaqCategoryId.of(categoryId));
        return ResponseEntity.ok(ApiResponse.ofSuccess(FaqCategoryV2ApiResponse.from(response)));
    }
}
