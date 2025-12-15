package com.setof.connectly.module.display.service.component;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.display.dto.component.SortItem;
import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.display.dto.query.ComponentItemQueryDto;
import com.setof.connectly.module.display.dto.query.ComponentQuery;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface SubProductComponentFindService<T extends ComponentQuery>
        extends SubComponentFindService {

    default Map<Integer, SubComponent> fetchComponents(
            List<T> productRelatedQueries,
            List<ComponentItemQueryDto> componentItemQueries,
            List<ComponentQueryDto> componentQueries) {
        throw new UnsupportedOperationException();
    }

    default Map<Integer, SubComponent> fetchComponents(
            List<T> productRelatedQueries, List<ComponentQueryDto> componentQueries) {
        throw new UnsupportedOperationException();
    }

    default CustomSlice<ProductGroupThumbnail> fetchComponentProductGroups(
            long componentId,
            Set<ComponentItemQueryDto> componentItemQuerySet,
            ComponentFilter filter,
            Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    <R extends ComponentFilter> List<ProductGroupThumbnail> processSortItemsWithLastDomainId(
            long componentId, List<SortItem> sortItems, R filterDto, Pageable pageable);
}
