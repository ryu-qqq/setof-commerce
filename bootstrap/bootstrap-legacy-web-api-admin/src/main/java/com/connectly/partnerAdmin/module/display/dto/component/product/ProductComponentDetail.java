package com.connectly.partnerAdmin.module.display.dto.component.product;

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


@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("productComponentDetail")
public class ProductComponentDetail extends AbstractCreateComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long productComponentId;
    private int exposedProducts;
    private ComponentDetails componentDetails;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long viewExtensionId;
    private ViewExtensionDetails viewExtensionDetails;
    private List<SortItem> sortItems;

    @Override
    public Long getSubComponentId() {
        return productComponentId;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.PRODUCT;
    }

    @Override
    public List<DisplayProductGroupThumbnail> getProductGroupThumbnails() {
        return sortItems.stream()
                .flatMap(sortItem -> sortItem.getProductGroups().stream())
                .toList();
    }

}
