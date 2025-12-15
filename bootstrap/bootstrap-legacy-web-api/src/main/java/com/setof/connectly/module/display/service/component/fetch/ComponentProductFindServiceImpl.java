package com.setof.connectly.module.display.service.component.fetch;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.display.dto.query.ComponentItemQueryDto;
import com.setof.connectly.module.display.dto.query.ComponentQuery;
import com.setof.connectly.module.display.service.component.ProductComponentFetchStrategy;
import com.setof.connectly.module.display.service.component.SubProductComponentFindService;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComponentProductFindServiceImpl implements ComponentProductFindService {

    private final ProductComponentFetchStrategy componentFetchStrategy;

    @Override
    public CustomSlice<ProductGroupThumbnail> fetchComponentProductGroups(
            long componentId,
            Set<ComponentItemQueryDto> componentItemQuerySet,
            ComponentFilter filter,
            Pageable pageable) {
        SubProductComponentFindService<? extends ComponentQuery> subComponentFindService =
                componentFetchStrategy.get(filter.getComponentType());
        return subComponentFindService.fetchComponentProductGroups(
                componentId, componentItemQuerySet, filter, pageable);
    }
}
