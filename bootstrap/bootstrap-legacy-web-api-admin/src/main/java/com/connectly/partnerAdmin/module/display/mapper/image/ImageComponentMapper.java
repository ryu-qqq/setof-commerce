package com.connectly.partnerAdmin.module.display.mapper.image;

import com.connectly.partnerAdmin.module.display.dto.component.image.ImageComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.image.ImageComponentLink;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.item.ImageComponentItem;
import com.connectly.partnerAdmin.module.display.entity.component.sub.ImageComponent;

import java.util.List;

public interface ImageComponentMapper {
    ImageComponent toEntity(Component component, ImageComponentDetail imageComponentDetail);
    List<ImageComponentItem> toImageComponentItem(long imageComponentId, List<ImageComponentLink> imageComponentLinks);
    ImageComponentItem toImageComponentItem(ImageComponentLink link);

}
