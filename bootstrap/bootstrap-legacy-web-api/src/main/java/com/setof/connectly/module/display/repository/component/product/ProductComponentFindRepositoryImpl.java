package com.setof.connectly.module.display.repository.component.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.display.repository.component.AbstractComponentRepository;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public class ProductComponentFindRepositoryImpl extends AbstractComponentRepository
        implements ProductComponentFindRepository {

    public ProductComponentFindRepositoryImpl(JPAQueryFactory queryFactory) {
        super(queryFactory);
    }

    @Override
    public List<ProductGroupThumbnail> fetchProductComponentsWhenLesserThanExposedSize(
            long componentId, Set<Long> productIds, ComponentFilter filter, int pageSize) {
        return super.fetchProductComponentsWhenLesserThanExposedSize(
                componentId, productIds, filter, pageSize);
    }
}
