package com.ryuqq.setof.adapter.in.rest.v1.search.controller;

import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.search.SearchV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.search.dto.request.SearchProductsCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.search.dto.response.SearchSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.search.mapper.SearchV1ApiMapper;
import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupSearchParams;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;
import com.ryuqq.setof.application.productgroup.port.in.query.SearchProductGroupsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SearchQueryV1Controller - 상품 검색 V1 Public API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * <p>API-CTR-005: Controller @Transactional 금지.
 *
 * <p>API-CTR-007: Controller 비즈니스 로직 금지.
 *
 * <p>레거시 SearchController.fetchSearchResults 변환. MySQL ngram FULLTEXT 기반 키워드 검색 + 커서 페이징.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "상품 검색 V1", description = "상품 키워드 검색 V1 Public API")
@RestController
@RequestMapping(SearchV1Endpoints.SEARCH)
public class SearchQueryV1Controller {

    private final SearchProductGroupsUseCase searchProductGroupsUseCase;
    private final SearchV1ApiMapper mapper;

    public SearchQueryV1Controller(
            SearchProductGroupsUseCase searchProductGroupsUseCase, SearchV1ApiMapper mapper) {
        this.searchProductGroupsUseCase = searchProductGroupsUseCase;
        this.mapper = mapper;
    }

    /**
     * 상품 키워드 검색 API (커서 페이징).
     *
     * <p>GET /api/v1/search
     *
     * <p>레거시 SearchController.fetchSearchResults 변환. MySQL ngram FULLTEXT(Boolean Mode, prefix
     * match)로 상품명을 검색합니다. searchWord 미지정 시 전체 상품 조회로 동작합니다. orderType 미지정 시 RECOMMEND(score DESC)가
     * 자동 적용됩니다.
     *
     * @param request 검색 필터 요청 DTO
     * @return 검색 결과 슬라이스 응답
     */
    @Operation(
            summary = "상품 키워드 검색 (커서 페이징)",
            description =
                    "키워드로 상품을 검색합니다. MySQL ngram FULLTEXT(Boolean Mode) 사용."
                            + " searchWord 미지정 시 전체 조회."
                            + " orderType 미지정 시 RECOMMEND 자동 적용."
                            + " 레거시 /api/v1/search 호환.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "검색 성공")
    })
    @GetMapping
    public ResponseEntity<V1ApiResponse<SearchSliceV1ApiResponse>> fetchSearchResults(
            @ParameterObject @ModelAttribute SearchProductsCursorV1ApiRequest request) {

        ProductGroupSearchParams params = mapper.toSearchParams(request);
        ProductGroupSliceResult result = searchProductGroupsUseCase.execute(params);
        boolean isFirstPage = request.lastDomainId() == null;
        SearchSliceV1ApiResponse response = mapper.toSliceResponse(result, isFirstPage);

        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
