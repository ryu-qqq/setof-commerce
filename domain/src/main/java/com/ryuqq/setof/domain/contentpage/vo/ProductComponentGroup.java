package com.ryuqq.setof.domain.contentpage.vo;

import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ProductComponentGroup - 상품 관련 디스플레이 컴포넌트 그룹.
 *
 * <p>전체 컴포넌트 목록에서 상품 컴포넌트만 필터링한 뒤, TAB/Non-TAB으로 분류하여 제공한다. FIXED/AUTO 상품 Map을 받아 병합/정렬/pageSize
 * 제한까지 도메인 내부에서 처리한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductComponentGroup(
        List<DisplayComponent> nonTabComponents, List<DisplayComponent> tabComponents) {

    public static ProductComponentGroup from(List<DisplayComponent> components) {
        List<DisplayComponent> productComponents =
                components.stream().filter(DisplayComponent::isProductComponent).toList();

        List<DisplayComponent> nonTab =
                productComponents.stream()
                        .filter(c -> c.componentType() != ComponentType.TAB)
                        .toList();
        List<DisplayComponent> tab =
                productComponents.stream()
                        .filter(c -> c.componentType() == ComponentType.TAB)
                        .toList();

        return new ProductComponentGroup(nonTab, tab);
    }

    public boolean isEmpty() {
        return nonTabComponents.isEmpty() && tabComponents.isEmpty();
    }

    public List<Long> nonTabComponentIds() {
        return nonTabComponents.stream().map(c -> c.id().value()).toList();
    }

    public List<Long> tabComponentIds() {
        return tabComponents.stream().map(c -> c.id().value()).toList();
    }

    /**
     * Non-TAB 컴포넌트별 FIXED + AUTO 상품을 병합하여 반환한다.
     *
     * @param fixedByComponent componentId → FIXED 상품 목록
     * @param autoByComponent componentId → AUTO 상품 목록
     * @return componentId → 병합된 상품 목록
     */
    public Map<Long, List<ProductThumbnailSnapshot>> mergeComponentProducts(
            Map<Long, List<ProductThumbnailSnapshot>> fixedByComponent,
            Map<Long, List<ProductThumbnailSnapshot>> autoByComponent) {
        if (nonTabComponents.isEmpty()) {
            return Map.of();
        }

        Map<Long, List<ProductThumbnailSnapshot>> result = new HashMap<>();
        for (DisplayComponent comp : nonTabComponents) {
            long componentId = comp.id().value();
            List<ProductThumbnailSnapshot> fixed =
                    fixedByComponent.getOrDefault(componentId, List.of());
            List<ProductThumbnailSnapshot> auto =
                    autoByComponent.getOrDefault(componentId, List.of());

            OrderType orderType = comp.displayConfig().orderType();
            int pageSize = comp.resolvePageSize();
            result.put(
                    componentId,
                    ProductThumbnailSnapshot.mergeFixedAndAuto(fixed, auto, orderType, pageSize));
        }
        return result;
    }

    /**
     * TAB 컴포넌트별 FIXED + AUTO 상품을 병합하여 반환한다.
     *
     * @param fixedByTab tabId → FIXED 상품 목록
     * @param autoByTab tabId → AUTO 상품 목록
     * @return tabId → 병합된 상품 목록
     */
    public Map<Long, List<ProductThumbnailSnapshot>> mergeTabProducts(
            Map<Long, List<ProductThumbnailSnapshot>> fixedByTab,
            Map<Long, List<ProductThumbnailSnapshot>> autoByTab) {
        if (tabComponents.isEmpty()) {
            return Map.of();
        }

        Map<Long, List<ProductThumbnailSnapshot>> result = new HashMap<>();
        for (DisplayComponent comp : tabComponents) {
            List<AutoProductCriteria> criteriaList = comp.resolveAutoProductCriteria();
            OrderType orderType = comp.displayConfig().orderType();

            for (AutoProductCriteria criteria : criteriaList) {
                long tabId = criteria.tabId();
                List<ProductThumbnailSnapshot> fixed = fixedByTab.getOrDefault(tabId, List.of());
                List<ProductThumbnailSnapshot> auto = autoByTab.getOrDefault(tabId, List.of());

                int pageSize = comp.resolveTabPageSize(tabId);
                result.put(
                        tabId,
                        ProductThumbnailSnapshot.mergeFixedAndAuto(
                                fixed, auto, orderType, pageSize));
            }
        }
        return result;
    }
}
