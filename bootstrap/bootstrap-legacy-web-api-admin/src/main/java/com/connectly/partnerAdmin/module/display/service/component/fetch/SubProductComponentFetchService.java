package com.connectly.partnerAdmin.module.display.service.component.fetch;


import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentItemQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQuery;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;

import java.util.List;
import java.util.Map;


public interface SubProductComponentFetchService<T extends ComponentQuery> extends SubComponentFetchService {

    default Map<Integer, SubComponent> fetchComponents(List<T> productRelatedQueries, List<ComponentItemQueryDto> componentItemQueries, List<ComponentQueryDto> componentQueries) {
        throw new UnsupportedOperationException();
    }

    default Map<Integer, SubComponent> fetchComponents(List<T> productRelatedQueries, List<ComponentQueryDto> componentQueries) {
        throw new UnsupportedOperationException();
    }


}
