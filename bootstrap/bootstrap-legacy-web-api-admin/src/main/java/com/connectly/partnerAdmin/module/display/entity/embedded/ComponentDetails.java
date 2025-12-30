package com.connectly.partnerAdmin.module.display.entity.embedded;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.enums.BadgeType;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.enums.ListType;
import com.connectly.partnerAdmin.module.display.enums.OrderType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Objects;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Embeddable
public class ComponentDetails {

    @Column(name = "COMPONENT_TYPE")
    @Enumerated(EnumType.STRING)
    private ComponentType componentType;

    @Column(name = "LIST_TYPE")
    @Enumerated(EnumType.STRING)
    private ListType listType;

    @Column(name = "ORDER_TYPE")
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(name = "BADGE_TYPE")
    @Enumerated(EnumType.STRING)
    private BadgeType badgeType;

    @Column(name = "FILTER_YN")
    @Enumerated(EnumType.STRING)
    private Yn filterYn;


    public ComponentDetails(ComponentDetails other) {
        this.componentType = other.componentType;
        this.listType = other.listType;
        this.orderType = other.orderType;
        this.badgeType = other.badgeType;
        this.filterYn = other.filterYn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentDetails that = (ComponentDetails) o;
        return componentType == that.componentType &&
                listType == that.listType &&
                orderType == that.orderType &&
                badgeType == that.badgeType &&
                filterYn == that.filterYn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentType, listType, orderType, badgeType, filterYn);
    }
}
