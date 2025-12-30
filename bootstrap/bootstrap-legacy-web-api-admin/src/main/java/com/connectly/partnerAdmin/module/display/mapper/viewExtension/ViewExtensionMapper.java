package com.connectly.partnerAdmin.module.display.mapper.viewExtension;

import com.connectly.partnerAdmin.module.display.entity.component.ViewExtension;
import com.connectly.partnerAdmin.module.display.entity.embedded.ViewExtensionDetails;

public interface ViewExtensionMapper {

    ViewExtension toEntity(ViewExtensionDetails viewExtensionDetails);
}
