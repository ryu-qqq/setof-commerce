package com.setof.connectly.module.display.service.component.fetch.category;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.display.dto.component.SortItem;
import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.factory.CategoryComponentFactoryDto;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.display.dto.filter.category.CategoryItemFilter;
import com.setof.connectly.module.display.dto.query.ComponentItemQueryDto;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.dto.query.category.CategoryComponentQueryDto;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.display.factory.ComponentFactoryStrategy;
import com.setof.connectly.module.display.mapper.ComponentItemMapper;
import com.setof.connectly.module.display.repository.component.category.CategoryComponentFindRepository;
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
public class CategoryComponentFindService
        extends BaseProductComponentFindService<CategoryComponentQueryDto> {

    private final CategoryComponentFindRepository categoryComponentFindRepository;

    public CategoryComponentFindService(
            ComponentFactoryStrategy componentFactoryStrategy,
            ProductSliceMapper productSliceMapper,
            ProductGroupFindService productGroupFindService,
            ComponentItemMapper componentItemMapper,
            CategoryComponentFindRepository categoryComponentFindRepository) {
        super(
                componentFactoryStrategy,
                productSliceMapper,
                productGroupFindService,
                componentItemMapper);
        this.categoryComponentFindRepository = categoryComponentFindRepository;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.CATEGORY;
    }

    @Override
    public Map<Integer, SubComponent> fetchComponents(
            List<CategoryComponentQueryDto> productRelatedQueries,
            List<ComponentItemQueryDto> componentItemQueries,
            List<ComponentQueryDto> componentQueries) {

        Map<Integer, SubComponent> results = new HashMap<>();

        Map<Long, List<CategoryComponentQueryDto>> categoryComponentQueryMap =
                toListMap(productRelatedQueries);
        Map<Long, List<ComponentItemQueryDto>> componentItemMap =
                componentIdMap(componentItemQueries);
        Map<Long, ComponentQueryDto> componentQueryMap = componentQueryMap(componentQueries);
        Map<Long, Integer> orderMap = displayOrderMap(componentQueries);

        for (Long componentId : categoryComponentQueryMap.keySet()) {
            List<SortItem> sortItems =
                    determineProductGroupThumbnailsForCategory(componentId, componentItemMap);

            CategoryComponentFactoryDto categoryComponentFactoryDto =
                    CategoryComponentFactoryDto.builder()
                            .componentQueryDto(componentQueryMap.get(componentId))
                            .queries(categoryComponentQueryMap.get(componentId))
                            .build();

            List<ProductGroupThumbnail> productGroupThumbnails =
                    processSortItemsWithLastDomainId(
                            componentId,
                            sortItems,
                            toCategoryFilter(categoryComponentFactoryDto.getTargetId()),
                            PageRequest.of(
                                    0,
                                    categoryComponentFactoryDto.toComponentDetail().getPageSize()));
            categoryComponentFactoryDto.setProductGroupThumbnails(productGroupThumbnails);

            results.put(orderMap.get(componentId), toComponentDetail(categoryComponentFactoryDto));
        }

        return results;
    }

    private List<SortItem> determineProductGroupThumbnailsForCategory(
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
        return categoryComponentFindRepository.fetchCategoryComponentsWhenLesserThanExposedSize(
                componentId, productIds, filter, pageSize);
    }

    public CategoryItemFilter toCategoryFilter(long categoryId) {
        return CategoryItemFilter.builder().categoryId(categoryId).build();
    }
}
