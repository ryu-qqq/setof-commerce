package com.setof.connectly.module.display.dto.component.tab;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.setof.connectly.module.display.dto.component.AbstractCreateComponent;
import com.setof.connectly.module.display.entity.embedded.ViewExtensionDetails;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("tabComponent")
public class TabComponentDetail extends AbstractCreateComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long tabComponentId;

    private List<TabDetail> tabDetails;
    private int exposedProducts;

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
    public List<ProductGroupThumbnail> getProductGroupThumbnails() {
        return tabDetails.stream()
                .map(TabDetail::getProductGroupThumbnails)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabComponentDetail)) return false;
        if (!super.equals(o)) return false; // 상위 클래스의 equals 호출

        TabComponentDetail that = (TabComponentDetail) o;

        if (!Objects.equals(tabDetails, that.tabDetails)) return false;
        if (exposedProducts != that.exposedProducts) return false;
        if (!Objects.equals(getComponentDetails(), that.getComponentDetails())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tabDetails, getComponentDetails(), exposedProducts);
    }

    @Override
    public boolean isProductRelatedComponent() {
        return true;
    }
}
