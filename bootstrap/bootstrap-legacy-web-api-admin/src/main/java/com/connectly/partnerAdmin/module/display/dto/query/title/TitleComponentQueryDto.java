package com.connectly.partnerAdmin.module.display.dto.query.title;

import com.connectly.partnerAdmin.module.display.dto.query.ComponentQuery;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TitleComponentQueryDto implements ComponentQuery {

    private long contentId;
    private long componentId;
    private long titleComponentId;
    private String title1;
    private String title2;
    private String subTitle1;
    private String subTitle2;

    @QueryProjection
    public TitleComponentQueryDto(long contentId, long componentId, long titleComponentId, String title1, String title2, String subTitle1, String subTitle2) {
        this.contentId = contentId;
        this.componentId = componentId;
        this.titleComponentId = titleComponentId;
        this.title1 = title1;
        this.title2 = title2;
        this.subTitle1 = subTitle1;
        this.subTitle2 = subTitle2;
    }

    @Override
    public int hashCode() {
        return String.valueOf(this.componentId + this.titleComponentId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TitleComponentQueryDto p) {
            return this.hashCode()==p.hashCode();
        }
        return false;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TITLE;
    }


}
