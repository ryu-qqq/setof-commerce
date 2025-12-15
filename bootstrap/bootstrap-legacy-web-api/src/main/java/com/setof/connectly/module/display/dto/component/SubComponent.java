package com.setof.connectly.module.display.dto.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.dto.component.blank.BlankComponentDetail;
import com.setof.connectly.module.display.dto.component.brand.BrandComponentDetail;
import com.setof.connectly.module.display.dto.component.category.CategoryComponentDetail;
import com.setof.connectly.module.display.dto.component.image.ImageComponentDetail;
import com.setof.connectly.module.display.dto.component.product.ProductComponentDetail;
import com.setof.connectly.module.display.dto.component.tab.TabComponentDetail;
import com.setof.connectly.module.display.dto.component.text.TextComponentDetail;
import com.setof.connectly.module.display.dto.component.title.TitleComponentDetail;
import com.setof.connectly.module.display.entity.embedded.ComponentDetails;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import com.setof.connectly.module.display.entity.embedded.ViewExtensionDetails;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BrandComponentDetail.class, name = "brandComponent"),
    @JsonSubTypes.Type(value = CategoryComponentDetail.class, name = "categoryComponent"),
    @JsonSubTypes.Type(value = ProductComponentDetail.class, name = "productComponent"),
    @JsonSubTypes.Type(value = TabComponentDetail.class, name = "tabComponent"),
    @JsonSubTypes.Type(value = ImageComponentDetail.class, name = "imageComponent"),
    @JsonSubTypes.Type(value = TextComponentDetail.class, name = "textComponent"),
    @JsonSubTypes.Type(value = TitleComponentDetail.class, name = "titleComponent"),
    @JsonSubTypes.Type(value = BlankComponentDetail.class, name = "blankComponent"),
})
public interface SubComponent {

    Long getComponentId();

    @JsonIgnore
    Long getSubComponentId();

    int getDisplayOrder();

    Long getViewExtensionId();

    ComponentType getComponentType();

    String getComponentName();

    DisplayPeriod getDisplayPeriod();

    Yn getDisplayYn();

    ComponentDetails getComponentDetails();

    int getExposedProducts();

    ViewExtensionDetails getViewExtensionDetails();

    @JsonIgnore
    boolean isProductRelatedComponent();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<ProductGroupThumbnail> getProductGroupThumbnails();
}
