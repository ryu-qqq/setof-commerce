package com.setof.connectly.module.display.repository.component.tab;

import static com.setof.connectly.module.display.entity.component.QComponentItem.componentItem;
import static com.setof.connectly.module.display.entity.component.QComponentTarget.componentTarget;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.display.repository.component.AbstractComponentRepository;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class TabComponentFindRepositoryImpl extends AbstractComponentRepository
        implements TabComponentFindRepository {

    public TabComponentFindRepositoryImpl(JPAQueryFactory queryFactory) {
        super(queryFactory);
    }

    @Override
    public List<ProductGroupThumbnail> getTabComponentsWhenLesserThanExposedSize(
            long componentId, long tabId, Pageable pageable) {

        List<Long> componentItemIds =
                queryFactory
                        .select(componentItem.id)
                        .from(componentTarget)
                        .innerJoin(componentItem)
                        .on(componentItem.componentTarget.id.eq(componentTarget.id))
                        .where(tabIdEq(tabId))
                        .orderBy(componentItem.id.asc())
                        .limit(pageable.getPageSize())
                        .fetch();

        return getProductGroupThumbnail(componentItemIds);
    }

    protected BooleanExpression tabIdEq(long tabId) {
        return componentTarget.tabId.eq(tabId);
    }
}
