package com.connectly.partnerAdmin.module.display.dto.component;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.dto.component.blank.BlankComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.brand.BrandComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.category.CategoryComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.image.ImageComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.product.ProductComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.tab.TabComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.text.TextComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.title.TitleComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.embedded.ComponentDetails;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BrandComponentDetail.class, name = "brandComponentDetail"),
        @JsonSubTypes.Type(value = CategoryComponentDetail.class, name = "categoryComponentDetail"),
        @JsonSubTypes.Type(value = ProductComponentDetail.class, name = "productComponentDetail"),
        @JsonSubTypes.Type(value = TabComponentDetail.class, name = "tabComponentDetail"),
        @JsonSubTypes.Type(value = ImageComponentDetail.class, name = "imageComponentDetail"),
        @JsonSubTypes.Type(value = TextComponentDetail.class, name = "textComponentDetail"),
        @JsonSubTypes.Type(value = TitleComponentDetail.class, name = "titleComponentDetail"),
        @JsonSubTypes.Type(value = BlankComponentDetail.class, name = "blankComponentDetail"),
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

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<SortItem> getSortItems();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<DisplayProductGroupThumbnail> getProductGroupThumbnails();



}
