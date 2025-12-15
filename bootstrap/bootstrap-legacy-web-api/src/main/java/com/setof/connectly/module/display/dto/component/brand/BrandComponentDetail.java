package com.setof.connectly.module.display.dto.component.brand;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.setof.connectly.module.display.dto.component.AbstractCreateComponent;
import com.setof.connectly.module.display.entity.embedded.ViewExtensionDetails;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.dto.brand.BrandDto;
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
@JsonTypeName("brandComponent")
public class BrandComponentDetail extends AbstractCreateComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long brandComponentId;

    private List<BrandDto> brandList;
    private Long categoryId;

    private int exposedProducts;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long viewExtensionId;

    @JsonIgnore private ViewExtensionDetails viewExtensionDetails;

    private List<ProductGroupThumbnail> productGroupThumbnails;

    @Override
    public Long getSubComponentId() {
        return brandComponentId;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BRAND;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BrandComponentDetail)) return false;
        if (!super.equals(o)) return false;

        BrandComponentDetail that = (BrandComponentDetail) o;

        if (!Objects.equals(brandList, that.brandList)) return false;
        if (!Objects.equals(categoryId, that.categoryId)) return false;
        if (exposedProducts != that.exposedProducts) return false;
        if (!Objects.equals(getComponentDetails(), that.getComponentDetails())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(), brandList, categoryId, getComponentDetails(), exposedProducts);
    }

    public void setBrandList(List<BrandDto> brandList) {
        this.brandList = brandList;
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
