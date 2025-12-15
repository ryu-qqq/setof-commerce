package com.setof.connectly.module.display.service.content;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.dto.component.NotProductRelatedComponents;
import com.setof.connectly.module.display.dto.component.ProductRelatedComponents;
import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.content.ContentGroupResponse;
import com.setof.connectly.module.display.dto.content.OnDisplayContent;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.display.dto.query.ComponentItemQueryDto;
import com.setof.connectly.module.display.dto.query.ComponentQuery;
import com.setof.connectly.module.display.dto.query.ComponentQueryDto;
import com.setof.connectly.module.display.dto.query.ContentQueryDto;
import com.setof.connectly.module.display.enums.component.ComponentType;
import com.setof.connectly.module.display.mapper.ContentMapper;
import com.setof.connectly.module.display.repository.component.NotRelatedProductComponentRepository;
import com.setof.connectly.module.display.repository.component.RelatedProductComponentRepository;
import com.setof.connectly.module.display.repository.content.ContentFindRepository;
import com.setof.connectly.module.display.service.component.NonProductComponentFetchStrategy;
import com.setof.connectly.module.display.service.component.ProductComponentFetchStrategy;
import com.setof.connectly.module.display.service.component.SubNonProductComponentFindService;
import com.setof.connectly.module.display.service.component.SubProductComponentFindService;
import com.setof.connectly.module.display.service.component.fetch.ComponentProductFindService;
import com.setof.connectly.module.exception.content.ContentNotFoundException;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ContentFindServiceImpl implements ContentFindService {

    private final ContentFindRepository contentFindRepository;
    private final NotRelatedProductComponentRepository notRelatedProductComponentRepository;
    private final RelatedProductComponentRepository relatedProductComponentRepository;
    private final ContentMapper contentMapper;
    private final ComponentProductFindService componentProductFindService;
    private final ProductComponentFetchStrategy componentFetchStrategy;
    private final NonProductComponentFetchStrategy nonProductComponentFetchStrategy;

    @Override
    public OnDisplayContent fetchOnDisplayContents() {
        List<Long> contentIds = contentFindRepository.fetchOnDisplayContents();
        return OnDisplayContent.builder().contentIds(new HashSet<>(contentIds)).build();
    }

    @Override
    public ContentGroupResponse fetchContent(long contentId, Yn bypass) {
        ContentQueryDto contentQueryDto =
                contentFindRepository
                        .fetchContentQueryInfo(contentId, bypass)
                        .orElseThrow(() -> new ContentNotFoundException(contentId));

        long findContentId = contentQueryDto.getContentId();

        List<Long> nonComponentIds =
                contentQueryDto.getComponentQueries().stream()
                        .filter(ComponentQueryDto::isNonProductRelatedContents)
                        .map(ComponentQueryDto::getComponentId)
                        .collect(Collectors.toList());

        List<Long> componentIds =
                contentQueryDto.getComponentQueries().stream()
                        .filter(ComponentQueryDto::isProductRelatedContents)
                        .map(ComponentQueryDto::getComponentId)
                        .collect(Collectors.toList());

        NotProductRelatedComponents nonProductComponents =
                notRelatedProductComponentRepository.fetchNonRelatedProductComponent(
                        findContentId, nonComponentIds);
        ProductRelatedComponents productComponents =
                relatedProductComponentRepository.fetchRelatedProductComponent(
                        findContentId, componentIds);
        Set<ComponentItemQueryDto> componentItemQuerySet =
                new HashSet<>(
                        relatedProductComponentRepository.fetchComponentItemQueries(componentIds));

        if (productComponents != null && !componentItemQuerySet.isEmpty()) {
            productComponents.setComponentItemQueries(componentItemQuerySet);
        }

        Map<Integer, SubComponent> combinedMap = new HashMap<>();

        if (nonProductComponents != null)
            processNonProductsComponents(
                    nonProductComponents.getAllComponents(),
                    contentQueryDto.getComponentQueries(),
                    combinedMap);
        if (productComponents != null)
            processComponents(
                    productComponents.getAllComponents(),
                    contentQueryDto.getComponentQueries(),
                    new ArrayList<>(productComponents.getComponentItemQueries()),
                    combinedMap);

        return contentMapper.toContentGroupResponse(contentQueryDto, combinedMap);
    }

    @Override
    public ContentGroupResponse fetchOnlyContent(long contentId) {
        return contentFindRepository
                .fetchContent(contentId)
                .orElseThrow(() -> new ContentNotFoundException(contentId));
    }

    @Override
    public CustomSlice<ProductGroupThumbnail> fetchComponentProductGroups(
            long componentId, ComponentFilter filter, Pageable pageable) {
        Set<ComponentItemQueryDto> componentItemQuerySet =
                new HashSet<>(
                        relatedProductComponentRepository.fetchComponentItemQueries(
                                Collections.singletonList(componentId), filter));
        return componentProductFindService.fetchComponentProductGroups(
                componentId, componentItemQuerySet, filter, pageable);
    }

    private void processNonProductsComponents(
            List<? extends ComponentQuery> allComponents,
            List<ComponentQueryDto> contents,
            Map<Integer, SubComponent> result) {

        Map<ComponentType, List<ComponentQuery>> tempGroupedComponents =
                allComponents.stream()
                        .collect(Collectors.groupingBy(ComponentQuery::getComponentType));

        Map<ComponentType, List<? extends ComponentQuery>> groupedComponents =
                (Map<ComponentType, List<? extends ComponentQuery>>)
                        (Map<?, ?>) tempGroupedComponents;

        for (ComponentType componentType : groupedComponents.keySet()) {
            SubNonProductComponentFindService<? extends ComponentQuery> service =
                    nonProductComponentFetchStrategy.get(componentType);
            Map<Integer, SubComponent> fetchedComponents =
                    fetchWithService(
                            (SubNonProductComponentFindService) service,
                            groupedComponents.get(componentType),
                            contents);
            result.putAll(fetchedComponents);
        }
    }

    private <T extends ComponentQuery> Map<Integer, SubComponent> fetchWithService(
            SubNonProductComponentFindService<T> service,
            List<T> queries,
            List<ComponentQueryDto> contents) {
        return service.fetchComponents(queries, contents);
    }

    private void processComponents(
            List<? extends ComponentQuery> allComponents,
            List<ComponentQueryDto> contents,
            List<ComponentItemQueryDto> items,
            Map<Integer, SubComponent> result) {

        Map<ComponentType, List<ComponentQuery>> tempGroupedComponents =
                allComponents.stream()
                        .collect(Collectors.groupingBy(ComponentQuery::getComponentType));

        Map<ComponentType, List<? extends ComponentQuery>> groupedComponents =
                (Map<ComponentType, List<? extends ComponentQuery>>)
                        (Map<?, ?>) tempGroupedComponents;

        for (ComponentType componentType : groupedComponents.keySet()) {
            SubProductComponentFindService<? extends ComponentQuery> service =
                    componentFetchStrategy.get(componentType);
            Map<Integer, SubComponent> fetchedComponents =
                    fetchWithService(
                            (SubProductComponentFindService) service,
                            groupedComponents.get(componentType),
                            contents,
                            items);
            result.putAll(fetchedComponents);
        }
    }

    private <T extends ComponentQuery> Map<Integer, SubComponent> fetchWithService(
            SubProductComponentFindService<T> service,
            List<T> queries,
            List<ComponentQueryDto> contents,
            List<ComponentItemQueryDto> items) {
        return service.fetchComponents(queries, items, contents);
    }
}
