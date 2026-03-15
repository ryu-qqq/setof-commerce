package com.ryuqq.setof.application.contentpage;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.id.DisplayComponentId;
import com.ryuqq.setof.domain.contentpage.vo.AutoProductCriteria;
import com.ryuqq.setof.domain.contentpage.vo.BadgeType;
import com.ryuqq.setof.domain.contentpage.vo.ComponentSpec;
import com.ryuqq.setof.domain.contentpage.vo.ComponentType;
import com.ryuqq.setof.domain.contentpage.vo.DisplayConfig;
import com.ryuqq.setof.domain.contentpage.vo.DisplayTab;
import com.ryuqq.setof.domain.contentpage.vo.ListType;
import com.ryuqq.setof.domain.contentpage.vo.OrderType;
import com.ryuqq.setof.domain.contentpage.vo.ProductSlot;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * ContentPage Product 관련 Application 테스트 Fixtures.
 *
 * <p>FixedProductReadManager, AutoProductReadManager, ComponentProductReadFacade 테스트에서 공통으로 사용하는 객체
 * 생성 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ContentPageProductFixtures {

    private static final Instant FIXED_PAST = Instant.parse("2024-01-01T00:00:00Z");
    private static final Instant FIXED_FUTURE = Instant.parse("2099-12-31T23:59:59Z");

    private ContentPageProductFixtures() {}

    // ===== ProductThumbnailSnapshot =====

    public static ProductThumbnailSnapshot productSnapshot(long productGroupId) {
        return new ProductThumbnailSnapshot(
                productGroupId,
                1L,
                "상품명_" + productGroupId,
                10L,
                "브랜드명",
                "https://example.com/image/" + productGroupId + ".jpg",
                null,
                null,
                30000,
                25000,
                5000,
                10,
                3000,
                16,
                Instant.parse("2024-01-01T00:00:00Z"),
                true,
                false);
    }

    public static List<ProductThumbnailSnapshot> productSnapshots(long... productGroupIds) {
        List<ProductThumbnailSnapshot> result = new java.util.ArrayList<>();
        for (long id : productGroupIds) {
            result.add(productSnapshot(id));
        }
        return result;
    }

    // ===== AutoProductCriteria =====

    public static AutoProductCriteria autoProductCriteriaForComponent(long componentId) {
        return AutoProductCriteria.ofComponent(componentId, 0L, List.of(), 20);
    }

    public static AutoProductCriteria autoProductCriteriaForTab(long componentId, long tabId) {
        return AutoProductCriteria.ofTab(componentId, tabId, 0L, List.of(), 20);
    }

    // ===== Map Fixtures =====

    public static Map<Long, List<ProductThumbnailSnapshot>> fixedProductMap(long componentId) {
        return Map.of(componentId, productSnapshots(101L, 102L));
    }

    public static Map<Long, List<ProductThumbnailSnapshot>> emptyProductMap() {
        return Map.of();
    }

    // ===== DisplayComponent (via reconstitute) =====

    /** PRODUCT 타입 NonTab DisplayComponent 생성. */
    public static DisplayComponent productComponent(long componentId) {
        ComponentSpec.ProductSpec spec =
                new ComponentSpec.ProductSpec(
                        componentId,
                        10,
                        List.of(new ProductSlot(101L, 1)),
                        List.of(new ProductSlot(201L, 1)));

        DisplayConfig config =
                new DisplayConfig(ListType.TWO_STEP, OrderType.RECOMMEND, BadgeType.NONE, false);

        DisplayPeriod period = DisplayPeriod.of(FIXED_PAST, FIXED_FUTURE);

        return DisplayComponent.reconstitute(
                DisplayComponentId.of(componentId),
                1L,
                "상품_컴포넌트_" + componentId,
                1,
                ComponentType.PRODUCT,
                config,
                period,
                true,
                null,
                spec,
                DeletionStatus.active(),
                FIXED_PAST,
                FIXED_PAST);
    }

    /** TAB 타입 DisplayComponent 생성. */
    public static DisplayComponent tabComponent(long componentId, long tabId) {
        DisplayTab tab =
                new DisplayTab(
                        tabId,
                        "탭_" + tabId,
                        1,
                        List.of(new ProductSlot(301L, 1)),
                        List.of(new ProductSlot(401L, 1)));

        ComponentSpec.TabSpec spec =
                new ComponentSpec.TabSpec(componentId, 10, false, null, List.of(tab));

        DisplayConfig config =
                new DisplayConfig(ListType.TWO_STEP, OrderType.RECOMMEND, BadgeType.NONE, false);

        DisplayPeriod period = DisplayPeriod.of(FIXED_PAST, FIXED_FUTURE);

        return DisplayComponent.reconstitute(
                DisplayComponentId.of(componentId),
                1L,
                "탭_컴포넌트_" + componentId,
                2,
                ComponentType.TAB,
                config,
                period,
                true,
                null,
                spec,
                DeletionStatus.active(),
                FIXED_PAST,
                FIXED_PAST);
    }

    /** TEXT 타입 (비상품) DisplayComponent 생성. */
    public static DisplayComponent textComponent(long componentId) {
        ComponentSpec.TextSpec spec = new ComponentSpec.TextSpec(componentId, "텍스트 내용");

        DisplayConfig config =
                new DisplayConfig(ListType.NONE, OrderType.NONE, BadgeType.NONE, false);

        DisplayPeriod period = DisplayPeriod.of(FIXED_PAST, FIXED_FUTURE);

        return DisplayComponent.reconstitute(
                DisplayComponentId.of(componentId),
                1L,
                "텍스트_컴포넌트_" + componentId,
                3,
                ComponentType.TEXT,
                config,
                period,
                true,
                null,
                spec,
                DeletionStatus.active(),
                FIXED_PAST,
                FIXED_PAST);
    }
}
