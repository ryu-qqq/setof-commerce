package com.setof.connectly.module.display.repository.component.brand;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.display.repository.component.AbstractComponentRepository;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public class BrandComponentFindRepositoryImpl extends AbstractComponentRepository
        implements BrandComponentFindRepository {

    public BrandComponentFindRepositoryImpl(JPAQueryFactory queryFactory) {
        super(queryFactory);
    }

    @Override
    public List<ProductGroupThumbnail> fetchBrandComponentsWhenLesserThanExposedSize(
            long componentId, Set<Long> productIds, ComponentFilter filter, int pageSize) {
        return super.fetchProductComponentsWhenLesserThanExposedSize(
                componentId, productIds, filter, pageSize);
    }
}
