package com.setof.connectly.module.display.service.component;

import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.query.ComponentQuery;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import java.util.List;
import java.util.Map;

public interface SubNonProductComponentFindService<T extends ComponentQuery>
        extends SubComponentFindService {

    default Map<Integer, SubComponent> fetchComponents(
            List<T> productRelatedQueries, List<ComponentQueryDto> componentQueries) {
        throw new UnsupportedOperationException();
    }
}
