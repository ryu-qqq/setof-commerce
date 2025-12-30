package com.connectly.partnerAdmin.module.display.dto.query.text;

import com.connectly.partnerAdmin.module.display.dto.query.ComponentQuery;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TextComponentQueryDto implements ComponentQuery {
    private long contentId;
    private long componentId;
    private long textComponentId;
    private String content;

    @QueryProjection
    public TextComponentQueryDto(long contentId, long componentId, long textComponentId, String content) {
        this.contentId = contentId;
        this.componentId = componentId;
        this.textComponentId = textComponentId;
        this.content = content;
    }

    @Override
    public int hashCode() {
        return String.valueOf(this.componentId + this.textComponentId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TextComponentQueryDto p) {
            return this.hashCode()==p.hashCode();
        }
        return false;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TEXT;
    }

}
