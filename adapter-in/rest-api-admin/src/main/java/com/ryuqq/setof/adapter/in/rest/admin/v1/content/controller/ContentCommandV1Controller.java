package com.ryuqq.setof.adapter.in.rest.admin.v1.content.controller;

import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.CreateContentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.UpdateContentDisplayYnV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.ContentV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper.ContentCommandV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper.ContentQueryV1ApiMapper;
import com.ryuqq.setof.application.contentpage.dto.command.ChangeContentPageStatusCommand;
import com.ryuqq.setof.application.contentpage.dto.command.RegisterContentPageCommand;
import com.ryuqq.setof.application.contentpage.dto.command.UpdateContentPageCommand;
import com.ryuqq.setof.application.contentpage.port.in.GetContentPageMetaUseCase;
import com.ryuqq.setof.application.contentpage.port.in.command.ChangeContentPageStatusUseCase;
import com.ryuqq.setof.application.contentpage.port.in.command.RegisterContentPageUseCase;
import com.ryuqq.setof.application.contentpage.port.in.command.UpdateContentPageUseCase;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ContentCommandV1Controller - 콘텐츠 등록/수정/상태변경 v1 API.
 *
 * <p>레거시 콘텐츠 Command API와 동일한 URL, HTTP 메서드, 요청/응답 구조를 유지합니다.
 *
 * <p>레거시 호환 URL:
 *
 * <ul>
 *   <li>POST /api/v1/content — 콘텐츠 등록/수정 (contentId 유무로 분기)
 *   <li>PATCH /api/v1/content/{contentId}/display-status — 노출 상태 변경
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "콘텐츠 관리 v1", description = "콘텐츠 등록/수정/상태변경 v1 API (레거시 호환)")
@RestController
@RequestMapping("/api/v1")
public class ContentCommandV1Controller {

    private final ContentCommandV1ApiMapper commandMapper;
    private final ContentQueryV1ApiMapper queryMapper;
    private final RegisterContentPageUseCase registerContentPageUseCase;
    private final UpdateContentPageUseCase updateContentPageUseCase;
    private final ChangeContentPageStatusUseCase changeContentPageStatusUseCase;
    private final GetContentPageMetaUseCase getContentPageMetaUseCase;

    /**
     * ContentCommandV1Controller 생성자.
     *
     * @param commandMapper 콘텐츠 Command API 매퍼
     * @param queryMapper 콘텐츠 Query API 매퍼 (응답 변환용)
     * @param registerContentPageUseCase 콘텐츠 페이지 등록 UseCase
     * @param updateContentPageUseCase 콘텐츠 페이지 수정 UseCase
     * @param changeContentPageStatusUseCase 콘텐츠 페이지 노출 상태 변경 UseCase
     * @param getContentPageMetaUseCase 콘텐츠 페이지 메타 조회 UseCase
     */
    public ContentCommandV1Controller(
            ContentCommandV1ApiMapper commandMapper,
            ContentQueryV1ApiMapper queryMapper,
            RegisterContentPageUseCase registerContentPageUseCase,
            UpdateContentPageUseCase updateContentPageUseCase,
            ChangeContentPageStatusUseCase changeContentPageStatusUseCase,
            GetContentPageMetaUseCase getContentPageMetaUseCase) {
        this.commandMapper = commandMapper;
        this.queryMapper = queryMapper;
        this.registerContentPageUseCase = registerContentPageUseCase;
        this.updateContentPageUseCase = updateContentPageUseCase;
        this.changeContentPageStatusUseCase = changeContentPageStatusUseCase;
        this.getContentPageMetaUseCase = getContentPageMetaUseCase;
    }

    /**
     * 콘텐츠 등록/수정 API.
     *
     * <p>contentId가 null이거나 0이면 등록, 양수이면 수정으로 처리합니다.
     *
     * <p>레거시 응답 호환을 위해 등록/수정 후 ContentV1ApiResponse를 반환합니다.
     *
     * @param request 콘텐츠 등록/수정 요청 DTO
     * @return 등록/수정된 콘텐츠 응답
     */
    @Operation(summary = "콘텐츠 등록/수정", description = "contentId가 null이면 등록, 양수이면 수정으로 처리합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "처리 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "수정 시 콘텐츠를 찾을 수 없음")
    })
    @PostMapping("/content")
    public ResponseEntity<V1ApiResponse<ContentV1ApiResponse>> enrollContent(
            @Validated @RequestBody CreateContentV1ApiRequest request) {

        long contentPageId;

        if (request.contentId() != null && request.contentId() > 0) {
            UpdateContentPageCommand command = commandMapper.toUpdateCommand(request);
            updateContentPageUseCase.execute(command);
            contentPageId = request.contentId();
        } else {
            RegisterContentPageCommand command = commandMapper.toRegisterCommand(request);
            contentPageId = registerContentPageUseCase.execute(command);
        }

        ContentPage contentPage = getContentPageMetaUseCase.execute(contentPageId);
        ContentV1ApiResponse response = queryMapper.toContentResponse(contentPage);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    /**
     * 콘텐츠 노출 상태 변경 API.
     *
     * @param contentId 콘텐츠 ID
     * @param request 노출 상태 변경 요청 DTO
     * @return 변경된 콘텐츠 응답
     */
    @Operation(summary = "콘텐츠 노출 상태 변경", description = "콘텐츠의 전시 여부(displayYn)를 변경합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "상태 변경 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "콘텐츠를 찾을 수 없음")
    })
    @PatchMapping("/content/{contentId}/display-status")
    public ResponseEntity<V1ApiResponse<ContentV1ApiResponse>> updateContentDisplayYn(
            @Parameter(description = "콘텐츠 ID", required = true) @PathVariable long contentId,
            @RequestBody UpdateContentDisplayYnV1ApiRequest request) {

        ChangeContentPageStatusCommand command =
                commandMapper.toChangeStatusCommand(contentId, request);
        changeContentPageStatusUseCase.execute(command);

        ContentPage contentPage = getContentPageMetaUseCase.execute(contentId);
        ContentV1ApiResponse response = queryMapper.toContentResponse(contentPage);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
