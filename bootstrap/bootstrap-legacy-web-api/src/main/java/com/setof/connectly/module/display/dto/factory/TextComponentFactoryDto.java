package com.setof.connectly.module.display.dto.factory;

import com.setof.connectly.module.display.dto.component.text.TextComponentDetail;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.dto.query.text.TextComponentQueryDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TextComponentFactoryDto implements ComponentFactoryMakeDto {

    private ComponentQueryDto componentQueryDto;
    private TextComponentQueryDto query;

    public TextComponentFactoryDto(
            ComponentQueryDto componentQueryDto, TextComponentQueryDto query) {
        this.componentQueryDto = componentQueryDto;
        this.query = query;
    }

    @Override
    public TextComponentDetail toComponentDetail() {
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
