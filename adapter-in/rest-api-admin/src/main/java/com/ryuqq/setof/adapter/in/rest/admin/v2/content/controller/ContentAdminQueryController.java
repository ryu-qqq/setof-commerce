package com.ryuqq.setof.adapter.in.rest.admin.v2.content.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.query.SearchContentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.response.ContentSummaryV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.response.ContentV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.content.mapper.ContentAdminV2ApiMapper;
import com.ryuqq.setof.application.content.dto.query.SearchContentQuery;
import com.ryuqq.setof.application.content.dto.response.ContentResponse;
import com.ryuqq.setof.application.content.dto.response.ContentSummaryResponse;
import com.ryuqq.setof.application.content.port.in.query.GetContentUseCase;
import com.ryuqq.setof.application.content.port.in.query.SearchContentUseCase;
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
 * Content Admin 조회 Controller
 *
 * <p>CQRS Query 담당: 조회 전용 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Content Admin", description = "콘텐츠 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Contents.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class ContentAdminQueryController {

    private final GetContentUseCase getContentUseCase;
    private final SearchContentUseCase searchContentUseCase;
    private final ContentAdminV2ApiMapper mapper;

    public ContentAdminQueryController(
            GetContentUseCase getContentUseCase,
            SearchContentUseCase searchContentUseCase,
            ContentAdminV2ApiMapper mapper) {
        this.getContentUseCase = getContentUseCase;
        this.searchContentUseCase = searchContentUseCase;
        this.mapper = mapper;
    }

    /**
     * 콘텐츠 목록 조회
     *
     * @param request 검색 조건
     * @return 콘텐츠 목록
     */
    @Operation(summary = "콘텐츠 목록 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ContentSummaryV2ApiResponse>>> search(
            @ParameterObject SearchContentV2ApiRequest request) {
        SearchContentQuery query = mapper.toSearchQuery(request);
        List<ContentSummaryResponse> responses = searchContentUseCase.execute(query);
        List<ContentSummaryV2ApiResponse> apiResponses =
                responses.stream().map(ContentSummaryV2ApiResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponses));
    }

    /**
     * 콘텐츠 단건 조회
     *
     * @param contentId 콘텐츠 ID
     * @return 콘텐츠 정보
     */
    @Operation(summary = "콘텐츠 단건 조회")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공")
            })
    @GetMapping(ApiV2Paths.Contents.ID_PATH)
    public ResponseEntity<ApiResponse<ContentV2ApiResponse>> getById(
            @Parameter(description = "콘텐츠 ID", required = true) @PathVariable Long contentId) {
        ContentResponse response = getContentUseCase.execute(contentId);
        return ResponseEntity.ok(ApiResponse.ofSuccess(ContentV2ApiResponse.from(response)));
    }
}
