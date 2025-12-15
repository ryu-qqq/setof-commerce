package com.setof.connectly.module.display.service.component.fetch.brand;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.display.dto.component.SortItem;
import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.factory.BrandComponentFactoryDto;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.display.dto.filter.brand.BrandItemFilter;
import com.setof.connectly.module.display.dto.query.ComponentItemQueryDto;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.dto.query.brand.BrandComponentQueryDto;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.display.factory.ComponentFactoryStrategy;
import com.setof.connectly.module.display.mapper.ComponentItemMapper;
import com.setof.connectly.module.display.repository.component.brand.BrandComponentFindRepository;
import com.setof.connectly.module.display.service.component.fetch.BaseProductComponentFindService;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.mapper.ProductSliceMapper;
import com.setof.connectly.module.product.service.group.fetch.ProductGroupFindService;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class BrandComponentFindService
        extends BaseProductComponentFindService<BrandComponentQueryDto> {

    private final BrandComponentFindRepository brandComponentFindRepository;

    public BrandComponentFindService(
            ComponentFactoryStrategy componentFactoryStrategy,
            ProductSliceMapper productSliceMapper,
            ProductGroupFindService productGroupFindService,
            ComponentItemMapper componentItemMapper,
            BrandComponentFindRepository brandComponentFindRepository) {
        super(
                componentFactoryStrategy,
                productSliceMapper,
                productGroupFindService,
                componentItemMapper);
        this.brandComponentFindRepository = brandComponentFindRepository;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BRAND;
    }

    @Override
    public Map<Integer, SubComponent> fetchComponents(
            List<BrandComponentQueryDto> productRelatedQueries,
            List<ComponentItemQueryDto> componentItemQueries,
            List<ComponentQueryDto> componentQueries) {
        Map<Integer, SubComponent> results = new HashMap<>();

        Map<Long, List<BrandComponentQueryDto>> brandComponentQueryMap =
                toListMap(productRelatedQueries);
        Map<Long, List<ComponentItemQueryDto>> componentItemMap =
                componentIdMap(componentItemQueries);
        Map<Long, ComponentQueryDto> componentQueryMap = componentQueryMap(componentQueries);
        Map<Long, Integer> orderMap = displayOrderMap(componentQueries);

        for (Long componentId : brandComponentQueryMap.keySet()) {
            List<SortItem> sortItems =
                    determineProductGroupThumbnails(componentId, componentItemMap);

            BrandComponentFactoryDto brandComponentFactoryDto =
                    BrandComponentFactoryDto.builder()
                            .componentQueryDto(componentQueryMap.get(componentId))
                            .queries(brandComponentQueryMap.get(componentId))
                            .build();

            List<Long> categoryIds =
                    brandComponentFactoryDto.getQueries().stream()
                            .map(BrandComponentQueryDto::getCategoryId)
                            .collect(Collectors.toList());
            List<ProductGroupThumbnail> productGroupThumbnails =
                    processSortItemsWithLastDomainId(
                            componentId,
                            sortItems,
                            toBrandFilter(brandComponentFactoryDto.getTargetIds(), categoryIds),
                            PageRequest.of(
                                    0, brandComponentFactoryDto.toComponentDetail().getPageSize()));
            brandComponentFactoryDto.setProductGroupThumbnails(productGroupThumbnails);

            results.put(orderMap.get(componentId), toComponentDetail(brandComponentFactoryDto));
        }

        return results;
    }

    private List<SortItem> determineProductGroupThumbnails(
            Long componentId, Map<Long, List<ComponentItemQueryDto>> componentItemMap) {
        List<ComponentItemQueryDto> componentItems =
                componentItemMap.getOrDefault(componentId, new ArrayList<>());
        return transProductGroupThumbnail(componentItems);
    }

    @Override
    public CustomSlice<ProductGroupThumbnail> fetchComponentProductGroups(
            long componentId,
            Set<ComponentItemQueryDto> componentItemQueries,
            ComponentFilter filter,
            Pageable pageable) {
        return super.fetchComponentProductGroups(
                componentId, componentItemQueries, filter, pageable);
    }

    @Override
    public <R extends ComponentFilter> List<ProductGroupThumbnail> processSortItemsWithLastDomainId(
            long componentId, List<SortItem> sortItems, R filterDto, Pageable pageable) {
        return super.processSortItemsWithLastDomainId(componentId, sortItems, filterDto, pageable);
    }

    @Override
    protected List<ProductGroupThumbnail> fetchNoOffsetComponentRelatedProducts(
            long componentId, Set<Long> productIds, ComponentFilter filter, int pageSize) {
        return brandComponentFindRepository.fetchBrandComponentsWhenLesserThanExposedSize(
                componentId, productIds, filter, pageSize);
    }

    protected BrandItemFilter toBrandFilter(List<Long> brandIds, List<Long> categoryIds) {
        return BrandItemFilter.builder().brandIds(brandIds).categoryIds(categoryIds).build();
    }
}
