package com.ryuqq.setof.application.contentpage.manager;

import com.ryuqq.setof.application.category.port.out.CategoryQueryPort;
import com.ryuqq.setof.application.contentpage.port.out.ComponentAutoProductQueryPort;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.contentpage.vo.AutoProductCriteria;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * AutoProductReadManager - AUTO 상품 조회 매니저.
 *
 * <p>카테고리 ID를 하위 카테고리까지 확장한 후 ComponentAutoProductQueryPort로 상품을 조회한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class AutoProductReadManager {

    private final ComponentAutoProductQueryPort queryPort;
    private final CategoryQueryPort categoryQueryPort;

    public AutoProductReadManager(
            ComponentAutoProductQueryPort queryPort, CategoryQueryPort categoryQueryPort) {
        this.queryPort = queryPort;
        this.categoryQueryPort = categoryQueryPort;
    }

    @Transactional(readOnly = true)
    public List<ProductThumbnailSnapshot> fetchAutoProducts(AutoProductCriteria criteria) {
        AutoProductCriteria expanded = expandCategories(criteria);
        return queryPort.fetchAutoProducts(expanded);
    }

    private AutoProductCriteria expandCategories(AutoProductCriteria criteria) {
        if (!criteria.hasCategoryFilter()) {
            return criteria;
        }

        List<Long> originalIds = criteria.categoryIds();
        if (originalIds.size() != 1) {
            return criteria;
        }

        long categoryId = originalIds.getFirst();
        List<Long> descendantIds = categoryQueryPort.findDescendantIds(CategoryId.of(categoryId));

        if (descendantIds.isEmpty() || descendantIds.equals(originalIds)) {
            return criteria;
        }

        return criteria.withExpandedCategories(descendantIds);
    }
}
