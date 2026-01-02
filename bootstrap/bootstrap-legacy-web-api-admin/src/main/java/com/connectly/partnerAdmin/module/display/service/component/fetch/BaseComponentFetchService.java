package com.connectly.partnerAdmin.module.display.service.component.fetch;

import com.connectly.partnerAdmin.module.display.dto.component.DisplayProductGroupThumbnail;
import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.factory.ComponentFactoryMakeDto;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentItemQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQuery;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;
import com.connectly.partnerAdmin.module.display.enums.SortType;
import com.connectly.partnerAdmin.module.display.factory.ComponentFactory;
import com.connectly.partnerAdmin.module.display.factory.ComponentFactoryStrategy;

import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public abstract class BaseComponentFetchService implements SubComponentFetchService {

    private final ComponentFactoryStrategy componentFactoryStrategy;

    protected LinkedHashMap<Long, Integer> displayOrderMap(List<ComponentQueryDto> componentQueries) {
        return componentQueries.stream()
                .sorted(Comparator.comparingInt(ComponentQueryDto::getDisplayOrder))
                .collect(Collectors.toMap(
                        ComponentQueryDto::getComponentId,
                        ComponentQueryDto::getDisplayOrder,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }




    protected Map<Long, ComponentQueryDto> componentQueryMap(List<ComponentQueryDto> componentQueries){
        return componentQueries.stream().collect(Collectors.toMap(ComponentQueryDto::getComponentId, Function.identity()));
    }

    protected <U extends ComponentQuery> Map<Long, List<U>> toListMap(List<U> componentQueries) {
        return componentQueries.stream()
                .collect(Collectors.groupingBy(U::getComponentId));
    }

    protected <U extends ComponentQuery> Map<Long, U> toMap(List<U> componentQueries) {
        return componentQueries.stream()
                .collect(Collectors.toMap(U::getComponentId, Function.identity()));
    }

    protected <U extends SubComponent, K extends ComponentFactoryMakeDto> SubComponent toComponentDetail(K makeDto){
        ComponentFactory<U, K> componentFactory = (ComponentFactory<U, K>) componentFactoryStrategy.get(getComponentType());
        return componentFactory.makeSubComponent(makeDto);
    }


    protected List<SortItem> transProductGroupThumbnail(List<ComponentItemQueryDto> componentItemQueries) {

        Map<SortType, List<ComponentItemQueryDto>> groupedBySortType = componentItemQueries.stream()
                .filter(c -> c.getSortType() != null)
                .collect(Collectors.groupingBy(ComponentItemQueryDto::getSortType));

        List<SortItem> sortItems = new ArrayList<>();

        for (SortType sortType : Arrays.asList(SortType.FIXED, SortType.AUTO)) {
            List<DisplayProductGroupThumbnail> productGroupThumbnails = Optional.ofNullable(groupedBySortType.get(sortType))
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(componentItemQuery -> componentItemQuery.getProductGroupId() != 0)
                    .sorted(Comparator.comparingInt(ComponentItemQueryDto::getDisplayOrder))
                    .map(componentItemQuery ->
                            DisplayProductGroupThumbnail.builder()
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