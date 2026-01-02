package com.ryuqq.setof.adapter.in.rest.v2.faq.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.faq.dto.query.SearchFaqV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.faq.dto.response.FaqV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.faq.mapper.FaqV2ApiMapper;
import com.ryuqq.setof.application.faq.dto.query.SearchFaqQuery;
import com.ryuqq.setof.application.faq.dto.response.FaqResponse;
import com.ryuqq.setof.application.faq.port.in.query.GetFaqUseCase;
import com.ryuqq.setof.application.faq.port.in.query.SearchFaqUseCase;
import com.ryuqq.setof.domain.faq.vo.FaqId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * FAQ V2 Controller
 *
 * <p>FAQ 정보 조회 API 엔드포인트 (Client용)
 *
 * <p>PUBLISHED 상태의 FAQ만 조회합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "FAQ", description = "FAQ 정보 조회 API")
@RestController
@RequestMapping(ApiV2Paths.Faqs.BASE)
public class FaqV2Controller {

    private final GetFaqUseCase getFaqUseCase;
    private final SearchFaqUseCase searchFaqUseCase;
    private final FaqV2ApiMapper mapper;

    public FaqV2Controller(
            GetFaqUseCase getFaqUseCase, SearchFaqUseCase searchFaqUseCase, FaqV2ApiMapper mapper) {
        this.getFaqUseCase = getFaqUseCase;
        this.searchFaqUseCase = searchFaqUseCase;
        this.mapper = mapper;
    }

    /**
     * FAQ 목록 조회
     *
     * <p>PUBLISHED 상태의 FAQ 목록을 조회합니다.
     *
     * @param request 검색 조건
     * @return FAQ 목록
     */
    @Operation(summary = "FAQ 목록 조회", description = "PUBLISHED 상태의 FAQ 목록을 조회합니다.")
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
    public ResponseEntity<ApiResponse<List<FaqV2ApiResponse>>> search(
            @ParameterObject SearchFaqV2ApiRequest request) {
        SearchFaqQuery query = mapper.toSearchQuery(request);
        List<FaqResponse> responses = searchFaqUseCase.execute(query);
        List<FaqV2ApiResponse> apiResponses =
                responses.stream().map(FaqV2ApiResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }

    /**
     * FAQ 단건 조회
     *
     * @param faqId FAQ ID
     * @return FAQ 정보
     */
    @Operation(summary = "FAQ 단건 조회", description = "FAQ ID로 상세 정보를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "FAQ를 찾을 수 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "500",
                        description = "서버 오류",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Faqs.ID_PATH)
    public ResponseEntity<ApiResponse<FaqV2ApiResponse>> getById(
            @Parameter(description = "FAQ ID", example = "1") @PathVariable Long faqId) {
        FaqResponse response = getFaqUseCase.execute(FaqId.of(faqId));
        return ResponseEntity.ok(ApiResponse.ofSuccess(FaqV2ApiResponse.from(response)));
    }

    /**
     * 상단 FAQ 목록 조회
     *
     * <p>상단에 노출되는 FAQ 목록을 조회합니다.
     *
     * @param categoryCode 카테고리 코드 (optional)
     * @param limit 조회 개수 (기본값: 5)
     * @return 상단 FAQ 목록
     */
    @Operation(summary = "상단 FAQ 조회", description = "상단에 노출되는 FAQ 목록을 조회합니다.")
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
    @GetMapping(ApiV2Paths.Faqs.TOP_PATH)
    public ResponseEntity<ApiResponse<List<FaqV2ApiResponse>>> getTop(
            @Parameter(description = "카테고리 코드") @RequestParam(required = false) String categoryCode,
            @Parameter(description = "조회 개수", example = "5") @RequestParam(defaultValue = "5")
                    int limit) {
        SearchFaqQuery query = mapper.toTopQuery(categoryCode, limit);
        List<FaqResponse> responses = searchFaqUseCase.execute(query);
        List<FaqV2ApiResponse> apiResponses =
                responses.stream().map(FaqV2ApiResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }
}
