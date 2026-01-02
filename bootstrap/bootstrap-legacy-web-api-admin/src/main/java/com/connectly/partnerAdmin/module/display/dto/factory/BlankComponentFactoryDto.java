package com.connectly.partnerAdmin.module.display.dto.factory;


import com.connectly.partnerAdmin.module.display.dto.component.blank.BlankComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.blank.BlankComponentQueryDto;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BlankComponentFactoryDto implements ComponentFactoryMakeDto{

    private ComponentQueryDto componentQueryDto;
    private BlankComponentQueryDto query;

    @Override
    public BlankComponentDetail toComponentDetail() {
        return BlankComponentDetail.builder()
                .componentId(componentQueryDto.getComponentId())
                .blankComponentId(query.getBlankComponentId())
                .componentName(componentQueryDto.getComponentName())
                .displayOrder(componentQueryDto.getDisplayOrder())
                .displayPeriod(componentQueryDto.getDisplayPeriod())
                .displayYn(componentQueryDto.getDisplayYn())
                .displayOrder(componentQueryDto.getDisplayOrder())
                .componentDetails(componentQueryDto.getComponentDetails())
                .lineYn(query.getLineYn())
                .height(query.getHeight())
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
        return query.getBlankComponentId();
    }
}
