package com.setof.connectly.module.display.dto.content;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.viewExtension.ViewExtensionDetailDto;
import com.setof.connectly.module.display.enums.component.BadgeType;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.display.enums.component.ListType;
import com.setof.connectly.module.display.enums.component.OrderType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ComponentDetail {
    private long componentId;
    private String componentName;
    private ComponentType componentType;
    private ListType listType;
    private OrderType orderType;
    private BadgeType badgeType;
    private Yn filterYn;
    private int exposedProducts;
    private SubComponent component;
    private ViewExtensionDetailDto viewExtensionDetails;

    public ComponentDetail(
            long componentId,
            String componentName,
            ComponentType componentType,
            ListType listType,
            OrderType orderType,
            BadgeType badgeType,
            Yn filterYn,
            int exposedProducts,
            SubComponent component,
            ViewExtensionDetailDto viewExtensionDetails) {
        this.componentId = componentId;
        this.componentName = componentName;
        this.componentType = componentType;
        this.listType = listType;
        this.orderType = orderType;
        this.badgeType = badgeType;
        this.filterYn = filterYn;
        this.exposedProducts = exposedProducts;
        this.component = component;
        this.viewExtensionDetails = viewExtensionDetails;
    }
}
