package com.connectly.partnerAdmin.module.display.dto.factory;

import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.component.product.ProductComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.product.ProductComponentQueryDto;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductComponentFactoryDto implements ComponentFactoryMakeDto{

    private ComponentQueryDto componentQueryDto;
    private List<ProductComponentQueryDto> queries;
    private List<SortItem> sortItems;




    @Override
    public ProductComponentDetail toComponentDetail() {
        return ProductComponentDetail.builder()
                .componentId(componentQueryDto.getComponentId())
                .productComponentId(queries.getFirst().getProductComponentId())
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
        if(!queries.isEmpty()) return queries.getFirst().getProductComponentId();
        else return 0L;
    }

    @Override
    public List<Long> getTargetIds() {
        return queries.stream().map(ProductComponentQueryDto::getProductComponentId).collect(Collectors.toList());
    }

}
