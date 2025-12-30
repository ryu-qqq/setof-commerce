package com.connectly.partnerAdmin.module.display.mapper.image;

import com.connectly.partnerAdmin.module.display.dto.component.image.ImageComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.image.ImageComponentLink;
import com.connectly.partnerAdmin.module.display.entity.component.item.ImageComponentItem;
import com.connectly.partnerAdmin.module.display.entity.component.sub.ImageComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ImageComponentMapperImpl implements ImageComponentMapper {

    @Override
    public ImageComponent toEntity(com.connectly.partnerAdmin.module.display.entity.component.Component component, ImageComponentDetail imageComponentDetail) {
        return ImageComponent.builder()
                .component(component)
                .imageType(imageComponentDetail.getImageType())
                .build();
    }

    @Override
    public List<ImageComponentItem> toImageComponentItem(long imageComponentId, List<ImageComponentLink> imageComponentLinks) {
        return imageComponentLinks.stream().map(imageComponentLink -> ImageComponentItem.builder()
                .imageComponentId(imageComponentId)
                .displayOrder(imageComponentLink.getDisplayOrder())
                .imageUrl(imageComponentLink.getImageUrl())
                .linkUrl(imageComponentLink.getLinkUrl())
                .imageSize(imageComponentLink.getImageSize())
                .build())
                .collect(Collectors.toList());
    }

    @Override
    public ImageComponentItem toImageComponentItem(ImageComponentLink link) {
        ImageComponentItem.ImageComponentItemBuilder imageComponentItemBuilder = ImageComponentItem.builder()
                .imageComponentId(link.getImageComponentId())
                .displayOrder(link.getDisplayOrder())
                .imageUrl(link.getImageUrl())
                .linkUrl(link.getLinkUrl())
                .imageSize(link.getImageSize());

        if(link.getImageComponentItemId() != null) imageComponentItemBuilder.id(link.getImageComponentItemId());

        return imageComponentItemBuilder
                .build();
    }


}
