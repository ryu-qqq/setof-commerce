package com.setof.connectly.module.display.service.component.fetch;

import com.setof.connectly.module.common.dto.CursorDto;
import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.common.filter.ItemFilter;
import com.setof.connectly.module.display.dto.component.SortItem;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.display.dto.query.ComponentItemQueryDto;
import com.setof.connectly.module.display.dto.query.ComponentQuery;
import com.setof.connectly.module.display.enums.SortType;
import com.setof.connectly.module.display.factory.ComponentFactoryStrategy;
import com.setof.connectly.module.display.mapper.ComponentItemMapper;
import com.setof.connectly.module.display.service.component.SubProductComponentFindService;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.mapper.ProductSliceMapper;
import com.setof.connectly.module.product.service.group.fetch.ProductGroupFindService;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Pageable;

public abstract class BaseProductComponentFindService<T extends ComponentQuery>
        extends BaseComponentFindService implements SubProductComponentFindService<T> {
    private final ProductSliceMapper productSliceMapper;
    private final ProductGroupFindService productGroupFindService;
    private final ComponentItemMapper componentItemMapper;

    public BaseProductComponentFindService(
            ComponentFactoryStrategy componentFactoryStrategy,
            ProductSliceMapper productSliceMapper,
            ProductGroupFindService productGroupFindService,
            ComponentItemMapper componentItemMapper) {
        super(componentFactoryStrategy);
        this.productSliceMapper = productSliceMapper;
        this.productGroupFindService = productGroupFindService;
        this.componentItemMapper = componentItemMapper;
    }

    protected List<SortItem> transProductGroupThumbnail(
            List<ComponentItemQueryDto> componentItemQueries) {
        return componentItemMapper.transProductGroupThumbnail(componentItemQueries);
    }

    protected Map<Long, List<ComponentItemQueryDto>> componentIdMap(
            List<ComponentItemQueryDto> componentItemQueries) {
        return componentItemQueries.stream()
                .collect(Collectors.groupingBy(ComponentItemQueryDto::getComponentId));
    }

    @Override
    public CustomSlice<ProductGroupThumbnail> fetchComponentProductGroups(
            long componentId,
            Set<ComponentItemQueryDto> componentItemQueries,
            ComponentFilter filter,
            Pageable pageable) {
        List<SortItem> sortItems =
                transProductGroupThumbnail(new ArrayList<>(componentItemQueries));
        Set<Long> productIds = extractProductIds(sortItems);
        long productCount = fetchProductCount(filter, productIds);

        List<ProductGroupThumbnail> results =
                fetchSortedProductGroups(componentId, sortItems, filter, pageable, productIds);

        return toSlice(results, pageable, productCount, filter);
    }

    private List<ProductGroupThumbnail> fetchSortedProductGroups(
            long componentId,
            List<SortItem> sortItems,
            ComponentFilter filter,
            Pageable pageable,
            Set<Long> productIds) {
        if (filter.getLastDomainId() != null && productIds.contains(filter.getLastDomainId())) {
            return processSortItemsWithLastDomainId(componentId, sortItems, filter, pageable);
        } else {
            if (filter.getLastDomainId() == null)
                return processSortItemsWithLastDomainId(componentId, sortItems, filter, pageable);
            return fetchNoOffsetComponentRelatedProducts(
                    componentId, productIds, filter, pageable.getPageSize());
        }
    }

    @Override
    public <R extends ComponentFilter> List<ProductGroupThumbnail> processSortItemsWithLastDomainId(
            long componentId, List<SortItem> sortItems, R filter, Pageable pageable) {
        LinkedList<ProductGroupThumbnail> combinedProductGroups = new LinkedList<>();

        Map<SortType, SortItem> sortTypeSortItemMap = toSortTypeMap(sortItems);

        Set<Long> toExcludeProductGroupIds =
                processFixedProductsAndGetExclusions(sortTypeSortItemMap, combinedProductGroups);
        List<ProductGroupThumbnail> autoProductGroups =
                processAutoProducts(sortTypeSortItemMap, toExcludeProductGroupIds, filter);

        combinedProductGroups.addAll(autoProductGroups);

        int startIndex =
                findStartIndex(
                        combinedProductGroups,
                        filter.getLastDomainId() != null ? filter.getLastDomainId() : 0);
        int endIndex =
                Math.min(startIndex + pageable.getPageSize() + 1, combinedProductGroups.size());

        List<ProductGroupThumbnail> result =
                new ArrayList<>(combinedProductGroups.subList(startIndex, endIndex));

        if (result.size() < pageable.getPageSize()) {
            setForNoOffset(filter, toExcludeProductGroupIds);
            int remainingCount = pageable.getPageSize() - result.size();
            List<ProductGroupThumbnail> productGroupThumbnails =
                    fetchNoOffsetComponentRelatedProducts(
                            componentId, toExcludeProductGroupIds, filter, remainingCount);
            result.addAll(productGroupThumbnails);
        }

        return result;
    }

    private Map<SortType, SortItem> toSortTypeMap(List<SortItem> sortItems) {
        return sortItems.stream()
                .collect(
                        Collectors.toMap(
                                SortItem::getSortType,
                                Function.identity(),
                                (existing, replacement) -> existing));
    }

    private Set<Long> processFixedProductsAndGetExclusions(
            Map<SortType, SortItem> sortTypeSortItemMap,
            LinkedList<ProductGroupThumbnail> combinedProductGroups) {
        Set<Long> toExcludeProductGroupIds = new HashSet<>();

        SortItem fixedProducts = sortTypeSortItemMap.get(SortType.FIXED);

        if (fixedProducts != null && fixedProducts.hasProductGroups()) {
            combinedProductGroups.addAll(fixedProducts.getProductGroups());
            fixedProducts
                    .getProductGroups()
                    .forEach(pg -> toExcludeProductGroupIds.add(pg.getProductGroupId()));
        }

        return toExcludeProductGroupIds;
    }

    private List<ProductGroupThumbnail> processAutoProducts(
            Map<SortType, SortItem> sortTypeSortItemMap,
            Set<Long> toExcludeProductGroupIds,
            ComponentFilter filter) {
        SortItem autoProducts = sortTypeSortItemMap.get(SortType.AUTO);

        if (autoProducts != null && autoProducts.hasProductGroups()) {
            Stream<ProductGroupThumbnail> stream = autoProducts.getProductGroups().stream();

            if (!toExcludeProductGroupIds.isEmpty()) {
                stream =
                        stream.filter(
                                pg -> !toExcludeProductGroupIds.contains(pg.getProductGroupId()));
            }

            return stream.collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    protected long fetchProductCount(ComponentFilter filter, Set<Long> productIds) {
        return productGroupFindService.fetchProductCountQuery(filter) + productIds.size();
    }

    protected List<ProductGroupThumbnail> fetchNoOffsetComponentRelatedProducts(
            long componentId, Set<Long> productIds, ComponentFilter filter, int pageSize) {
        return new ArrayList<>();
    }

    protected int findStartIndex(List<ProductGroupThumbnail> productGroups, Long lastDomainId) {
        if (lastDomainId == 0) return 0;
        for (int i = 0; i < productGroups.size(); i++) {
            if (productGroups.get(i).getProductGroupId() == lastDomainId) {
                return i + 1;
            }
        }
        return 0;
    }

    protected Set<Long> extractProductIds(List<SortItem> sortItems) {
        return sortItems.stream()
                .flatMap(sortItem -> sortItem.getProductGroups().stream())
                .map(ProductGroupThumbnail::getProductGroupId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    protected CustomSlice<ProductGroupThumbnail> toSlice(
            List<ProductGroupThumbnail> results,
            Pageable pageable,
            long productCount,
            ItemFilter filter) {
        return productSliceMapper.toSlice(results, pageable, productCount, filter);
    }

    private <R extends ComponentFilter> void setForNoOffset(
            R filter, Set<Long> toExcludeProductGroupIds) {
        Optional<CursorDto> cursorDtoOpt =
                productGroupFindService.fetchLastProductGroupId(filter, toExcludeProductGroupIds);

        if (cursorDtoOpt.isPresent()) {
            CursorDto cursorDto = cursorDtoOpt.get();
            filter.setLastDomainId(cursorDto.getLastDomainId());
            String cursorValue = cursorDto.getCursorValue(filter.getOrderType());
            filter.setCursorValue(cursorValue);
        }
    }
}
