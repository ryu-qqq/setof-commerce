package com.ryuqq.setof.adapter.in.rest.admin.content;

import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.ComponentDetailsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.CreateContentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.DisplayPeriodV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.BlankComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.BrandComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.BrandItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.CategoryComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.ImageComponentLinkV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.ImageComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.ProductComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.TabComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.TabDetailV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.TextComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.TitleComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.UpdateContentDisplayYnV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.ViewExtensionDetailsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.ContentV1ApiResponse;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.id.ContentPageId;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * Content Command v1 API 테스트 Fixtures.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ContentCommandV1ApiFixtures {

    private ContentCommandV1ApiFixtures() {}

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    public static final LocalDateTime DEFAULT_START = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
    public static final LocalDateTime DEFAULT_END = LocalDateTime.of(2025, 12, 31, 23, 59, 59);
    public static final long DEFAULT_CONTENT_ID = 1L;

    // ===== DisplayPeriod Fixtures =====

    public static DisplayPeriodV1ApiRequest defaultDisplayPeriod() {
        return new DisplayPeriodV1ApiRequest(DEFAULT_START, DEFAULT_END);
    }

    // ===== ComponentDetails Fixtures =====

    public static ComponentDetailsV1ApiRequest defaultComponentDetails() {
        return new ComponentDetailsV1ApiRequest("PRODUCT", "GRID", "POPULAR", "NEW", "Y");
    }

    public static ComponentDetailsV1ApiRequest componentDetailsWithFilterN() {
        return new ComponentDetailsV1ApiRequest("PRODUCT", "LIST", "RECENT", "NONE", "N");
    }

    // ===== ViewExtensionDetails Fixtures =====

    public static ViewExtensionDetailsV1ApiRequest defaultViewExtensionDetails() {
        return new ViewExtensionDetailsV1ApiRequest(
                "MORE_BUTTON", "/products/more", "더보기", 20, 5, "HIDE", "/products/end");
    }

    // ===== SubComponent Fixtures =====

    public static BrandComponentV1ApiRequest brandComponent() {
        return new BrandComponentV1ApiRequest(
                10L,
                null,
                defaultDisplayPeriod(),
                "브랜드 컴포넌트",
                1,
                "Y",
                List.of(
                        new BrandItemV1ApiRequest(100L, "나이키"),
                        new BrandItemV1ApiRequest(101L, "아디다스")),
                200L,
                defaultComponentDetails(),
                10,
                null,
                defaultViewExtensionDetails());
    }

    public static CategoryComponentV1ApiRequest categoryComponent() {
        return new CategoryComponentV1ApiRequest(
                20L,
                null,
                defaultDisplayPeriod(),
                "카테고리 컴포넌트",
                2,
                "Y",
                300L,
                defaultComponentDetails(),
                0,
                null,
                null);
    }

    public static ProductComponentV1ApiRequest productComponent() {
        return new ProductComponentV1ApiRequest(
                30L,
                null,
                defaultDisplayPeriod(),
                "상품 컴포넌트",
                3,
                "Y",
                20,
                defaultComponentDetails(),
                null,
                defaultViewExtensionDetails());
    }

    public static TabComponentV1ApiRequest tabComponent() {
        return new TabComponentV1ApiRequest(
                40L,
                null,
                defaultDisplayPeriod(),
                "탭 컴포넌트",
                4,
                "Y",
                List.of(
                        new TabDetailV1ApiRequest(400L, "신상품", "Y", "SCROLL", 1),
                        new TabDetailV1ApiRequest(401L, "베스트", "N", "SCROLL", 2)),
                0,
                defaultComponentDetails(),
                null,
                null);
    }

    public static ImageComponentV1ApiRequest imageComponent() {
        return new ImageComponentV1ApiRequest(
                50L,
                null,
                defaultDisplayPeriod(),
                "이미지 컴포넌트",
                5,
                "Y",
                "BANNER",
                List.of(
                        new ImageComponentLinkV1ApiRequest(
                                500L, 1, "https://img.example.com/1.jpg", "/event/1"),
                        new ImageComponentLinkV1ApiRequest(
                                501L, 2, "https://img.example.com/2.jpg", "/event/2")),
                null);
    }

    public static TextComponentV1ApiRequest textComponent() {
        return new TextComponentV1ApiRequest(
                60L, null, defaultDisplayPeriod(), "텍스트 컴포넌트", 6, "Y", "이번 주 특가 상품을 확인하세요!", null);
    }

    public static TitleComponentV1ApiRequest titleComponent() {
        return new TitleComponentV1ApiRequest(
                70L,
                null,
                defaultDisplayPeriod(),
                "타이틀 컴포넌트",
                7,
                "Y",
                "메인 타이틀",
                "서브 타이틀",
                "소제목1",
                "소제목2",
                null);
    }

    public static BlankComponentV1ApiRequest blankComponent() {
        return new BlankComponentV1ApiRequest(
                80L, null, defaultDisplayPeriod(), "여백 컴포넌트", 8, "Y", 16.0, "N", null);
    }

    // ===== CreateContentV1ApiRequest Fixtures =====

    public static CreateContentV1ApiRequest createRequest() {
        return new CreateContentV1ApiRequest(
                null,
                defaultDisplayPeriod(),
                "메인 콘텐츠",
                "테스트용 메모",
                "https://img.example.com/main.jpg",
                "Y",
                List.of(brandComponent()));
    }

    public static CreateContentV1ApiRequest createRequestWithMultipleComponents() {
        return new CreateContentV1ApiRequest(
                null,
                defaultDisplayPeriod(),
                "멀티 컴포넌트 콘텐츠",
                "멀티 컴포넌트 테스트",
                "https://img.example.com/multi.jpg",
                "Y",
                List.of(
                        brandComponent(),
                        categoryComponent(),
                        productComponent(),
                        tabComponent(),
                        imageComponent(),
                        textComponent(),
                        titleComponent(),
                        blankComponent()));
    }

    public static CreateContentV1ApiRequest updateRequest(Long contentId) {
        return new CreateContentV1ApiRequest(
                contentId,
                defaultDisplayPeriod(),
                "수정된 콘텐츠",
                "수정 메모",
                "https://img.example.com/updated.jpg",
                "N",
                List.of(productComponent()));
    }

    public static CreateContentV1ApiRequest createRequestWithDisplayYnN() {
        return new CreateContentV1ApiRequest(
                null, defaultDisplayPeriod(), "비전시 콘텐츠", null, null, "N", List.of(textComponent()));
    }

    // ===== UpdateContentDisplayYnV1ApiRequest Fixtures =====

    public static UpdateContentDisplayYnV1ApiRequest displayYnY() {
        return new UpdateContentDisplayYnV1ApiRequest("Y");
    }

    public static UpdateContentDisplayYnV1ApiRequest displayYnN() {
        return new UpdateContentDisplayYnV1ApiRequest("N");
    }

    // ===== ContentV1ApiResponse Fixtures =====

    public static ContentV1ApiResponse contentResponse(long contentId) {
        return ContentV1ApiResponse.of(
                contentId,
                "Y",
                "메인 콘텐츠",
                ContentV1ApiResponse.DisplayPeriodResponse.of(DEFAULT_START, DEFAULT_END),
                "",
                "",
                DEFAULT_START,
                DEFAULT_START);
    }

    // ===== ContentPage Domain Fixtures =====

    public static ContentPage contentPage(long contentId) {
        Instant start = DEFAULT_START.atZone(KST).toInstant();
        Instant end = DEFAULT_END.atZone(KST).toInstant();
        Instant now = start;
        return ContentPage.reconstitute(
                ContentPageId.of(contentId),
                "메인 콘텐츠",
                "테스트용 메모",
                "https://img.example.com/main.jpg",
                DisplayPeriod.of(start, end),
                true,
                DeletionStatus.active(),
                now,
                now);
    }
}
