package com.connectly.partnerAdmin.module.display.mapper.viewExtension;


import com.connectly.partnerAdmin.module.display.entity.component.ViewExtension;
import com.connectly.partnerAdmin.module.display.entity.embedded.ViewExtensionDetails;
import org.springframework.stereotype.Component;

@Component
public class ViewExtensionMapperImpl implements ViewExtensionMapper{


    @Override
    public ViewExtension toEntity(ViewExtensionDetails viewExtensionDetails) {
        return ViewExtension.builder()
                .viewExtensionDetails(viewExtensionDetails)
                .build();
    }
}
