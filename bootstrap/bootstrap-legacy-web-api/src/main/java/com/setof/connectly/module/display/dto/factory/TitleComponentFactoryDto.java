package com.setof.connectly.module.display.dto.factory;

import com.setof.connectly.module.display.dto.component.title.TitleComponentDetail;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.dto.query.title.TitleComponentQueryDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TitleComponentFactoryDto implements ComponentFactoryMakeDto {

    private ComponentQueryDto componentQueryDto;
    private TitleComponentQueryDto query;

    public TitleComponentFactoryDto(
            ComponentQueryDto componentQueryDto, TitleComponentQueryDto query) {
        this.componentQueryDto = componentQueryDto;
        this.query = query;
    }

    @Override
    public TitleComponentDetail toComponentDetail() {
        return TitleComponentDetail.builder()
                .componentId(componentQueryDto.getComponentId())
                .titleComponentId(query.getTitleComponentId())
                .title1(query.getTitle1())
                .title2(query.getTitle2())
                .subTitle1(query.getSubTitle1())
                .subTitle2(query.getSubTitle2())
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
        return query.getTitleComponentId();
    }
}
