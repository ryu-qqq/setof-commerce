package com.connectly.partnerAdmin.module.display.dto.factory;


import com.connectly.partnerAdmin.module.display.dto.component.image.ImageComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.image.ImageComponentLink;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.image.ImageComponentQueryDto;
import com.connectly.partnerAdmin.module.display.enums.ImageType;
import lombok.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ImageComponentFactoryDto implements ComponentFactoryMakeDto{

    private ComponentQueryDto componentQueryDto;
    private List<ImageComponentQueryDto> queries;
    private List<String> imageUrls;




    public List<String> getImageUrls(){
        return queries.stream()
                .sorted(Comparator.comparingInt(ImageComponentQueryDto::getImageDisplayOrder))
                .map(ImageComponentQueryDto::getImageUrl)
                .collect(Collectors.toList());
    }

    public ImageType getImageType(){
        if(!queries.isEmpty()) return queries.getFirst().getImageType();
        else return ImageType.SINGLE;
    }

    @Override
    public ImageComponentDetail toComponentDetail() {

        List<ImageComponentLink> imageComponentLinks = queries.stream()
                .map(s ->
                        ImageComponentLink.builder()
                .imageComponentItemId(s.getImageComponentItemId())
                .displayOrder(s.getImageDisplayOrder())
                .imageUrl(s.getImageUrl())
                .linkUrl(s.getLinkUrl())
                .imageSize(s.getImageSize())
                .build())
                .collect(Collectors.toList());


        return ImageComponentDetail.builder()
                .componentId(componentQueryDto.getComponentId())
                .imageComponentId(queries.getFirst().getImageComponentId())
                .imageType(queries.getFirst().getImageType())
                .imageComponentLinks(imageComponentLinks)
                .displayYn(componentQueryDto.getDisplayYn())
                .displayPeriod(componentQueryDto.getDisplayPeriod())
                .componentName(componentQueryDto.getComponentName())
                .displayOrder(componentQueryDto.getDisplayOrder())
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
        if(!queries.isEmpty()) return queries.getFirst().getImageComponentId();
        else return 0L;
    }
}
