package com.connectly.partnerAdmin.module.display.service.component.query;

import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.component.tab.TabComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.tab.TabDetail;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.tab.TabJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.tab.TabRepository;
import com.connectly.partnerAdmin.module.display.service.component.query.item.ComponentItemQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.item.ComponentTargetQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.viewExtension.ViewExtensionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ComponentDeleteServiceImpl implements ComponentDeleteService{

    private final ComponentQueryStrategy componentQueryStrategy;
    private final ComponentTargetQueryService componentTargetQueryService;
    private final ComponentItemQueryService componentItemQueryService;
    private final ViewExtensionQueryService viewExtensionQueryService;
    private final ComponentJdbcRepository componentJdbcRepository;

    @Override
    public void deleteAll(List<SubComponent> components) {

        List<Long> componentIds = components.stream().map(SubComponent::getComponentId).collect(Collectors.toList());
        deleteComponent(componentIds);

        List<SubComponent> productRelatedComponents = components.stream()
                .filter(subComponent -> subComponent.getComponentType()
                        .isProductRelatedContents())
                .collect(Collectors.toList());

        deleteProductRelatedComponents(productRelatedComponents);

        Map<ComponentType, List<SubComponent>> componentTypeMap = components.stream().collect(Collectors.groupingBy(SubComponent::getComponentType));

        componentTypeMap.forEach((componentType, subComponents) ->
        {
            List<Long> subComponentIds = subComponents.stream().map(SubComponent::getSubComponentId).collect(Collectors.toList());
            SubComponentQueryService<SubComponent> subComponentQueryService = (SubComponentQueryService<SubComponent>) componentQueryStrategy.get(componentType);
            subComponentQueryService.removeComponents(subComponentIds);
        });

    }

    private void deleteProductRelatedComponents(List<SubComponent> productRelatedComponents){
        List<SubComponent> tabComponents = productRelatedComponents.stream()
                .filter(subComponent -> subComponent.getComponentType().equals(ComponentType.TAB))
                .collect(Collectors.toList());

        List<SubComponent> normalProductRelatedComponents = productRelatedComponents.stream()
                .filter(subComponent -> !subComponent.getComponentType().equals(ComponentType.TAB))
                .collect(Collectors.toList());

        deleteTabComponentTargetAndItems(tabComponents);
        deleteNormalComponentTargetAndItems(normalProductRelatedComponents);

        List<Long> viewExtensionIds = productRelatedComponents.stream().map(SubComponent::getViewExtensionId).collect(Collectors.toList());
        if(!viewExtensionIds.isEmpty()) deleteViewExtension(viewExtensionIds);

    }

    private void deleteTabComponentTargetAndItems(List<SubComponent> tabComponents){
        List<Long> deleteTabIds = new ArrayList<>();
        for(SubComponent subComponent : tabComponents){
            if (subComponent instanceof TabComponentDetail) {
                List<Long> tabIds = ((TabComponentDetail) subComponent).getTabDetails().stream()
                        .map(TabDetail::getTabId).collect(Collectors.toList());

                deleteTabIds.addAll(tabIds);
            }
        }

        if(!deleteTabIds.isEmpty()) deleteTabComponentTargetsAndItems(deleteTabIds);
    }

    private void deleteTabComponentTargetsAndItems(List<Long> tabIds){
        componentItemQueryService.deleteComponentItemsWithTabIds(tabIds);
        componentTargetQueryService.deleteComponentTargetWithTabIds(tabIds);
    }

    private void deleteNormalComponentTargetAndItems(List<SubComponent> normalProductRelatedComponents){
        List<Long> productRelatedComponentIds = normalProductRelatedComponents.stream().map(SubComponent::getComponentId).collect(Collectors.toList());
        if(!productRelatedComponentIds.isEmpty()) deleteComponentTargetsAndItems(productRelatedComponentIds);
    }

    private void deleteComponentTargetsAndItems(List<Long> componentIds){
        componentItemQueryService.deleteComponentItems(componentIds);
        componentTargetQueryService.deleteComponentTarget(componentIds);
    }

    private void deleteComponent(List<Long> componentIds){
        componentJdbcRepository.deleteAll(componentIds);
    }

    private void deleteViewExtension(List<Long> viewExtensionIds){
        viewExtensionQueryService.deleteAll(viewExtensionIds);
    }



}
