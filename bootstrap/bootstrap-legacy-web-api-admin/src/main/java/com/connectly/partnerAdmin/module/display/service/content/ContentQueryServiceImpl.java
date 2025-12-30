package com.connectly.partnerAdmin.module.display.service.content;

import com.connectly.partnerAdmin.module.display.dto.component.ComponentUpdatePair;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.content.ContentGroupResponse;
import com.connectly.partnerAdmin.module.display.dto.content.ContentResponse;
import com.connectly.partnerAdmin.module.display.dto.content.query.CreateContent;
import com.connectly.partnerAdmin.module.display.dto.content.query.UpdateContentDisplayYn;
import com.connectly.partnerAdmin.module.display.entity.content.Content;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.mapper.ContentMapper;
import com.connectly.partnerAdmin.module.display.mapper.ContentQueryMapper;
import com.connectly.partnerAdmin.module.display.repository.content.ContentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.content.ContentRepository;
import com.connectly.partnerAdmin.module.display.service.component.query.ComponentDeleteService;
import com.connectly.partnerAdmin.module.display.service.component.query.ComponentQueryStrategy;
import com.connectly.partnerAdmin.module.display.service.component.query.SubComponentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class ContentQueryServiceImpl implements ContentQueryService{

    private final ContentFetchService contentFetchService;
    private final ContentRepository contentRepository;
    private final ComponentQueryStrategy componentQueryStrategy;
    private final ContentQueryMapper contentQueryMapper;
    private final ContentMapper contentFetchMapper;
    private final ContentJdbcRepository contentJdbcRepository;
    private final ComponentDeleteService componentDeleteService;

    @Override
    public ContentResponse enrollContent(CreateContent createContent) {
        if(createContent.getContentId() != null && createContent.getContentId()>0) return updateContent(createContent);

        Content content = contentQueryMapper.toEntity(createContent);
        Content savedContent = contentRepository.save(content);

        createAllSubComponents(savedContent.getId(), createContent.getComponentsByType());

        return contentFetchMapper.toContentResponse(savedContent);
    }

    @Override
    public ContentResponse updateDisplayYn(long contentId, UpdateContentDisplayYn updateContentDisplayYn) {
        Content content = contentFetchService.fetchContentEntity(contentId);
        content.updateDisplayYn(updateContentDisplayYn.getDisplayYn());
        return contentFetchMapper.toContentResponse(content);
    }


    private void createAllSubComponents(long contentId, Map<ComponentType, List<SubComponent>> componentsByType) {
        componentsByType.forEach((componentType, components) ->
                createSubComponent(contentId, componentType, components));
    }


    public ContentResponse updateContent(CreateContent createContent) {
        ContentGroupResponse contentGroupResponse = contentFetchService.fetchContent(createContent.getContentId());
        Content content = contentQueryMapper.toEntity(createContent);
        contentJdbcRepository.update(content);

        Map<AbstractMap.SimpleEntry<Long, ComponentType>, SubComponent> existingComponentMap =
                createExistingComponentMap(contentGroupResponse);

        List<SubComponent> componentsToBeAdded = new ArrayList<>();
        List<SubComponent> componentsToBeRemoved = new ArrayList<>();
        List<ComponentUpdatePair<SubComponent>> componentsToBeUpdated = new ArrayList<>();

        classifyComponents(createContent, existingComponentMap, componentsToBeAdded, componentsToBeUpdated, componentsToBeRemoved);
        addNewComponents(createContent, componentsToBeAdded);
        removeComponents(componentsToBeRemoved);
        updateComponents(componentsToBeUpdated);

        return contentFetchMapper.toContentResponse(content);
    }


    private Map<AbstractMap.SimpleEntry<Long, ComponentType>, SubComponent> createExistingComponentMap(ContentGroupResponse contentGroupResponse) {
        return contentGroupResponse.getComponents().stream()
                .collect(Collectors.toMap(comp ->
                                new AbstractMap.SimpleEntry<>(comp.getSubComponentId(), comp.getComponentType()),
                        Function.identity()));
    }

    private <T extends SubComponent> void classifyComponents(
            CreateContent createContent,
            Map<AbstractMap.SimpleEntry<Long, ComponentType>, SubComponent> existingComponentMap,
            List<SubComponent> componentsToBeAdded,
            List<ComponentUpdatePair<SubComponent>> componentsToBeUpdated,
            List<SubComponent> componentsToBeRemoved
    ) {
        Set<Long> requestComponentIds = createContent.getComponents().stream()
                .map(SubComponent::getSubComponentId)
                .collect(Collectors.toSet());

        for (Map.Entry<AbstractMap.SimpleEntry<Long, ComponentType>, SubComponent> entry : existingComponentMap.entrySet()) {
            Long existingId = entry.getKey().getKey();
            if (!requestComponentIds.contains(existingId)) {
                componentsToBeRemoved.add(entry.getValue());
            }
        }

        for (SubComponent newComp : createContent.getComponents()) {
            AbstractMap.SimpleEntry<Long, ComponentType> key = new AbstractMap.SimpleEntry<>(newComp.getSubComponentId(), newComp.getComponentType());

            if (!existingComponentMap.containsKey(key)) {
                componentsToBeAdded.add(newComp);
            } else {
                SubComponent existingComp = existingComponentMap.get(key);
                componentsToBeUpdated.add(new ComponentUpdatePair<>(newComp.getComponentType(), newComp, newComp.getClass().cast(existingComp)));

                componentsToBeRemoved.removeIf(comp -> comp.getSubComponentId().equals(existingComp.getSubComponentId()));
            }
        }
    }




    private void addNewComponents(CreateContent createContent, List<SubComponent> componentsToBeAdded) {
        Map<ComponentType, List<SubComponent>> componentsToAddByType = componentsToBeAdded.stream()
                .collect(Collectors.groupingBy(SubComponent::getComponentType));
        createAllSubComponents(createContent.getContentId(), componentsToAddByType);
    }


    private void createSubComponent(long contentId, ComponentType componentType, List<SubComponent> components) {
        SubComponentQueryService<SubComponent> subComponentQueryService = (SubComponentQueryService<SubComponent>) componentQueryStrategy.get(componentType);
        subComponentQueryService.createComponents(contentId, components);
    }

    private void removeComponents(List<SubComponent> componentsToBeRemoved){
        componentDeleteService.deleteAll(componentsToBeRemoved);
    }


    private <T extends SubComponent> void updateComponents(List<ComponentUpdatePair<T>> componentsToBeUpdated) {
        componentsToBeUpdated.stream()
                .collect(Collectors.groupingBy(ComponentUpdatePair::getComponentType))
                .forEach((componentType, components) -> {
                    SubComponentQueryService<T> subComponentQueryService = (SubComponentQueryService<T>) componentQueryStrategy.get(componentType);
                    subComponentQueryService.updateComponents(components);
                });
    }


}
