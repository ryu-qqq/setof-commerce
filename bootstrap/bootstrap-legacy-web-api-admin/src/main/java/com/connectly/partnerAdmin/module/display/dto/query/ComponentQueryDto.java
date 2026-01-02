package com.connectly.partnerAdmin.module.display.dto.query;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.entity.embedded.ComponentDetails;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.connectly.partnerAdmin.module.display.entity.embedded.ViewExtensionDetails;
import com.connectly.partnerAdmin.module.display.enums.ViewExtensionType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ComponentQueryDto {
    private long contentId;
    private long componentId;
    private String componentName;
    private int exposedProducts;
    private ComponentDetails componentDetails;
    private DisplayPeriod displayPeriod;
    private int displayOrder;
    private long viewExtensionId;
    private ViewExtensionDetails viewExtensionDetails;
    private Yn displayYn;

    @Builder
    @QueryProjection
    public ComponentQueryDto(long contentId, long componentId, String componentName, int exposedProducts, ComponentDetails componentDetails, DisplayPeriod displayPeriod, int displayOrder, long viewExtensionId, ViewExtensionDetails viewExtensionDetails, Yn displayYn) {
        this.contentId = contentId;
        this.componentId = componentId;
        this.componentName = componentName;
        this.exposedProducts = exposedProducts;
        this.componentDetails = componentDetails;
        this.displayPeriod = displayPeriod;
        this.displayOrder = displayOrder;
        this.viewExtensionId = viewExtensionId;
        this.viewExtensionDetails = viewExtensionDetails;
        this.displayYn = displayYn;
    }

    public boolean isNonProductRelatedContents(){
        return componentDetails.getComponentType().isNonProductRelatedContents();
    }

    public boolean isProductRelatedContents(){
        return componentDetails.getComponentType().isProductRelatedContents();
    }

}
