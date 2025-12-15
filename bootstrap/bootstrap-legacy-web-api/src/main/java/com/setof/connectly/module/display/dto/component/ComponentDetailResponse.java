package com.setof.connectly.module.display.dto.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.entity.embedded.ViewExtensionDetails;
import com.setof.connectly.module.display.enums.component.BadgeType;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.display.enums.component.ListType;
import com.setof.connectly.module.display.enums.component.OrderType;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ComponentDetailResponse {

    private long componentId;
    private String componentName;
    private ComponentType componentType;
    private ListType listType;
    private OrderType orderType;
    private BadgeType badgeType;
    private Yn filterYn;
    private Yn displayYn;
    private SubComponent component;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ViewExtensionDetails viewExtensionDetails;
}
