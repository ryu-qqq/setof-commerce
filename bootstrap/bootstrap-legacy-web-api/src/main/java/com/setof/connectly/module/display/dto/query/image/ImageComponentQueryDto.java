package com.setof.connectly.module.display.dto.query.image;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.display.dto.query.ComponentQuery;
import com.setof.connectly.module.display.enums.ImageType;
import com.setof.connectly.module.display.enums.component.ComponentType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageComponentQueryDto implements ComponentQuery {

    private long contentId;
    private long componentId;
    private long imageComponentId;
    private ImageType imageType;

    private long imageComponentItemId;
    private String imageUrl;

    private String linkUrl;

    private int imageDisplayOrder;

    @QueryProjection
    public ImageComponentQueryDto(
            long contentId,
            long componentId,
            long imageComponentId,
            ImageType imageType,
            long imageComponentItemId,
            String imageUrl,
            String linkUrl,
            int imageDisplayOrder) {
        this.contentId = contentId;
        this.componentId = componentId;
        this.imageComponentId = imageComponentId;
        this.imageType = imageType;
        this.imageComponentItemId = imageComponentItemId;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.imageDisplayOrder = imageDisplayOrder;
    }

    @Override
    public int hashCode() {
        return ((this.componentId + this.imageComponentId) + this.imageUrl).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImageComponentQueryDto) {
            ImageComponentQueryDto p = (ImageComponentQueryDto) obj;
            return this.hashCode() == p.hashCode();
        }
        return false;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.IMAGE;
    }
}
