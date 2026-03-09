package com.ryuqq.setof.application.contentpage.manager;

import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.vo.AutoProductCriteria;
import com.ryuqq.setof.domain.contentpage.vo.ComponentProductBundle;
import com.ryuqq.setof.domain.contentpage.vo.ProductComponentGroup;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ComponentProductReadFacade - 컴포넌트 상품 조회 파사드.
 *
 * <p>FixedProductReadManager, AutoProductReadManager를 통해 데이터를 조회하고, 병합/정렬은 도메인 객체({@link
 * ProductComponentGroup})에 위임한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ComponentProductReadFacade {

    private final FixedProductReadManager fixedProductReadManager;
    private final AutoProductReadManager autoProductReadManager;

    public ComponentProductReadFacade(
            FixedProductReadManager fixedProductReadManager,
            AutoProductReadManager autoProductReadManager) {
        this.fixedProductReadManager = fixedProductReadManager;
        this.autoProductReadManager = autoProductReadManager;
    }

    @Transactional(readOnly = true)
    public ComponentProductBundle fetchComponentProducts(List<DisplayComponent> components) {
        ProductComponentGroup group = ProductComponentGroup.from(components);
        if (group.isEmpty()) {
            return ComponentProductBundle.empty();
        }

        Map<Long, List<ProductThumbnailSnapshot>> fixedByComponent = fetchFixedByComponent(group);
        Map<Long, List<ProductThumbnailSnapshot>> autoByComponent = fetchAutoByComponent(group);
        Map<Long, List<ProductThumbnailSnapshot>> componentProducts =
                group.mergeComponentProducts(fixedByComponent, autoByComponent);

        Map<Long, List<ProductThumbnailSnapshot>> fixedByTab = fetchFixedByTab(group);
        Map<Long, List<ProductThumbnailSnapshot>> autoByTab = fetchAutoByTab(group);
        Map<Long, List<ProductThumbnailSnapshot>> tabProducts =
                group.mergeTabProducts(fixedByTab, autoByTab);

        return new ComponentProductBundle(componentProducts, tabProducts);
    }

    private Map<Long, List<ProductThumbnailSnapshot>> fetchFixedByComponent(
            ProductComponentGroup group) {
        if (group.nonTabComponents().isEmpty()) {
            return Map.of();
        }
        return fixedProductReadManager.fetchFixedProducts(group.nonTabComponentIds());
    }

    private Map<Long, List<ProductThumbnailSnapshot>> fetchAutoByComponent(
            ProductComponentGroup group) {
        if (group.nonTabComponents().isEmpty()) {
            return Map.of();
        }

        Map<Long, List<ProductThumbnailSnapshot>> result = new HashMap<>();
        for (DisplayComponent comp : group.nonTabComponents()) {
            List<AutoProductCriteria> criteriaList = comp.resolveAutoProductCriteria();
            if (!criteriaList.isEmpty()) {
                result.put(
                        comp.id().value(),
                        autoProductReadManager.fetchAutoProducts(criteriaList.getFirst()));
            }
        }
        return result;
    }

    private Map<Long, List<ProductThumbnailSnapshot>> fetchFixedByTab(ProductComponentGroup group) {
        if (group.tabComponents().isEmpty()) {
            return Map.of();
        }
        return fixedProductReadManager.fetchFixedProductsByTab(group.tabComponentIds());
    }

    private Map<Long, List<ProductThumbnailSnapshot>> fetchAutoByTab(ProductComponentGroup group) {
        if (group.tabComponents().isEmpty()) {
            return Map.of();
        }

        Map<Long, List<ProductThumbnailSnapshot>> result = new HashMap<>();
        for (DisplayComponent comp : group.tabComponents()) {
            for (AutoProductCriteria criteria : comp.resolveAutoProductCriteria()) {
                result.put(criteria.tabId(), autoProductReadManager.fetchAutoProducts(criteria));
            }
        }
        return result;
    }
}
