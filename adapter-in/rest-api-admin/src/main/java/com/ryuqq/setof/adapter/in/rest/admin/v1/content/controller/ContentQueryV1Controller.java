package com.ryuqq.setof.adapter.in.rest.admin.v1.content.controller;

import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SearchContentsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.ContentDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.ContentV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper.ContentQueryV1ApiMapper;
import com.ryuqq.setof.application.contentpage.dto.ContentPageDetailResult;
import com.ryuqq.setof.application.contentpage.dto.ContentPagePageResult;
import com.ryuqq.setof.application.contentpage.dto.ContentPageSearchParams;
import com.ryuqq.setof.application.contentpage.port.in.GetContentPageDetailUseCase;
import com.ryuqq.setof.application.contentpage.port.in.SearchContentPagesUseCase;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ContentQueryV1Controller - 콘텐츠 조회 v1 API.
 *
 * <p>레거시 콘텐츠 조회 API와 동일한 URL, HTTP 메서드, 요청/응답 구조를 유지합니다.
 *
 * <p>레거시 호환 URL:
 *
 * <ul>
 *   <li>GET /api/v1/contents — 콘텐츠 목록 조회 (검색/페이징)
 *   <li>GET /api/v1/content/{contentId} — 콘텐츠 상세 조회 (컴포넌트 포함)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "콘텐츠 조회 v1", description = "콘텐츠 목록/상세 조회 v1 API (레거시 호환)")
@RestController
@RequestMapping("/api/v1")
public class ContentQueryV1Controller {

    private final SearchContentPagesUseCase searchContentPagesUseCase;
    private final GetContentPageDetailUseCase getContentPageDetailUseCase;
    private final ContentQueryV1ApiMapper queryMapper;

    /**
     * ContentQueryV1Controller 생성자.
     *
     * @param searchContentPagesUseCase 콘텐츠 페이지 검색 UseCase
     * @param getContentPageDetailUseCase 콘텐츠 페이지 상세 조회 UseCase
     * @param queryMapper 콘텐츠 Query API 매퍼
     */
    public ContentQueryV1Controller(
            SearchContentPagesUseCase searchContentPagesUseCase,
            GetContentPageDetailUseCase getContentPageDetailUseCase,
            ContentQueryV1ApiMapper queryMapper) {
        this.searchContentPagesUseCase = searchContentPagesUseCase;
        this.getContentPageDetailUseCase = getContentPageDetailUseCase;
        this.queryMapper = queryMapper;
    }

    /**
     * 콘텐츠 목록 조회 API.
     *
     * <p>전시 여부, 기간, 검색어 등으로 필터링하여 페이징 조회합니다.
     *
     * @param filter 검색 필터 및 페이징 요청 DTO
     * @return 콘텐츠 페이지 결과
     */
    @Operation(summary = "콘텐츠 목록 조회", description = "검색 조건으로 콘텐츠 목록을 페이징 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청")
    })
    @GetMapping("/contents")
    public ResponseEntity<V1ApiResponse<CustomPageableV1ApiResponse<ContentV1ApiResponse>>>
            getContents(@ModelAttribute SearchContentsV1ApiRequest filter) {

        ContentPageSearchParams params = queryMapper.toSearchParams(filter);
        ContentPagePageResult pageResult = searchContentPagesUseCase.execute(params);

        List<ContentV1ApiResponse> items =
                pageResult.items().stream().map(queryMapper::toContentResponse).toList();

        CustomPageableV1ApiResponse<ContentV1ApiResponse> pageResponse =
                CustomPageableV1ApiResponse.of(
                        items,
                        pageResult.page(),
                        pageResult.size(),
                        pageResult.totalCount(),
                        pageResult.lastDomainId());

        return ResponseEntity.ok(V1ApiResponse.success(pageResponse));
    }

    /**
     * 콘텐츠 상세 조회 API.
     *
     * <p>콘텐츠 ID로 메타정보 + 컴포넌트 목록을 조회합니다.
     *
     * @param contentId 콘텐츠 ID
     * @return 콘텐츠 상세 정보
     */
    @Operation(summary = "콘텐츠 상세 조회", description = "콘텐츠 ID로 메타정보 및 컴포넌트 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "콘텐츠를 찾을 수 없음")
    })
    @GetMapping("/content/{contentId}")
    public ResponseEntity<V1ApiResponse<ContentDetailV1ApiResponse>> getContentDetail(
            @Parameter(description = "콘텐츠 ID", required = true) @PathVariable long contentId) {

        ContentPageSearchCriteria criteria = new ContentPageSearchCriteria(contentId, false);
        ContentPageDetailResult result = getContentPageDetailUseCase.execute(criteria);

        ContentDetailV1ApiResponse response = queryMapper.toContentDetailResponse(result);

        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
