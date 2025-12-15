package com.setof.connectly.module.display.dto.factory;

import com.setof.connectly.module.display.dto.component.tab.TabComponentDetail;
import com.setof.connectly.module.display.dto.component.tab.TabDetail;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.dto.query.tab.TabComponentQueryDto;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TabComponentFactoryDto implements ComponentFactoryMakeDto {

    private ComponentQueryDto componentQueryDto;
    private List<TabComponentQueryDto> queries;
    private List<ProductGroupThumbnail> productGroupThumbnails;
    private long tabComponentId;
    private List<TabDetail> tabDetails;

    public TabComponentFactoryDto(
            ComponentQueryDto componentQueryDto,
            List<TabComponentQueryDto> queries,
            List<ProductGroupThumbnail> productGroupThumbnails,
            long tabComponentId,
            List<TabDetail> tabDetails) {
        this.componentQueryDto = componentQueryDto;
        this.queries = queries;
        this.productGroupThumbnails = productGroupThumbnails;
        this.tabComponentId = tabComponentId;
        this.tabDetails = tabDetails;
    }

    @Override
    public TabComponentDetail toComponentDetail() {
        return TabComponentDetail.builder()
                .componentId(componentQueryDto.getComponentId())
                .tabComponentId(queries.get(0).getTabComponentId())
                .componentDetails(componentQueryDto.getComponentDetails())
                .tabDetails(tabDetails)
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
        if (queries.size() > 0) return queries.get(0).getComponentId();
        else return 0L;
    }

    @Override
    public Long getTargetId() {
        if (queries.size() > 0) return queries.get(0).getTabId();
        else return 0L;
    }

    @Override
    public List<Long> getTargetIds() {
        return queries.stream().map(TabComponentQueryDto::getTabId).collect(Collectors.toList());
    }

    public void setTabDetails(List<TabDetail> tabDetails) {
        this.tabDetails = tabDetails;
    }
}
