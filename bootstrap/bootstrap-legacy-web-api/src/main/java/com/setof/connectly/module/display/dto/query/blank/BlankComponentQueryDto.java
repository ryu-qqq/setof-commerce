package com.setof.connectly.module.display.dto.query.blank;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.dto.query.ComponentQuery;
import com.setof.connectly.module.display.enums.component.ComponentType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlankComponentQueryDto implements ComponentQuery {

    private long contentId;
    private long componentId;
    private long blankComponentId;
    private double height;
    private Yn lineYn;

    @QueryProjection
    public BlankComponentQueryDto(
            long contentId, long componentId, long blankComponentId, double height, Yn lineYn) {
        this.contentId = contentId;
        this.componentId = componentId;
        this.blankComponentId = blankComponentId;
        this.height = height;
        this.lineYn = lineYn;
    }

    @Override
    public int hashCode() {
        return String.valueOf(this.componentId + this.blankComponentId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BlankComponentQueryDto) {
            BlankComponentQueryDto p = (BlankComponentQueryDto) obj;
            return this.hashCode() == p.hashCode();
        }
        return false;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BLANK;
    }
}
