package com.setof.connectly.module.display.dto.factory;

import com.setof.connectly.module.display.dto.component.image.ImageComponentDetail;
import com.setof.connectly.module.display.dto.component.image.ImageComponentLink;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.dto.query.image.ImageComponentQueryDto;
import com.setof.connectly.module.display.enums.ImageType;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageComponentFactoryDto implements ComponentFactoryMakeDto {

    private ComponentQueryDto componentQueryDto;
    private List<ImageComponentQueryDto> queries;
    private List<String> imageUrls;

    public ImageComponentFactoryDto(
            ComponentQueryDto componentQueryDto,
            List<ImageComponentQueryDto> queries,
            List<String> imageUrls) {
        this.componentQueryDto = componentQueryDto;
        this.queries = queries;
        this.imageUrls = getImageUrls();
    }

    public List<String> getImageUrls() {
        return queries.stream()
                .sorted(Comparator.comparingInt(ImageComponentQueryDto::getImageDisplayOrder))
                .map(ImageComponentQueryDto::getImageUrl)
                .collect(Collectors.toList());
    }

    public ImageType getImageType() {
        if (queries.size() > 0) return queries.get(0).getImageType();
        else return ImageType.SINGLE;
    }

    @Override
    public ImageComponentDetail toComponentDetail() {

        List<ImageComponentLink> imageComponentLinks =
                queries.stream()
                        .map(
                                s -> {
                                    return ImageComponentLink.builder()
                                            .imageComponentItemId(s.getImageComponentItemId())
                                            .displayOrder(s.getImageDisplayOrder())
                                            .imageUrl(s.getImageUrl())
                                            .linkUrl(s.getLinkUrl())
                                            .build();
                                })
                        .collect(Collectors.toList());

        return ImageComponentDetail.builder()
                .componentId(componentQueryDto.getComponentId())
                .imageComponentId(queries.get(0).getImageComponentId())
                .imageType(queries.get(0).getImageType())
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
        if (queries.size() > 0) return queries.get(0).getImageComponentId();
        else return 0L;
    }
}
