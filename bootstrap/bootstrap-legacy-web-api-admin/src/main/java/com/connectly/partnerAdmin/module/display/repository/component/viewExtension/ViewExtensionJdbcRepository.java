package com.connectly.partnerAdmin.module.display.repository.component.viewExtension;

import com.connectly.partnerAdmin.module.display.entity.component.ViewExtension;
import com.connectly.partnerAdmin.module.display.entity.embedded.ViewExtensionDetails;

import java.util.List;

public interface ViewExtensionJdbcRepository {
    void deleteAll(List<Long> viewExtensionIds);

    void updateAll(List<ViewExtension> viewExtensions);
}
