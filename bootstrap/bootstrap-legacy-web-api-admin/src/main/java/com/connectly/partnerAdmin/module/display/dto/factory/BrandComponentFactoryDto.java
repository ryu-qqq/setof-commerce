package com.connectly.partnerAdmin.module.display.dto.factory;


import com.connectly.partnerAdmin.module.brand.core.BaseBrandContext;
import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.component.brand.BrandComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.brand.BrandComponentQueryDto;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BrandComponentFactoryDto implements ComponentFactoryMakeDto{

    private ComponentQueryDto componentQueryDto;
    private List<BrandComponentQueryDto> queries;
    private List<SortItem> sortItems;



    @Override
    public BrandComponentDetail toComponentDetail() {
        return BrandComponentDetail.builder()
                .componentId(componentQueryDto.getComponentId())
                .brandComponentId(queries.getFirst().getBrandComponentId())
                .brandList(getBrands())
                .categoryId(queries.getFirst().getCategoryId())
                .sortItems(sortItems)
                .componentDetails(componentQueryDto.getComponentDetails())
                .exposedProducts(componentQueryDto.getExposedProducts())
                .viewExtensionId(componentQueryDto.getViewExtensionId())
                .viewExtensionDetails(componentQueryDto.getViewExtensionDetails())
                .displayYn(componentQueryDto.getDisplayYn())
                .displayPeriod(componentQueryDto.getDisplayPeriod())
                .componentName(componentQueryDto.getComponentName())
                .displayOrder(componentQueryDto.getDisplayOrder())
                .build();
    }

    @Override
    public long getContentId() {
        return componentQueryDto.getContentId();
    }

    @Override
    public long getComponentId() {
        return componentQueryDto.getComponentId();
    }

    @Override
    public long getSubComponentId() {
        if(!queries.isEmpty()) return queries.getFirst().getComponentId();
        else return 0L;
    }

    @Override
    public Long getTargetId() {
        if(!queries.isEmpty()) return queries.getFirst().getBrandComponentId();
        else return 0L;
    }

    @Override
    public List<Long> getTargetIds() {
        return queries.stream()
                .map(BrandComponentQueryDto::getBrand)
                .map(BaseBrandContext::getBrandId)
                .collect(Collectors.toList());
    }

    public List<BaseBrandContext> getBrands(){
        return queries.stream()
                .map(BrandComponentQueryDto::getBrand)
                .collect(Collectors.toList());
    }

}
