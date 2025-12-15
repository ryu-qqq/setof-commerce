package com.setof.connectly.module.display.dto.component.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.setof.connectly.module.display.dto.component.AbstractCreateComponent;
import com.setof.connectly.module.display.entity.embedded.ViewExtensionDetails;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("productComponent")
public class ProductComponentDetail extends AbstractCreateComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long productComponentId;

    private int exposedProducts;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long viewExtensionId;

    private ViewExtensionDetails viewExtensionDetails;
    private List<ProductGroupThumbnail> productGroupThumbnails;

    @Override
    public Long getSubComponentId() {
        return productComponentId;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.PRODUCT;
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
