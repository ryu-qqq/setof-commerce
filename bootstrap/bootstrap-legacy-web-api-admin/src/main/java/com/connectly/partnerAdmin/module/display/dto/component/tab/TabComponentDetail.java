package com.connectly.partnerAdmin.module.display.dto.component.tab;

import com.connectly.partnerAdmin.module.display.dto.component.AbstractCreateComponent;
import com.connectly.partnerAdmin.module.display.dto.component.DisplayProductGroupThumbnail;
import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.entity.embedded.ComponentDetails;
import com.connectly.partnerAdmin.module.display.entity.embedded.ViewExtensionDetails;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("tabComponentDetail")
public class TabComponentDetail extends AbstractCreateComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long tabComponentId;
    private List<TabDetail> tabDetails;
    private int exposedProducts;

    private ComponentDetails componentDetails;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long viewExtensionId;

    private ViewExtensionDetails viewExtensionDetails;

    @Override
    public Long getSubComponentId() {
        return tabComponentId;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TAB;
    }

    @Override
    public List<SortItem> getSortItems() {
        return null;
    }

    @Override
    public List<DisplayProductGroupThumbnail> getProductGroupThumbnails() {
        return tabDetails.stream()
                .flatMap(tabDetail -> tabDetail.getSortItems().stream())
                .flatMap(sortItem -> sortItem.getProductGroups().stream())
                .toList();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabComponentDetail that)) return false;
        if (!super.equals(o)) return false; // 상위 클래스의 equals 호출

        if (!Objects.equals(tabDetails, that.tabDetails)) return false;
        if (exposedProducts != that.exposedProducts) return false;
        return Objects.equals(componentDetails, that.componentDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tabDetails, componentDetails, exposedProducts);
    }

}
