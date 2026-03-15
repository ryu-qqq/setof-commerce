package com.ryuqq.setof.adapter.out.persistence.composite.content.condition;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.QComponentFixedProductJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.QProductGroupJpaEntity;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ContentCompositeConditionBuilder - 콘텐츠 Composite QueryDSL 조건 빌더.
 *
 * <p>FIXED/AUTO 상품 조회 시 사용하는 동적 조건.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ContentCompositeConditionBuilder {

    private static final QComponentFixedProductJpaEntity cfp =
            QComponentFixedProductJpaEntity.componentFixedProductJpaEntity;
    private static final QProductGroupJpaEntity pg = QProductGroupJpaEntity.productGroupJpaEntity;

    public BooleanExpression componentIdIn(List<Long> componentIds) {
        return componentIds != null && !componentIds.isEmpty()
                ? cfp.componentId.in(componentIds)
                : null;
    }

    public BooleanExpression fixedProductNotDeleted() {
        return cfp.deletedAt.isNull();
    }

    public BooleanExpression categoryIdIn(List<Long> categoryIds) {
        return categoryIds != null && !categoryIds.isEmpty() ? pg.categoryId.in(categoryIds) : null;
    }

    public BooleanExpression brandIdIn(List<Long> brandIds) {
        return brandIds != null && !brandIds.isEmpty() ? pg.brandId.in(brandIds) : null;
    }

    public BooleanExpression productGroupStatusNotDeleted() {
        return pg.status.ne("DELETED");
    }
}
