package com.ryuqq.setof.domain.contentpage.vo;

import java.util.List;
import java.util.Map;

/**
 * ComponentProductBundle - 디스플레이 컴포넌트별 상품 썸네일 번들.
 *
 * <p>PRODUCT/BRAND/CATEGORY 컴포넌트는 componentProducts로 조회하고, TAB 컴포넌트는 tabProducts로 탭별 상품 목록을 조회한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ComponentProductBundle(
        Map<Long, List<ProductThumbnailSnapshot>> componentProducts,
        Map<Long, List<ProductThumbnailSnapshot>> tabProducts) {

    public static ComponentProductBundle empty() {
        return new ComponentProductBundle(Map.of(), Map.of());
    }

    public List<ProductThumbnailSnapshot> forComponent(long componentId) {
        return componentProducts.getOrDefault(componentId, List.of());
    }

    public List<ProductThumbnailSnapshot> forTab(long tabId) {
        return tabProducts.getOrDefault(tabId, List.of());
    }
}
