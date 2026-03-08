package com.ryuqq.setof.domain.displaycomponent.entity;

import com.ryuqq.setof.domain.displaycomponent.id.ProductCurationId;
import com.ryuqq.setof.domain.displaycomponent.vo.CurationSortType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ProductCuration - 상품 큐레이션 Entity (DisplayComponent 소속).
 *
 * <p>컴포넌트 내 상품 편성을 관리한다. Tab 컴포넌트에서는 tabId로 탭별 큐레이션을 구분한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ProductCuration {

    private final ProductCurationId id;
    private CurationSortType curationSortType;
    private final Long tabId;
    private final List<CuratedProduct> items;

    private ProductCuration(
            ProductCurationId id,
            CurationSortType curationSortType,
            Long tabId,
            List<CuratedProduct> items) {
        this.id = id;
        this.curationSortType = curationSortType;
        this.tabId = tabId;
        this.items = new ArrayList<>(items);
    }

    public static ProductCuration forNew(
            CurationSortType curationSortType, Long tabId, List<CuratedProduct> items) {
        return new ProductCuration(ProductCurationId.forNew(), curationSortType, tabId, items);
    }

    public static ProductCuration reconstitute(
            ProductCurationId id,
            CurationSortType curationSortType,
            Long tabId,
            List<CuratedProduct> items) {
        return new ProductCuration(id, curationSortType, tabId, items);
    }

    public void changeSortType(CurationSortType curationSortType) {
        this.curationSortType = curationSortType;
    }

    public void replaceItems(List<CuratedProduct> newItems) {
        this.items.clear();
        this.items.addAll(newItems);
    }

    public void addItem(CuratedProduct item) {
        this.items.add(item);
    }

    public ProductCurationId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public CurationSortType curationSortType() {
        return curationSortType;
    }

    public Long tabId() {
        return tabId;
    }

    public List<CuratedProduct> items() {
        return Collections.unmodifiableList(items);
    }
}
