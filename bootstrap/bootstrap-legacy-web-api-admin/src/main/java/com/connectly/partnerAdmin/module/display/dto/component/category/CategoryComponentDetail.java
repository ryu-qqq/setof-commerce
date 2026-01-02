package com.connectly.partnerAdmin.module.display.dto.component.category;

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
@JsonTypeName("categoryComponentDetail")
public class CategoryComponentDetail extends AbstractCreateComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long categoryComponentId;
    private long categoryId;
    private ComponentDetails componentDetails;
    private int exposedProducts;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long viewExtensionId;
    private ViewExtensionDetails viewExtensionDetails;
    private List<SortItem> sortItems;

    @Override
    public Long getSubComponentId() {
        return categoryComponentId;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.CATEGORY;
    }

    @Override
    public List<DisplayProductGroupThumbnail> getProductGroupThumbnails() {
        return sortItems.stream()
                .flatMap(sortItem -> sortItem.getProductGroups().stream())
                .toList();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryComponentDetail that)) return false;
        if (!super.equals(o)) return false; // 상위 클래스의 equals 호출

        if (categoryId != that.categoryId) return false;
        if (exposedProducts != that.exposedProducts) return false;
        return Objects.equals(componentDetails, that.componentDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), categoryId, componentDetails, exposedProducts);
    }

}
