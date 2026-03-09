package com.ryuqq.setof.adapter.in.rest.v1.content.controller;

import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.content.ContentV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.ContentMetaV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.ContentV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.OnDisplayContentV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.content.mapper.ContentV1ApiMapper;
import com.ryuqq.setof.application.contentpage.dto.ContentPageDetailResult;
import com.ryuqq.setof.application.contentpage.port.in.GetContentPageDetailUseCase;
import com.ryuqq.setof.application.contentpage.port.in.GetContentPageMetaUseCase;
import com.ryuqq.setof.application.contentpage.port.in.GetOnDisplayContentPageIdsUseCase;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ContentQueryV1Controller - 콘텐츠 페이지 조회 V1 Public API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "콘텐츠 조회 V1", description = "콘텐츠 페이지 조회 V1 Public API")
@RestController
@RequestMapping(ContentV1Endpoints.CONTENT)
public class ContentQueryV1Controller {

    private final GetOnDisplayContentPageIdsUseCase getOnDisplayContentPageIdsUseCase;
    private final GetContentPageMetaUseCase getContentPageMetaUseCase;
    private final GetContentPageDetailUseCase getContentPageDetailUseCase;
    private final ContentV1ApiMapper mapper;

    public ContentQueryV1Controller(
            GetOnDisplayContentPageIdsUseCase getOnDisplayContentPageIdsUseCase,
            GetContentPageMetaUseCase getContentPageMetaUseCase,
            GetContentPageDetailUseCase getContentPageDetailUseCase,
            ContentV1ApiMapper mapper) {
        this.getOnDisplayContentPageIdsUseCase = getOnDisplayContentPageIdsUseCase;
        this.getContentPageMetaUseCase = getContentPageMetaUseCase;
        this.getContentPageDetailUseCase = getContentPageDetailUseCase;
        this.mapper = mapper;
    }

    /**
     * 전시 중인 콘텐츠 ID 목록 조회.
     *
     * <p>GET /api/v1/content/on-display
     *
     * @return 전시 중인 콘텐츠 ID 목록
     */
    @Operation(summary = "전시 중인 콘텐츠 ID 목록 조회", description = "현재 전시 중인 콘텐츠 페이지의 ID 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping("/on-display")
    public ResponseEntity<V1ApiResponse<OnDisplayContentV1ApiResponse>> fetchOnDisplayContents() {
        Set<Long> contentIds = getOnDisplayContentPageIdsUseCase.execute();
        OnDisplayContentV1ApiResponse response = mapper.toOnDisplayResponse(contentIds);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 콘텐츠 메타데이터 조회.
     *
     * <p>GET /api/v1/content/meta/{contentId}
     *
     * @param contentId 콘텐츠 ID
     * @return 콘텐츠 메타데이터
     */
    @Operation(summary = "콘텐츠 메타데이터 조회", description = "콘텐츠 페이지의 메타데이터를 조회합니다 (컴포넌트 제외).")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping("/meta/{contentId}")
    public ResponseEntity<V1ApiResponse<ContentMetaV1ApiResponse>> fetchContentMetaData(
            @Parameter(description = "콘텐츠 ID", required = true, example = "1") @PathVariable
                    long contentId) {
        ContentPage page = getContentPageMetaUseCase.execute(contentId);
        ContentMetaV1ApiResponse response = mapper.toMetaResponse(page);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 콘텐츠 상세 조회 (컴포넌트 포함).
     *
     * <p>GET /api/v1/content/{contentId}
     *
     * @param contentId 콘텐츠 ID
     * @param bypass 전시 기간 체크 우회 여부
     * @return 콘텐츠 상세
     */
    @Operation(summary = "콘텐츠 상세 조회", description = "콘텐츠 페이지의 상세 정보를 조회합니다 (컴포넌트 포함).")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공")
    })
    @GetMapping("/{contentId}")
    public ResponseEntity<V1ApiResponse<ContentV1ApiResponse>> fetchContent(
            @Parameter(description = "콘텐츠 ID", required = true, example = "1") @PathVariable
                    long contentId,
            @Parameter(description = "전시 기간 체크 우회 여부 (Y/N)", example = "N")
                    @RequestParam(value = "bypass", required = false)
                    String bypass) {
        boolean bypassFlag = "Y".equalsIgnoreCase(bypass);
        ContentPageSearchCriteria criteria = new ContentPageSearchCriteria(contentId, bypassFlag);

        ContentPageDetailResult detail = getContentPageDetailUseCase.execute(criteria);

        ContentV1ApiResponse response =
                mapper.toContentResponse(
                        detail.contentPage(), detail.displayComponents(), detail.productBundle());
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
