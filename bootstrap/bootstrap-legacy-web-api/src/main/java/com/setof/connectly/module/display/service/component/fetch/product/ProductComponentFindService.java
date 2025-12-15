package com.setof.connectly.module.display.service.component.fetch.product;

import com.setof.connectly.module.display.dto.component.SortItem;
import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.factory.ProductComponentFactoryDto;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.display.dto.filter.product.ProductItemFilter;
import com.setof.connectly.module.display.dto.query.ComponentItemQueryDto;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.dto.query.product.ProductComponentQueryDto;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.display.factory.ComponentFactoryStrategy;
import com.setof.connectly.module.display.mapper.ComponentItemMapper;
import com.setof.connectly.module.display.service.component.fetch.BaseProductComponentFindService;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.mapper.ProductSliceMapper;
import com.setof.connectly.module.product.service.group.fetch.ProductGroupFindService;
import java.util.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductComponentFindService
        extends BaseProductComponentFindService<ProductComponentQueryDto> {

    public ProductComponentFindService(
            ComponentFactoryStrategy componentFactoryStrategy,
            ProductSliceMapper productSliceMapper,
            ProductGroupFindService productGroupFindService,
            ComponentItemMapper componentItemMapper) {
        super(
                componentFactoryStrategy,
                productSliceMapper,
                productGroupFindService,
                componentItemMapper);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.PRODUCT;
    }

    @Override
    public Map<Integer, SubComponent> fetchComponents(
            List<ProductComponentQueryDto> productRelatedQueries,
            List<ComponentItemQueryDto> componentItemQueries,
            List<ComponentQueryDto> componentQueries) {

        Map<Integer, SubComponent> results = new HashMap<>();

        Map<Long, List<ProductComponentQueryDto>> productComponentQueryMap =
                toListMap(productRelatedQueries);
        Map<Long, List<ComponentItemQueryDto>> componentItemMap =
                componentIdMap(componentItemQueries);
        Map<Long, ComponentQueryDto> componentQueryMap = componentQueryMap(componentQueries);
        Map<Long, Integer> orderMap = displayOrderMap(componentQueries);

        for (Long componentId : productComponentQueryMap.keySet()) {
            List<SortItem> sortItems =
                    determineProductGroupThumbnailsForProduct(componentId, componentItemMap);

            ProductComponentFactoryDto productComponentFactoryDto =
                    ProductComponentFactoryDto.builder()
                            .componentQueryDto(componentQueryMap.get(componentId))
                            .queries(productComponentQueryMap.get(componentId))
                            .build();

            List<ProductGroupThumbnail> productGroupThumbnails =
                    processSortItemsWithLastDomainId(
                            componentId,
                            sortItems,
                            toProductFilter(new ArrayList<>(), new ArrayList<>()),
                            PageRequest.of(
                                    0,
                                    productComponentFactoryDto.toComponentDetail().getPageSize()));
            productComponentFactoryDto.setProductGroupThumbnails(productGroupThumbnails);

            results.put(orderMap.get(componentId), toComponentDetail(productComponentFactoryDto));
        }
        return results;
    }

    private List<SortItem> determineProductGroupThumbnailsForProduct(
            Long componentId, Map<Long, List<ComponentItemQueryDto>> componentItemMap) {
        List<ComponentItemQueryDto> componentItems =
                componentItemMap.getOrDefault(componentId, new ArrayList<>());
        return transProductGroupThumbnail(componentItems);
    }

    @Override
    public <R extends ComponentFilter> List<ProductGroupThumbnail> processSortItemsWithLastDomainId(
            long componentId, List<SortItem> sortItems, R filterDto, Pageable pageable) {
        return super.processSortItemsWithLastDomainId(componentId, sortItems, filterDto, pageable);
    }

    @Override
    protected long fetchProductCount(ComponentFilter filter, Set<Long> productIds) {
        return productIds.size();
    }

    protected ProductItemFilter toProductFilter(List<Long> brandIds, List<Long> categoryIds) {
        return ProductItemFilter.builder().brandIds(brandIds).categoryIds(categoryIds).build();
    }
}
