package com.setof.commerce.domain.contentpage;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
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
import java.time.LocalDateTime;
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
                10000,
                8000,
                8000,
                20,
                2000,
                20,
                LocalDateTime.of(2025, 1, 1, 0, 0),
                4.5,
                100L,
                0.85,
                false,
                "Y",
                "N");
    }

    /** 커스텀 가격/할인율/평점/리뷰 수를 가진 ProductThumbnailSnapshot 생성. */
    public static ProductThumbnailSnapshot snapshotWithDetails(
            long productGroupId,
            int regularPrice,
            int currentPrice,
            int discountRate,
            double averageRating,
            long reviewCount,
            double score) {
        return new ProductThumbnailSnapshot(
                productGroupId,
                1L,
                "상품명-" + productGroupId,
                10L,
                "브랜드A",
                "https://example.com/image/" + productGroupId + ".jpg",
                regularPrice,
                currentPrice,
                currentPrice,
                discountRate,
                regularPrice - currentPrice,
                discountRate,
                LocalDateTime.of(2025, 1, 1, 0, 0),
                averageRating,
                reviewCount,
                score,
                false,
                "Y",
                "N");
    }

    // ===== DisplayConfig Fixtures =====

    public static DisplayConfig displayConfig(OrderType orderType) {
        return new DisplayConfig(ListType.TWO_STEP, orderType, BadgeType.NONE, false);
    }

    public static DisplayConfig defaultDisplayConfig() {
        return displayConfig(OrderType.NONE);
    }

    // ===== DisplayComponent Fixtures (Non-TAB) =====

    /**
     * PRODUCT 타입 DisplayComponent 생성.
     *
     * @param componentId 컴포넌트 ID
     * @param orderType 정렬 타입
     * @param pageSize 노출 상품 수 (0이면 제한 없음)
     * @return DisplayComponent
     */
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

    /**
     * TAB 타입 DisplayComponent 생성.
     *
     * @param componentId 컴포넌트 ID
     * @param orderType 정렬 타입
     * @param tabs 탭 목록
     * @return DisplayComponent
     */
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

    /** TEXT 타입(상품 비관련) DisplayComponent 생성. */
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

    /**
     * DisplayTab 생성.
     *
     * @param tabId 탭 ID
     * @param tabName 탭 이름
     * @param fixedCount 고정 상품 수
     * @param autoCount 자동 상품 수
     * @return DisplayTab
     */
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

    /** Non-TAB 컴포넌트만 있는 ProductComponentGroup. */
    public static ProductComponentGroup nonTabOnly(List<DisplayComponent> nonTabComponents) {
        return new ProductComponentGroup(nonTabComponents, List.of());
    }

    /** TAB 컴포넌트만 있는 ProductComponentGroup. */
    public static ProductComponentGroup tabOnly(List<DisplayComponent> tabComponents) {
        return new ProductComponentGroup(List.of(), tabComponents);
    }

    /** 빈 ProductComponentGroup. */
    public static ProductComponentGroup emptyGroup() {
        return new ProductComponentGroup(List.of(), List.of());
    }
}
