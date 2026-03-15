package com.setof.commerce.domain.contentpage;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPageUpdateData;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponentUpdateData;
import com.ryuqq.setof.domain.contentpage.id.ContentPageId;
import com.ryuqq.setof.domain.contentpage.id.DisplayComponentId;
import com.ryuqq.setof.domain.contentpage.vo.BadgeType;
import com.ryuqq.setof.domain.contentpage.vo.ComponentSpec;
import com.ryuqq.setof.domain.contentpage.vo.ComponentType;
import com.ryuqq.setof.domain.contentpage.vo.DisplayConfig;
import com.ryuqq.setof.domain.contentpage.vo.DisplayTab;
import com.ryuqq.setof.domain.contentpage.vo.ListType;
import com.ryuqq.setof.domain.contentpage.vo.OrderType;
import com.ryuqq.setof.domain.contentpage.vo.ProductComponentGroup;
import com.ryuqq.setof.domain.contentpage.vo.ProductSlot;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import com.ryuqq.setof.domain.contentpage.vo.TabMovingType;
import java.time.Instant;
import java.util.List;

/**
 * ContentPage 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 ContentPage 관련 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ContentPageFixtures {

    private ContentPageFixtures() {}

    // ===== ContentPageId Fixtures =====

    public static ContentPageId defaultContentPageId() {
        return ContentPageId.of(1L);
    }

    public static ContentPageId contentPageId(Long value) {
        return ContentPageId.of(value);
    }

    // ===== ContentPage Aggregate Fixtures =====

    public static ContentPage newContentPage() {
        return ContentPage.forNew(
                "테스트 콘텐츠 페이지",
                "테스트 메모",
                "https://example.com/image.jpg",
                alwaysPeriod(),
                true,
                Instant.now());
    }

    public static ContentPage activeContentPage() {
        return ContentPage.reconstitute(
                ContentPageId.of(1L),
                "테스트 콘텐츠 페이지",
                "테스트 메모",
                "https://example.com/image.jpg",
                alwaysPeriod(),
                true,
                DeletionStatus.active(),
                Instant.now().minusSeconds(86400),
                Instant.now().minusSeconds(86400));
    }

    public static ContentPage activeContentPage(Long id) {
        return ContentPage.reconstitute(
                ContentPageId.of(id),
                "테스트 콘텐츠 페이지",
                "테스트 메모",
                "https://example.com/image.jpg",
                alwaysPeriod(),
                true,
                DeletionStatus.active(),
                Instant.now().minusSeconds(86400),
                Instant.now().minusSeconds(86400));
    }

    public static ContentPage inactiveContentPage() {
        return ContentPage.reconstitute(
                ContentPageId.of(2L),
                "비활성 콘텐츠 페이지",
                "비활성 메모",
                "https://example.com/inactive.jpg",
                alwaysPeriod(),
                false,
                DeletionStatus.active(),
                Instant.now().minusSeconds(86400),
                Instant.now().minusSeconds(86400));
    }

    public static ContentPage deletedContentPage() {
        Instant deletedAt = Instant.now().minusSeconds(86400);
        return ContentPage.reconstitute(
                ContentPageId.of(3L),
                "삭제된 콘텐츠 페이지",
                "삭제된 메모",
                "https://example.com/deleted.jpg",
                alwaysPeriod(),
                false,
                DeletionStatus.deletedAt(deletedAt),
                Instant.now().minusSeconds(86400),
                Instant.now().minusSeconds(86400));
    }

    // ===== ContentPageUpdateData Fixtures =====

    public static ContentPageUpdateData contentPageUpdateData() {
        return new ContentPageUpdateData(
                "수정된 콘텐츠 페이지",
                "수정된 메모",
                "https://example.com/updated.jpg",
                alwaysPeriod(),
                true,
                Instant.now());
    }

    // ===== DisplayComponentId Fixtures =====

    public static DisplayComponentId defaultDisplayComponentId() {
        return DisplayComponentId.of(1L);
    }

    // ===== DisplayComponentUpdateData Fixtures =====

    public static DisplayComponentUpdateData displayComponentUpdateData() {
        return new DisplayComponentUpdateData(
                "수정된 컴포넌트",
                2,
                defaultDisplayConfig(),
                null,
                null,
                alwaysPeriod(),
                true,
                Instant.now());
    }

    // ===== DisplayPeriod 헬퍼 =====

    public static DisplayPeriod alwaysPeriod() {
        return DisplayPeriod.of(
                Instant.parse("2000-01-01T00:00:00Z"), Instant.parse("2099-12-31T23:59:59Z"));
    }

    // ===== ProductThumbnailSnapshot Fixtures =====

    /**
     * 기본 ProductThumbnailSnapshot 생성.
     *
     * @param productGroupId 상품 그룹 ID
     * @return ProductThumbnailSnapshot
     */
    public static ProductThumbnailSnapshot snapshot(long productGroupId) {
        return new ProductThumbnailSnapshot(
                productGroupId,
                1L,
                "상품명-" + productGroupId,
                10L,
                "브랜드A",
                "https://example.com/image/" + productGroupId + ".jpg",
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

    /** 커스텀 가격/할인율을 가진 ProductThumbnailSnapshot 생성. */
    public static ProductThumbnailSnapshot snapshotWithDetails(
            long productGroupId, int regularPrice, int currentPrice, int discountRate) {
        return new ProductThumbnailSnapshot(
                productGroupId,
                1L,
                "상품명-" + productGroupId,
                10L,
                "브랜드A",
                "https://example.com/image/" + productGroupId + ".jpg",
                null,
                null,
                regularPrice,
                currentPrice,
                currentPrice,
                0,
                0,
                discountRate,
                Instant.parse("2025-01-01T00:00:00Z"),
                true,
                false);
    }

    // ===== DisplayConfig Fixtures =====

    public static DisplayConfig displayConfig(OrderType orderType) {
        return new DisplayConfig(ListType.TWO_STEP, orderType, BadgeType.NONE, false);
    }

    public static DisplayConfig defaultDisplayConfig() {
        return displayConfig(OrderType.NONE);
    }

    // ===== DisplayComponent Fixtures (Non-TAB) =====

    public static DisplayComponent productComponent(
            long componentId, OrderType orderType, int pageSize) {
        DisplayConfig config = displayConfig(orderType);
        ComponentSpec spec =
                new ComponentSpec.ProductSpec(componentId * 100, pageSize, List.of(), List.of());
        DisplayPeriod period = alwaysPeriod();
        return DisplayComponent.reconstitute(
                DisplayComponentId.of(componentId),
                1L,
                "상품 컴포넌트-" + componentId,
                1,
                ComponentType.PRODUCT,
                config,
                period,
                true,
                null,
                spec,
                DeletionStatus.active(),
                Instant.now(),
                Instant.now());
    }

    public static DisplayComponent tabComponent(
            long componentId, OrderType orderType, List<DisplayTab> tabs) {
        DisplayConfig config = displayConfig(orderType);
        ComponentSpec spec =
                new ComponentSpec.TabSpec(componentId * 100, 20, false, TabMovingType.SCROLL, tabs);
        DisplayPeriod period = alwaysPeriod();
        return DisplayComponent.reconstitute(
                DisplayComponentId.of(componentId),
                1L,
                "탭 컴포넌트-" + componentId,
                2,
                ComponentType.TAB,
                config,
                period,
                true,
                null,
                spec,
                DeletionStatus.active(),
                Instant.now(),
                Instant.now());
    }

    public static DisplayComponent textComponent(long componentId) {
        DisplayConfig config = defaultDisplayConfig();
        ComponentSpec spec = new ComponentSpec.TextSpec(componentId * 100, "텍스트 내용");
        DisplayPeriod period = alwaysPeriod();
        return DisplayComponent.reconstitute(
                DisplayComponentId.of(componentId),
                1L,
                "텍스트 컴포넌트-" + componentId,
                3,
                ComponentType.TEXT,
                config,
                period,
                true,
                null,
                spec,
                DeletionStatus.active(),
                Instant.now(),
                Instant.now());
    }

    public static DisplayTab displayTab(Long tabId, String tabName, int fixedCount, int autoCount) {
        List<ProductSlot> fixed = new java.util.ArrayList<>();
        for (int i = 0; i < fixedCount; i++) {
            fixed.add(new ProductSlot(tabId * 100 + i, i + 1));
        }
        List<ProductSlot> auto = new java.util.ArrayList<>();
        for (int i = 0; i < autoCount; i++) {
            auto.add(new ProductSlot(tabId * 200 + i, i + 1));
        }
        return new DisplayTab(tabId, tabName, 1, fixed, auto);
    }

    // ===== ProductComponentGroup Fixtures =====

    public static ProductComponentGroup nonTabOnly(List<DisplayComponent> nonTabComponents) {
        return new ProductComponentGroup(nonTabComponents, List.of());
    }

    public static ProductComponentGroup tabOnly(List<DisplayComponent> tabComponents) {
        return new ProductComponentGroup(List.of(), tabComponents);
    }

    public static ProductComponentGroup emptyGroup() {
        return new ProductComponentGroup(List.of(), List.of());
    }
}
