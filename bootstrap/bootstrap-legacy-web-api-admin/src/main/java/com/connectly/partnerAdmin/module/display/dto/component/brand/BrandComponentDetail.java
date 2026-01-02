package com.connectly.partnerAdmin.module.display.dto.component.brand;

import com.connectly.partnerAdmin.module.brand.core.BaseBrandContext;
import com.connectly.partnerAdmin.module.display.dto.component.AbstractCreateComponent;
import com.connectly.partnerAdmin.module.display.dto.component.DisplayProductGroupThumbnail;
import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.entity.embedded.ComponentDetails;
import com.connectly.partnerAdmin.module.display.entity.embedded.ViewExtensionDetails;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("brandComponentDetail")
public class BrandComponentDetail extends AbstractCreateComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long brandComponentId;
    @Setter
    private List<BaseBrandContext> brandList;
    private Long categoryId;
    private ComponentDetails componentDetails;
    private int exposedProducts;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long viewExtensionId;
    private ViewExtensionDetails viewExtensionDetails;
    private List<SortItem> sortItems;


    public BrandComponentDetail(BrandComponentDetail other) {
        this.brandComponentId = other.brandComponentId;
        this.brandList = new ArrayList<>(other.brandList);
        this.categoryId = other.categoryId;
        this.componentDetails = new ComponentDetails(other.componentDetails);
        this.exposedProducts = other.exposedProducts;
        this.viewExtensionId = other.viewExtensionId;
        this.viewExtensionDetails = new ViewExtensionDetails(other.viewExtensionDetails);
        this.sortItems = new ArrayList<>(other.sortItems);
    }


    @Override
    public Long getSubComponentId() {
        return brandComponentId;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BRAND;
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
        if (!(o instanceof BrandComponentDetail that)) return false;
        if (!super.equals(o)) return false;

        if (!Objects.equals(brandList, that.brandList)) return false;
        if (!Objects.equals(categoryId, that.categoryId)) return false;
        if (exposedProducts != that.exposedProducts) return false;
        return Objects.equals(componentDetails, that.componentDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), brandList, categoryId, componentDetails, exposedProducts);
    }

    @JsonIgnore
    public Map<Long, BaseBrandContext> getBrands(){
        return brandList.stream()
                .collect(Collectors.toMap(BaseBrandContext::getBrandId, Function.identity()));
    }

    @JsonIgnore
    public List<Long> getBrandIdList(){
        if(brandList == null || brandList.isEmpty()) return new ArrayList<>();
        return brandList.stream()
                .filter(Objects::nonNull)
                .map(BaseBrandContext::getBrandId)
                .collect(Collectors.toList());
    }


}
