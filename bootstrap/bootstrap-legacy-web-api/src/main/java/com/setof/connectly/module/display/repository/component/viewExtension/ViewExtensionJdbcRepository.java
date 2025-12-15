package com.setof.connectly.module.display.repository.component.viewExtension;

import com.setof.connectly.module.display.entity.component.ViewExtension;
import java.util.List;

public interface ViewExtensionJdbcRepository {
    void deleteAll(List<Long> viewExtensionIds);

    void updateAll(List<ViewExtension> viewExtensions);
}
