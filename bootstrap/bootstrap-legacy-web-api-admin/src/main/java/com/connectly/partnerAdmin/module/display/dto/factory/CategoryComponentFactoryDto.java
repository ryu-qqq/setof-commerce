package com.connectly.partnerAdmin.module.display.dto.factory;


import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.component.category.CategoryComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.category.CategoryComponentQueryDto;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CategoryComponentFactoryDto implements ComponentFactoryMakeDto{

    private ComponentQueryDto componentQueryDto;
    private List<CategoryComponentQueryDto> queries;
    private List<SortItem> sortItems;





    @Override
    public CategoryComponentDetail toComponentDetail() {
        return CategoryComponentDetail.builder()
                .componentId(componentQueryDto.getComponentId())
                .categoryComponentId(queries.getFirst().getCategoryComponentId())
                .categoryId(getTargetId())
                .componentDetails(componentQueryDto.getComponentDetails())
                .exposedProducts(componentQueryDto.getExposedProducts())
                .viewExtensionId(componentQueryDto.getViewExtensionId())
                .viewExtensionDetails(componentQueryDto.getViewExtensionDetails())
                .sortItems(sortItems)
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
        if(!queries.isEmpty()) return queries.getFirst().getCategoryComponentId();
        else return 0L;
    }

    @Override
    public Long getTargetId() {
        if(!queries.isEmpty()) return queries.getFirst().getCategoryId();
        else return 0L;
    }


    @Override
    public List<Long> getTargetIds() {
        return queries.stream().map(CategoryComponentQueryDto::getCategoryId).collect(Collectors.toList());
    }


}
