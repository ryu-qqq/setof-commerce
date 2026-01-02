package com.connectly.partnerAdmin.module.display.service.content;


import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.display.dto.component.NotProductRelatedComponents;
import com.connectly.partnerAdmin.module.display.dto.component.ProductRelatedComponents;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.content.ContentGroupResponse;
import com.connectly.partnerAdmin.module.display.dto.content.ContentResponse;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentItemQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQuery;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.ContentQueryDto;
import com.connectly.partnerAdmin.module.display.entity.content.Content;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.exception.ContentErrorConstant;
import com.connectly.partnerAdmin.module.display.exception.ContentNotFoundException;
import com.connectly.partnerAdmin.module.display.filter.ContentFilter;
import com.connectly.partnerAdmin.module.display.mapper.ContentMapper;
import com.connectly.partnerAdmin.module.display.repository.component.NotRelatedProductComponentRepository;
import com.connectly.partnerAdmin.module.display.repository.component.RelatedProductComponentRepository;
import com.connectly.partnerAdmin.module.display.repository.content.ContentFetchRepository;
import com.connectly.partnerAdmin.module.display.service.component.fetch.NonProductComponentFetchStrategy;
import com.connectly.partnerAdmin.module.display.service.component.fetch.ProductComponentFetchStrategy;
import com.connectly.partnerAdmin.module.display.service.component.fetch.SubNonProductComponentFetchService;
import com.connectly.partnerAdmin.module.display.service.component.fetch.SubProductComponentFetchService;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ContentFetchServiceImpl implements ContentFetchService {

    private final ContentFetchRepository contentFetchRepository;
    private final NotRelatedProductComponentRepository notRelatedProductComponentRepository;
    private final RelatedProductComponentRepository relatedProductComponentRepository;
    private final ContentMapper contentMapper;
    private final ProductComponentFetchStrategy componentFetchStrategy;
    private final NonProductComponentFetchStrategy nonProductComponentFetchStrategy;



    @Override
    public Content fetchContentEntity(long contentId) {
        return contentFetchRepository.fetchContentEntity(contentId).orElseThrow(() -> new ContentNotFoundException(ContentErrorConstant.CONTENT_NOT_FOUND_MSG));
    }

    @Override
    public ContentGroupResponse fetchContent(long contentId) {
        ContentQueryDto contentQueryDto = contentFetchRepository.fetchContentQueryInfo(contentId).orElseThrow(() -> new ContentNotFoundException(ContentErrorConstant.CONTENT_NOT_FOUND_MSG));

        List<Long> nonComponentIds = contentQueryDto.getComponentQueries().stream()
                .filter(ComponentQueryDto::isNonProductRelatedContents)
                .map(ComponentQueryDto::getComponentId).collect(Collectors.toList());

        List<Long> componentIds = contentQueryDto.getComponentQueries().stream()
                .filter(ComponentQueryDto::isProductRelatedContents)
                .map(ComponentQueryDto::getComponentId).collect(Collectors.toList());

        NotProductRelatedComponents nonProductComponents = notRelatedProductComponentRepository.fetchNonRelatedProductComponent(contentId, nonComponentIds);
        ProductRelatedComponents productComponents = relatedProductComponentRepository.fetchRelatedProductComponent(contentId, componentIds);
        Set<ComponentItemQueryDto> componentItemQuerySet = new HashSet<>(relatedProductComponentRepository.fetchComponentItemQueries(componentIds));

        if(productComponents != null && !componentItemQuerySet.isEmpty()){
            productComponents.setComponentItemQueries(componentItemQuerySet);
        }

        Map<Integer, SubComponent> combinedMap = new HashMap<>();

        if(nonProductComponents != null) processNonProductsComponents(nonProductComponents.getAllComponents(), contentQueryDto.getComponentQueries(), combinedMap);
        if(productComponents != null) processComponents(productComponents.getAllComponents(), contentQueryDto.getComponentQueries(),  new ArrayList<>(productComponents.getComponentItemQueries()), combinedMap);



        return contentMapper.toContentGroupResponse(contentQueryDto, combinedMap);
    }


    @Override
    public CustomPageable<ContentResponse> fetchContents(ContentFilter filter, Pageable pageable){
        List<ContentResponse> results = contentFetchRepository.fetchContents(filter, pageable);
        long totalCount = fetchContentCountQuery(filter, pageable);
        return contentMapper.toContentResponses(results, pageable, totalCount);
    }

    private long fetchContentCountQuery(ContentFilter filter, Pageable pageable){
        JPAQuery<Long> longJPAQuery = contentFetchRepository.fetchContentQuery(filter, pageable);
        Long totalCount = longJPAQuery.fetchOne();
        if(totalCount == null) return 0L;
        return totalCount;
    }

    private void processNonProductsComponents(List<? extends ComponentQuery> allComponents, List<ComponentQueryDto> contents, Map<Integer, SubComponent> result) {

        Map<ComponentType, List<ComponentQuery>> tempGroupedComponents = allComponents.stream()
                .collect(Collectors.groupingBy(ComponentQuery::getComponentType));

        Map<ComponentType, List<? extends ComponentQuery>> groupedComponents = (Map<ComponentType, List<? extends ComponentQuery>>) (Map<?, ?>) tempGroupedComponents;

        for(ComponentType componentType : groupedComponents.keySet()) {
            SubNonProductComponentFetchService<? extends ComponentQuery> service = nonProductComponentFetchStrategy.get(componentType);
            Map<Integer, SubComponent> fetchedComponents = fetchWithService((SubNonProductComponentFetchService)service, groupedComponents.get(componentType), contents);
            result.putAll(fetchedComponents);
        }
    }

    private <T extends ComponentQuery> Map<Integer, SubComponent> fetchWithService(SubNonProductComponentFetchService<T> service, List<T> queries, List<ComponentQueryDto> contents) {
        return service.fetchComponents(queries, contents);
    }

    private void processComponents(List<? extends ComponentQuery> allComponents, List<ComponentQueryDto> contents, List<ComponentItemQueryDto> items, Map<Integer, SubComponent> result) {

        Map<ComponentType, List<ComponentQuery>> tempGroupedComponents = allComponents.stream()
                .collect(Collectors.groupingBy(ComponentQuery::getComponentType));

        Map<ComponentType, List<? extends ComponentQuery>> groupedComponents = (Map<ComponentType, List<? extends ComponentQuery>>) (Map<?, ?>) tempGroupedComponents;

        for(ComponentType componentType : groupedComponents.keySet()) {
            SubProductComponentFetchService<? extends ComponentQuery> service = componentFetchStrategy.get(componentType);
            Map<Integer, SubComponent> fetchedComponents = fetchWithService((SubProductComponentFetchService)service, groupedComponents.get(componentType), contents, items);
            result.putAll(fetchedComponents);
        }
    }

    private <T extends ComponentQuery> Map<Integer, SubComponent> fetchWithService(SubProductComponentFetchService<T> service, List<T> queries, List<ComponentQueryDto> contents, List<ComponentItemQueryDto> items) {
        return service.fetchComponents(queries, items, contents);
    }




}
