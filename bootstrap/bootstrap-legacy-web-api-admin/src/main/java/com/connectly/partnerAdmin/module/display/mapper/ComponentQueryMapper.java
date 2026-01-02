package com.connectly.partnerAdmin.module.display.mapper;

import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.ViewExtension;


public interface ComponentQueryMapper {

    Component toEntity(long contentId, ViewExtension viewExtension, SubComponent subCreateComponent);
}
