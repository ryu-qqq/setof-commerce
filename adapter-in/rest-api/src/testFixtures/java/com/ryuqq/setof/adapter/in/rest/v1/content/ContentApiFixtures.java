package com.ryuqq.setof.adapter.in.rest.v1.content;

import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.ContentMetaV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.ContentV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.OnDisplayContentV1ApiResponse;
import com.ryuqq.setof.application.contentpage.dto.ContentPageDetailResult;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.id.ContentPageId;
import com.ryuqq.setof.domain.contentpage.id.DisplayComponentId;
import com.ryuqq.setof.domain.contentpage.vo.BadgeType;
import com.ryuqq.setof.domain.contentpage.vo.ComponentProductBundle;
import com.ryuqq.setof.domain.contentpage.vo.ComponentSpec;
import com.ryuqq.setof.domain.contentpage.vo.ComponentType;
import com.ryuqq.setof.domain.contentpage.vo.DisplayConfig;
import com.ryuqq.setof.domain.contentpage.vo.ListType;
import com.ryuqq.setof.domain.contentpage.vo.OrderType;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Content V1 API 테스트 Fixtures.
 *
 * <p>ContentPage 관련 Domain 객체 및 API Response 객체를 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ContentApiFixtures {

    private ContentApiFixtures() {}

    // ===== DisplayPeriod 헬퍼 =====

    public static DisplayPeriod alwaysPeriod() {
        return DisplayPeriod.of(
                Instant.parse("2000-01-01T00:00:00Z"), Instant.parse("2099-12-31T23:59:59Z"));
    }

    // ===== ContentPage Domain Fixtures =====

    public static ContentPage contentPage(long id) {
        return ContentPage.reconstitute(
                ContentPageId.of(id),
                "메인 콘텐츠",
                "메인 페이지용 콘텐츠",
                "https://cdn.example.com/content/1.jpg",
                alwaysPeriod(),
                true,
                DeletionStatus.active(),
                Instant.parse("2025-01-01T00:00:00Z"),
                Instant.parse("2025-01-01T00:00:00Z"));
    }

    // ===== ProductThumbnailSnapshot Fixtures =====

    public static ProductThumbnailSnapshot productThumbnailSnapshot(long productGroupId) {
        return new ProductThumbnailSnapshot(
                productGroupId,
                1L,
                "테스트 상품",
                10L,
                "NIKE",
                "https://cdn.example.com/products/" + productGroupId + ".jpg",
                null,
                null,
                10000,
                8000,
                8000,
                0,
                0,
                20,
                Instant.parse("2025-01-01T00:00:00Z"),
                true,
                false);
    }

    // ===== DisplayComponent Fixtures =====

    public static DisplayComponent textDisplayComponent(long componentId, long contentPageId) {
        return DisplayComponent.reconstitute(
                DisplayComponentId.of(componentId),
                contentPageId,
                "텍스트 컴포넌트",
                1,
                ComponentType.TEXT,
                new DisplayConfig(ListType.TWO_STEP, OrderType.NONE, BadgeType.NONE, false),
                alwaysPeriod(),
                true,
                null,
                new ComponentSpec.TextSpec(componentId * 100, "텍스트 내용"),
                DeletionStatus.active(),
                Instant.parse("2025-01-01T00:00:00Z"),
                Instant.parse("2025-01-01T00:00:00Z"));
    }

    // ===== ComponentProductBundle Fixtures =====

    public static ComponentProductBundle emptyBundle() {
        return ComponentProductBundle.empty();
    }

    public static ComponentProductBundle bundleWithComponent(
            long componentId, List<ProductThumbnailSnapshot> snapshots) {
        return new ComponentProductBundle(Map.of(componentId, snapshots), Map.of());
    }

    // ===== ContentPageDetailResult Fixtures =====

    public static ContentPageDetailResult contentPageDetailResult(long contentPageId) {
        ContentPage page = contentPage(contentPageId);
        DisplayComponent component = textDisplayComponent(1L, contentPageId);
        return new ContentPageDetailResult(page, List.of(component), emptyBundle());
    }

    public static ContentPageDetailResult contentPageDetailResultEmpty(long contentPageId) {
        ContentPage page = contentPage(contentPageId);
        return new ContentPageDetailResult(page, List.of(), emptyBundle());
    }

    // ===== On-Display Response Fixtures =====

    public static Set<Long> onDisplayContentIds() {
        return Set.of(1L, 2L, 3L);
    }

    public static OnDisplayContentV1ApiResponse onDisplayResponse() {
        return new OnDisplayContentV1ApiResponse(onDisplayContentIds());
    }

    // ===== ContentMetaV1ApiResponse Fixtures =====

    public static ContentMetaV1ApiResponse contentMetaResponse(long contentId) {
        return new ContentMetaV1ApiResponse(
                contentId,
                new ContentMetaV1ApiResponse.DisplayPeriodResponse(
                        "2000-01-01 00:00:00", "2099-12-31 23:59:59"),
                "메인 콘텐츠",
                "메인 페이지용 콘텐츠",
                "https://cdn.example.com/content/1.jpg",
                List.of());
    }

    // ===== ContentV1ApiResponse Fixtures =====

    public static ContentV1ApiResponse contentResponse(long contentId) {
        return new ContentV1ApiResponse(
                contentId,
                new ContentV1ApiResponse.DisplayPeriodResponse(
                        "2000-01-01 00:00:00", "2099-12-31 23:59:59"),
                "메인 콘텐츠",
                "메인 페이지용 콘텐츠",
                "https://cdn.example.com/content/1.jpg",
                List.of());
    }
}
