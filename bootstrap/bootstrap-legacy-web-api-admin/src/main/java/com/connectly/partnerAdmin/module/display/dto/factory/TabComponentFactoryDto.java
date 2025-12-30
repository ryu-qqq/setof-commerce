package com.connectly.partnerAdmin.module.display.dto.factory;


import com.connectly.partnerAdmin.module.display.dto.component.DisplayProductGroupThumbnail;
import com.connectly.partnerAdmin.module.display.dto.component.tab.TabComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.tab.TabDetail;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.tab.TabComponentQueryDto;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TabComponentFactoryDto implements ComponentFactoryMakeDto{

    private ComponentQueryDto componentQueryDto;
    private List<TabComponentQueryDto> queries;
    private List<DisplayProductGroupThumbnail> productGroupThumbnails;
    private long tabComponentId;
    private List<TabDetail> tabDetails;





    @Override
    public TabComponentDetail toComponentDetail() {
        return TabComponentDetail.builder()
                .componentId(componentQueryDto.getComponentId())
                .tabComponentId(queries.getFirst().getTabComponentId())
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
        if(!queries.isEmpty()) return queries.getFirst().getComponentId();
        else return 0L;
    }

    @Override
    public Long getTargetId() {
        if(!queries.isEmpty()) return queries.getFirst().getTabId();
        else return 0L;
    }

    @Override
    public List<Long> getTargetIds() {
        return queries.stream().map(TabComponentQueryDto::getTabId).collect(Collectors.toList());
    }

}
