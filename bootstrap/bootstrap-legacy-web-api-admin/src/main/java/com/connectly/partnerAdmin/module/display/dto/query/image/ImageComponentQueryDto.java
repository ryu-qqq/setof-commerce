package com.connectly.partnerAdmin.module.display.dto.query.image;

import com.connectly.partnerAdmin.module.display.dto.query.ComponentQuery;
import com.connectly.partnerAdmin.module.display.entity.embedded.ImageSize;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.enums.ImageType;
import com.querydsl.core.annotations.QueryProjection;
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
    private ImageSize imageSize;


    @QueryProjection
    public ImageComponentQueryDto(long contentId, long componentId, long imageComponentId, ImageType imageType, long imageComponentItemId, String imageUrl, String linkUrl, int imageDisplayOrder, ImageSize imageSize) {
        this.contentId = contentId;
        this.componentId = componentId;
        this.imageComponentId = imageComponentId;
        this.imageType = imageType;
        this.imageComponentItemId = imageComponentItemId;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.imageDisplayOrder = imageDisplayOrder;
        this.imageSize = imageSize;
    }

    @Override
    public int hashCode() {
        return ((this.componentId + this.imageComponentId) +this.imageUrl).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ImageComponentQueryDto p) {
            return this.hashCode()==p.hashCode();
        }
        return false;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.IMAGE;
    }

}
