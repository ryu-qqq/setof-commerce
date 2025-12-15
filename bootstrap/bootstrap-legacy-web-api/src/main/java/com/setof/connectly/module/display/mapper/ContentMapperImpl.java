package com.setof.connectly.module.display.mapper;

import com.setof.connectly.module.display.dto.component.ComponentDetailResponse;
import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.content.ContentGroupResponse;
import com.setof.connectly.module.display.dto.query.ContentQueryDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContentMapperImpl implements ContentMapper {

    @Override
    public ContentGroupResponse toContentGroupResponse(
            ContentQueryDto contentQueryDto, Map<Integer, SubComponent> combinedMap) {
        List<ComponentDetailResponse> componentDetails =
                combinedMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(Map.Entry::getValue)
                        .map(this::toComponentDetail)
                        .collect(Collectors.toList());

        return ContentGroupResponse.builder()
                .contentId(contentQueryDto.getContentId())
                .title(contentQueryDto.getTitle())
                .displayPeriod(contentQueryDto.getDisplayPeriod())
                .componentDetails(componentDetails)
                .imageUrl(contentQueryDto.getImageUrl())
                .build();
    }

    private ComponentDetailResponse toComponentDetail(SubComponent subComponent) {
        ComponentDetailResponse.ComponentDetailResponseBuilder builder =
                ComponentDetailResponse.builder()
                        .componentId(subComponent.getComponentId())
                        .componentName(subComponent.getComponentName())
                        .componentType(subComponent.getComponentType())
                        .listType(subComponent.getComponentDetails().getListType())
                        .orderType(subComponent.getComponentDetails().getOrderType())
                        .badgeType(subComponent.getComponentDetails().getBadgeType())
                        .filterYn(subComponent.getComponentDetails().getFilterYn())
                        .displayYn(subComponent.getDisplayYn())
                        .component(subComponent);

        if (subComponent.isProductRelatedComponent()) {
            builder.viewExtensionDetails(subComponent.getViewExtensionDetails());
        }

        return builder.build();
    }
}
