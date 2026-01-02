package com.connectly.partnerAdmin.module.display.mapper;

import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.ViewExtension;

@org.springframework.stereotype.Component
public class ComponentQueryMapperImpl implements ComponentQueryMapper{



    @Override
    public Component toEntity(long contentId, ViewExtension viewExtension, SubComponent subCreateComponent) {
        return Component.builder()
                .contentId(contentId)
                .componentName(subCreateComponent.getComponentName())
                .displayOrder(subCreateComponent.getDisplayOrder())
                .displayPeriod(subCreateComponent.getDisplayPeriod())
                .displayYn(subCreateComponent.getDisplayYn())
                .componentDetails(subCreateComponent.getComponentDetails())
                .exposedProducts(subCreateComponent.getExposedProducts())
                .viewExtension(viewExtension)
                .build();
    }
}
