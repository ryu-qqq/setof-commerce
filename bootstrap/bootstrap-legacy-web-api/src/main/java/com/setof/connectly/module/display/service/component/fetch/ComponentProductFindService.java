package com.setof.connectly.module.display.service.component.fetch;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.display.dto.query.ComponentItemQueryDto;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface ComponentProductFindService {

    CustomSlice<ProductGroupThumbnail> fetchComponentProductGroups(
            long componentId,
            Set<ComponentItemQueryDto> componentItemQuerySet,
            ComponentFilter filter,
            Pageable pageable);
}
