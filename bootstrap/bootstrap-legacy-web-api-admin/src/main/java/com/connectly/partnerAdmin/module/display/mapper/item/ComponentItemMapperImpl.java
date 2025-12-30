package com.connectly.partnerAdmin.module.display.mapper.item;

import com.connectly.partnerAdmin.module.display.dto.component.DisplayProductGroupThumbnail;
import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentItemQueryDto;
import com.connectly.partnerAdmin.module.display.entity.component.ComponentItem;
import com.connectly.partnerAdmin.module.display.entity.component.ComponentTarget;
import com.connectly.partnerAdmin.module.display.enums.SortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;



@Slf4j
@Component
@RequiredArgsConstructor
public class ComponentItemMapperImpl implements ComponentItemMapper{

    @Override
    public List<ComponentItem> toEntities(ComponentTarget componentTarget, List<DisplayProductGroupThumbnail> productGroupThumbnails) {
        return productGroupThumbnails.stream()
                .map(productGroupThumbnail ->{
                    return ComponentItem.builder()
                            .componentTarget(componentTarget)
                            .displayOrder(productGroupThumbnail.getDisplayOrder())
                            .productGroupId(productGroupThumbnail.getProductGroupId())
                            .productDisplayImage(productGroupThumbnail.getProductImageUrl())
                            .productDisplayName(productGroupThumbnail.getProductGroupName())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SortItem> transProductGroupThumbnail(List<ComponentItemQueryDto> componentItemQueries) {

        Map<SortType, List<ComponentItemQueryDto>> groupedBySortType = componentItemQueries.stream()
                .filter(c -> c.getSortType() != null)
                .collect(Collectors.groupingBy(ComponentItemQueryDto::getSortType));

        List<SortItem> sortItems = new ArrayList<>();

        // FIXED와 AUTO 순으로 SortItem 객체를 생성
        for (SortType sortType : Arrays.asList(SortType.FIXED, SortType.AUTO)) {
            List<DisplayProductGroupThumbnail> productGroupThumbnails = Optional.ofNullable(groupedBySortType.get(sortType))
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(componentItemQuery -> componentItemQuery.getProductGroupId() != 0)
                    .sorted(Comparator.comparingInt(ComponentItemQueryDto::getDisplayOrder))
                    .map(componentItemQuery -> DisplayProductGroupThumbnail.builder()
                            .productGroupId(componentItemQuery.getProductGroupId())
                            .productGroupName(componentItemQuery.getProductDisplayName())
                            .brand(componentItemQuery.getBrand())
                            .productImageUrl(componentItemQuery.getProductImageUrl())
                            .price(componentItemQuery.getPrice())
                            .displayOrder(componentItemQuery.getDisplayOrder())
                            .build())
                    .collect(Collectors.toList());

            sortItems.add(SortItem.builder()
                    .sortType(sortType)
                    .productGroups(productGroupThumbnails)
                    .build());
        }

        return sortItems;
    }

}
