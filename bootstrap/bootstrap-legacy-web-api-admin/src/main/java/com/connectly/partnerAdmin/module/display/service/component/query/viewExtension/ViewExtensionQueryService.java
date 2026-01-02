package com.connectly.partnerAdmin.module.display.service.component.query.viewExtension;

import com.connectly.partnerAdmin.module.display.entity.component.ViewExtension;
import com.connectly.partnerAdmin.module.display.entity.embedded.ViewExtensionDetails;

import java.util.List;

public interface ViewExtensionQueryService {

    ViewExtension saveViewExtension(ViewExtensionDetails viewExtensionDetails);

    void deleteAll(List<Long> viewExtensionIds);

    void updateAll(List<ViewExtension> viewExtensions);
}
