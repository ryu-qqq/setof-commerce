package com.ryuqq.setof.adapter.in.rest.admin.v2.faq.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faq.dto.query.SearchFaqV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faq.dto.response.FaqV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faq.mapper.FaqAdminV2ApiMapper;
import com.ryuqq.setof.application.faq.dto.query.SearchFaqQuery;
import com.ryuqq.setof.application.faq.dto.response.FaqResponse;
import com.ryuqq.setof.application.faq.port.in.query.GetFaqUseCase;
import com.ryuqq.setof.application.faq.port.in.query.SearchFaqUseCase;
import com.ryuqq.setof.domain.faq.vo.FaqId;
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
 * FAQ Admin 조회 Controller
 *
 * <p>CQRS Query 담당: 조회 전용 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "FAQ Admin", description = "FAQ 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Faqs.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class FaqAdminQueryController {

    private final GetFaqUseCase getFaqUseCase;
    private final SearchFaqUseCase searchFaqUseCase;
    private final FaqAdminV2ApiMapper mapper;

    public FaqAdminQueryController(
            GetFaqUseCase getFaqUseCase,
            SearchFaqUseCase searchFaqUseCase,
            FaqAdminV2ApiMapper mapper) {
        this.getFaqUseCase = getFaqUseCase;
        this.searchFaqUseCase = searchFaqUseCase;
        this.mapper = mapper;
    }

    /**
     * FAQ 목록 조회
     *
     * @param request 검색 조건
     * @return FAQ 목록
     */
    @Operation(summary = "FAQ 목록 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
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
    @Operation(summary = "FAQ 단건 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping(ApiV2Paths.Faqs.ID_PATH)
    public ResponseEntity<ApiResponse<FaqV2ApiResponse>> getById(
            @Parameter(description = "FAQ ID", required = true) @PathVariable Long faqId) {
        FaqResponse response = getFaqUseCase.execute(FaqId.of(faqId));
        return ResponseEntity.ok(ApiResponse.ofSuccess(FaqV2ApiResponse.from(response)));
    }
}
