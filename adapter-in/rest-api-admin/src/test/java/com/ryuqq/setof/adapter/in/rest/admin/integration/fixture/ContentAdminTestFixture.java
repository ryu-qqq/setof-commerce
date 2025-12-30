package com.ryuqq.setof.adapter.in.rest.admin.integration.fixture;

import com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.command.CreateContentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.command.UpdateContentV2ApiRequest;
import com.ryuqq.setof.application.content.dto.response.ContentResponse;
import com.ryuqq.setof.application.content.dto.response.ContentSummaryResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Content Admin 통합 테스트용 Fixture
 *
 * @author development-team
 * @since 2.0.0
 */
public final class ContentAdminTestFixture {

    private ContentAdminTestFixture() {}

    // ===== Content IDs =====
    public static final Long CONTENT_ID_1 = 1L;
    public static final Long CONTENT_ID_2 = 2L;
    public static final Long CONTENT_ID_3 = 3L;
    public static final Long NON_EXISTENT_CONTENT_ID = 99999L;

    // ===== Content 속성 =====
    public static final String CONTENT_TITLE = "메인 페이지 신규 컬렉션";
    public static final String CONTENT_MEMO = "2024년 여름 컬렉션 프로모션";
    public static final String CONTENT_IMAGE_URL = "https://cdn.example.com/images/content1.jpg";
    public static final String CONTENT_STATUS_ACTIVE = "ACTIVE";
    public static final String CONTENT_STATUS_INACTIVE = "INACTIVE";

    // ===== Display Date =====
    public static final Instant DISPLAY_START_DATE = Instant.now().minus(1, ChronoUnit.DAYS);
    public static final Instant DISPLAY_END_DATE = Instant.now().plus(30, ChronoUnit.DAYS);

    // ===== Component Count =====
    public static final int DEFAULT_COMPONENT_COUNT = 3;

    /** 콘텐츠 생성 요청 생성 */
    public static CreateContentV2ApiRequest createContentRequest() {
        return new CreateContentV2ApiRequest(
                CONTENT_TITLE,
                CONTENT_MEMO,
                CONTENT_IMAGE_URL,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE);
    }

    /** 콘텐츠 생성 요청 생성 (커스텀) */
    public static CreateContentV2ApiRequest createContentRequest(
            String title, String memo, String imageUrl) {
        return new CreateContentV2ApiRequest(
                title, memo, imageUrl, DISPLAY_START_DATE, DISPLAY_END_DATE);
    }

    /** 콘텐츠 수정 요청 생성 */
    public static UpdateContentV2ApiRequest updateContentRequest() {
        return new UpdateContentV2ApiRequest(
                CONTENT_TITLE + " (수정)",
                CONTENT_MEMO + " (수정)",
                CONTENT_IMAGE_URL,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE.plus(30, ChronoUnit.DAYS));
    }

    /** 콘텐츠 수정 요청 생성 (커스텀) */
    public static UpdateContentV2ApiRequest updateContentRequest(
            String title, String memo, String imageUrl) {
        return new UpdateContentV2ApiRequest(
                title, memo, imageUrl, DISPLAY_START_DATE, DISPLAY_END_DATE);
    }

    /** 콘텐츠 응답 생성 (Mock용) */
    public static ContentResponse createContentResponse(Long contentId) {
        return ContentResponse.of(
                contentId,
                CONTENT_TITLE,
                CONTENT_MEMO,
                CONTENT_IMAGE_URL,
                CONTENT_STATUS_ACTIVE,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE,
                Instant.now().minus(7, ChronoUnit.DAYS),
                Instant.now());
    }

    /** 콘텐츠 응답 생성 (커스텀 상태) */
    public static ContentResponse createContentResponse(Long contentId, String status) {
        return ContentResponse.of(
                contentId,
                CONTENT_TITLE,
                CONTENT_MEMO,
                CONTENT_IMAGE_URL,
                status,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE,
                Instant.now().minus(7, ChronoUnit.DAYS),
                Instant.now());
    }

    /** 콘텐츠 요약 응답 생성 (Mock용) */
    public static ContentSummaryResponse createContentSummaryResponse(Long contentId) {
        return ContentSummaryResponse.of(
                contentId,
                CONTENT_TITLE,
                CONTENT_STATUS_ACTIVE,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE,
                DEFAULT_COMPONENT_COUNT);
    }

    /** 콘텐츠 요약 응답 생성 (커스텀) */
    public static ContentSummaryResponse createContentSummaryResponse(
            Long contentId, String title, String status, int componentCount) {
        return ContentSummaryResponse.of(
                contentId, title, status, DISPLAY_START_DATE, DISPLAY_END_DATE, componentCount);
    }

    /** 콘텐츠 요약 응답 목록 생성 (Mock용) */
    public static List<ContentSummaryResponse> createContentSummaryResponses() {
        return List.of(
                createContentSummaryResponse(CONTENT_ID_1, "콘텐츠 1", CONTENT_STATUS_ACTIVE, 3),
                createContentSummaryResponse(CONTENT_ID_2, "콘텐츠 2", CONTENT_STATUS_ACTIVE, 5),
                createContentSummaryResponse(CONTENT_ID_3, "콘텐츠 3", CONTENT_STATUS_INACTIVE, 0));
    }
}
