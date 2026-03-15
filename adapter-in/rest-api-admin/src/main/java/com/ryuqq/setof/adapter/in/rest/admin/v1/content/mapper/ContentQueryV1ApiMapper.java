package com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SearchContentsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.ContentDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.ContentV1ApiResponse;
import com.ryuqq.setof.application.contentpage.dto.ContentPageDetailResult;
import com.ryuqq.setof.application.contentpage.dto.ContentPageSearchParams;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ContentQueryV1ApiMapper - v1 콘텐츠 Query API 변환 매퍼.
 *
 * <p>Domain 객체(ContentPage, DisplayComponent)를 레거시 v1 응답 DTO로 변환합니다.
 *
 * <p>변환 규칙:
 *
 * <ul>
 *   <li>active boolean → displayYn "Y"/"N"
 *   <li>Instant (UTC) → LocalDateTime (KST)
 *   <li>contentPageId → contentId
 *   <li>displayComponentId → componentId
 *   <li>periodType "DISPLAY" → 전시 기간 검색, "INSERT" → 등록일 검색
 *   <li>searchKeyword "CONTENT_TITLE" → 제목 검색, "CONTENT_ID" → ID 검색
 *   <li>INSERT_OPERATOR/UPDATE_OPERATOR → 미지원 (새 도메인에 없음)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ContentQueryV1ApiMapper {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    /**
     * ContentPage → ContentV1ApiResponse 변환 (목록용).
     *
     * <p>레거시 응답에는 insertOperator/updateOperator가 포함되지만, 새 도메인에는 없으므로 빈 문자열로 반환합니다.
     *
     * @param page 콘텐츠 페이지 도메인 객체
     * @return ContentV1ApiResponse
     */
    public ContentV1ApiResponse toContentResponse(ContentPage page) {
        return ContentV1ApiResponse.of(
                page.idValue(),
                toDisplayYn(page.isActive()),
                page.title(),
                ContentV1ApiResponse.DisplayPeriodResponse.of(
                        toLocalDateTime(page.displayPeriod().startDate()),
                        toLocalDateTime(page.displayPeriod().endDate())),
                "",
                "",
                toLocalDateTime(page.createdAt()),
                toLocalDateTime(page.updatedAt()));
    }

    /**
     * ContentPageDetailResult → ContentDetailV1ApiResponse 변환 (상세용).
     *
     * @param result 콘텐츠 페이지 상세 조회 결과
     * @return ContentDetailV1ApiResponse
     */
    public ContentDetailV1ApiResponse toContentDetailResponse(ContentPageDetailResult result) {
        ContentPage page = result.contentPage();

        List<ContentDetailV1ApiResponse.ComponentResponse> components =
                result.displayComponents().stream().map(this::toComponentResponse).toList();

        return ContentDetailV1ApiResponse.of(
                page.idValue(),
                ContentDetailV1ApiResponse.DisplayPeriodResponse.of(
                        toLocalDateTime(page.displayPeriod().startDate()),
                        toLocalDateTime(page.displayPeriod().endDate())),
                page.title(),
                page.memo(),
                page.imageUrl(),
                toDisplayYn(page.isActive()),
                components);
    }

    /**
     * SearchContentsV1ApiRequest → ContentPageSearchParams 변환.
     *
     * @param request v1 콘텐츠 검색 요청 DTO
     * @return Application 검색 파라미터
     */
    public ContentPageSearchParams toSearchParams(SearchContentsV1ApiRequest request) {
        Instant displayPeriodStart = null;
        Instant displayPeriodEnd = null;
        Instant createdAtStart = null;
        Instant createdAtEnd = null;

        if ("DISPLAY".equalsIgnoreCase(request.periodType())) {
            displayPeriodStart = toInstant(request.startDate());
            displayPeriodEnd = toInstant(request.endDate());
        } else if ("INSERT".equalsIgnoreCase(request.periodType())) {
            createdAtStart = toInstant(request.startDate());
            createdAtEnd = toInstant(request.endDate());
        }

        String titleKeyword = resolveTitleKeyword(request.searchKeyword(), request.searchWord());
        Long contentPageId = resolveContentPageId(request.searchKeyword(), request.searchWord());

        return new ContentPageSearchParams(
                toActiveNullable(request.displayYn()),
                displayPeriodStart,
                displayPeriodEnd,
                createdAtStart,
                createdAtEnd,
                titleKeyword,
                contentPageId,
                request.lastDomainId(),
                request.page() != null ? request.page() : 0,
                request.size() != null ? request.size() : 20);
    }

    private ContentDetailV1ApiResponse.ComponentResponse toComponentResponse(
            DisplayComponent component) {
        Long viewExtensionId =
                component.viewExtension() != null
                        ? component.viewExtension().viewExtensionId()
                        : null;

        return ContentDetailV1ApiResponse.ComponentResponse.of(
                component.idValue(),
                component.displayOrder(),
                viewExtensionId,
                component.componentType().name(),
                component.name(),
                ContentDetailV1ApiResponse.DisplayPeriodResponse.of(
                        toLocalDateTime(component.displayPeriod().startDate()),
                        toLocalDateTime(component.displayPeriod().endDate())),
                toDisplayYn(component.isActive()),
                resolveExposedProducts(component));
    }

    private int resolveExposedProducts(DisplayComponent component) {
        if (component.componentSpec() == null) {
            return 0;
        }
        return switch (component.componentSpec()) {
            case com.ryuqq.setof.domain.contentpage.vo.ComponentSpec.ProductSpec s ->
                    s.exposedProducts();
            case com.ryuqq.setof.domain.contentpage.vo.ComponentSpec.BrandSpec s ->
                    s.exposedProducts();
            case com.ryuqq.setof.domain.contentpage.vo.ComponentSpec.CategorySpec s ->
                    s.exposedProducts();
            case com.ryuqq.setof.domain.contentpage.vo.ComponentSpec.TabSpec s ->
                    s.exposedProducts();
            default -> 0;
        };
    }

    private Boolean toActiveNullable(String displayYn) {
        if (displayYn == null || displayYn.isBlank()) {
            return null;
        }
        return "Y".equalsIgnoreCase(displayYn);
    }

    /**
     * searchKeyword가 CONTENT_TITLE일 때만 searchWord를 제목 검색어로 반환합니다. INSERT_OPERATOR/UPDATE_OPERATOR는
     * 새 도메인에 없으므로 무시합니다.
     */
    private String resolveTitleKeyword(String searchKeyword, String searchWord) {
        if ("CONTENT_TITLE".equalsIgnoreCase(searchKeyword)
                && searchWord != null
                && !searchWord.isBlank()) {
            return searchWord;
        }
        return null;
    }

    /** searchKeyword가 CONTENT_ID일 때 searchWord를 Long ID로 파싱합니다. */
    private Long resolveContentPageId(String searchKeyword, String searchWord) {
        if ("CONTENT_ID".equalsIgnoreCase(searchKeyword)
                && searchWord != null
                && !searchWord.isBlank()) {
            try {
                return Long.parseLong(searchWord.trim());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Instant toInstant(LocalDateTime ldt) {
        if (ldt == null) {
            return null;
        }
        return ldt.atZone(KST).toInstant();
    }

    private String toDisplayYn(boolean active) {
        return active ? "Y" : "N";
    }

    private LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return instant.atZone(KST).toLocalDateTime();
    }
}
