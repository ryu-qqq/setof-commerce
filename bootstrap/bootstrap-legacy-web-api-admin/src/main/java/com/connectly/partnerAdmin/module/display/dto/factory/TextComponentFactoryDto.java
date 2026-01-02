package com.connectly.partnerAdmin.module.display.dto.factory;

import com.connectly.partnerAdmin.module.display.dto.component.text.TextComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.text.TextComponentQueryDto;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TextComponentFactoryDto implements ComponentFactoryMakeDto{

    private ComponentQueryDto componentQueryDto;
    private TextComponentQueryDto query;

    @Override
    public TextComponentDetail toComponentDetail(){
        return TextComponentDetail.builder()
                .componentId(componentQueryDto.getComponentId())
                .textComponentId(query.getTextComponentId())
                .content(query.getContent())
                .componentName(componentQueryDto.getComponentName())
                .displayOrder(componentQueryDto.getDisplayOrder())
                .displayPeriod(componentQueryDto.getDisplayPeriod())
                .displayYn(componentQueryDto.getDisplayYn())
                .componentDetails(componentQueryDto.getComponentDetails())
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
        return query.getTextComponentId();
    }
}
