package com.setof.connectly.module.display.entity.embedded;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.enums.component.BadgeType;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.display.enums.component.ListType;
import com.setof.connectly.module.display.enums.component.OrderType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class ComponentDetails {
    @Enumerated(EnumType.STRING)
    private ComponentType componentType;

    @Enumerated(EnumType.STRING)
    private ListType listType;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    private BadgeType badgeType;

    @Enumerated(EnumType.STRING)
    private Yn filterYn;

    public ComponentDetails(
            ComponentType componentType,
            ListType listType,
            OrderType orderType,
            BadgeType badgeType,
            Yn filterYn) {
        this.componentType = componentType;
        this.listType = listType;
        this.orderType = orderType;
        this.badgeType = badgeType;
        this.filterYn = filterYn;
    }
}
