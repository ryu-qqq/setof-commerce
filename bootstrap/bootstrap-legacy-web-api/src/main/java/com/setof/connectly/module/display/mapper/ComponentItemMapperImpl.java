package com.setof.connectly.module.display.mapper;

import com.setof.connectly.module.common.mapper.CursorValueProvider;
import com.setof.connectly.module.display.dto.component.SortItem;
import com.setof.connectly.module.display.dto.query.ComponentItemQueryDto;
import com.setof.connectly.module.display.enums.SortType;
import com.setof.connectly.module.display.enums.component.OrderType;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.utils.SortUtils;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ComponentItemMapperImpl implements ComponentItemMapper {

    @Override
    public List<SortItem> transProductGroupThumbnail(
            List<ComponentItemQueryDto> componentItemQueries) {

        Map<SortType, List<ComponentItemQueryDto>> groupedBySortType =
                componentItemQueries.stream()
                        .filter(c -> c.getSortType() != null)
                        .collect(Collectors.groupingBy(ComponentItemQueryDto::getSortType));

        List<SortItem> sortItems = new ArrayList<>();

        for (SortType sortType : Arrays.asList(SortType.FIXED, SortType.AUTO)) {
            List<ProductGroupThumbnail> productGroupThumbnails =
                    Optional.ofNullable(groupedBySortType.get(sortType))
                            .orElse(Collections.emptyList())
                            .stream()
                            .sorted(
                                    Optional.ofNullable(
                                                    getComparatorBasedOnOrderType(
                                                            componentItemQueries, sortType))
                                            .orElse(
                                                    Comparator.comparingLong(
                                                            CursorValueProvider::getId)))
                            .filter(
                                    componentItemQuery ->
                                            componentItemQuery.getProductGroupId() != 0)
                            .map(
                                    componentItemQuery ->
                                            ProductGroupThumbnail.builder()
                                                    .productGroupId(
                                                            componentItemQuery.getProductGroupId())
                                                    .sellerId(componentItemQuery.getSellerId())
                                                    .productGroupName(
                                                            componentItemQuery
                                                                    .getProductDisplayName())
                                                    .brand(componentItemQuery.getBrand())
                                                    .productImageUrl(
                                                            componentItemQuery.getProductImageUrl())
                                                    .price(componentItemQuery.getPrice())
                                                    .insertDate(componentItemQuery.getInsertDate())
                                                    .averageRating(
                                                            componentItemQuery.getAverageRating())
                                                    .reviewCount(
                                                            componentItemQuery.getReviewCount())
                                                    .score(componentItemQuery.getScore())
                                                    .build())
                            .collect(Collectors.toList());

            sortItems.add(
                    SortItem.builder()
                            .sortType(sortType)
                            .productGroups(productGroupThumbnails)
                            .build());
        }
        return sortItems;
    }

    private Comparator<CursorValueProvider> getComparatorBasedOnOrderType(
            List<ComponentItemQueryDto> componentItemQueries, SortType sortType) {
        OrderType orderType = determineOrderType(componentItemQueries, sortType);
        return SortUtils.getComparatorBasedOnOrderType(orderType);
    }

    private OrderType determineOrderType(
            List<ComponentItemQueryDto> componentItemQueries, SortType sortType) {
        if (sortType == SortType.FIXED) {
            return OrderType.NONE;
        }
        if (componentItemQueries.isEmpty()) {
            return OrderType.NONE;
        }
        return componentItemQueries.get(0).getOrderType();
    }
}
