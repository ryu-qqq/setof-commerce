package com.setof.connectly.module.display.dto.component.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.setof.connectly.module.display.dto.component.AbstractCreateComponent;
import com.setof.connectly.module.display.entity.embedded.ViewExtensionDetails;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("categoryComponent")
public class CategoryComponentDetail extends AbstractCreateComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long categoryComponentId;

    private long categoryId;
    private int exposedProducts;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long viewExtensionId;

    private ViewExtensionDetails viewExtensionDetails;
    private List<ProductGroupThumbnail> productGroupThumbnails;

    @Override
    public Long getSubComponentId() {
        return categoryComponentId;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.CATEGORY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryComponentDetail)) return false;
        if (!super.equals(o)) return false; // 상위 클래스의 equals 호출

        CategoryComponentDetail that = (CategoryComponentDetail) o;

        if (categoryId != that.categoryId) return false;
        if (exposedProducts != that.exposedProducts) return false;
        if (!Objects.equals(getComponentDetails(), that.getComponentDetails())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), categoryId, getComponentDetails(), exposedProducts);
    }

    @Override
    public boolean isProductRelatedComponent() {
        return true;
    }

    public int getPageSize() {
        if (!viewExtensionDetails.getViewExtensionType().isProductExtension())
            return exposedProducts;
        int maxClickCount = viewExtensionDetails.getMaxClickCount();
        int productCountPerClick = viewExtensionDetails.getProductCountPerClick();
        return exposedProducts + maxClickCount + productCountPerClick;
    }
}
