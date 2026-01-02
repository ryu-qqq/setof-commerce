package com.connectly.partnerAdmin.module.display.service.component.query;

import com.connectly.partnerAdmin.module.display.dto.component.*;
import com.connectly.partnerAdmin.module.display.dto.component.tab.TabComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.tab.TabDetail;
import com.connectly.partnerAdmin.module.display.entity.component.ComponentItem;
import com.connectly.partnerAdmin.module.display.entity.component.ComponentTarget;
import com.connectly.partnerAdmin.module.display.entity.component.ViewExtension;
import com.connectly.partnerAdmin.module.display.entity.embedded.ViewExtensionDetails;
import com.connectly.partnerAdmin.module.display.enums.SortType;
import com.connectly.partnerAdmin.module.display.mapper.ComponentQueryMapper;
import com.connectly.partnerAdmin.module.display.mapper.item.ComponentItemMapper;
import com.connectly.partnerAdmin.module.display.mapper.item.ComponentTargetMapper;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentRepository;
import com.connectly.partnerAdmin.module.display.service.component.query.item.ComponentItemQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.item.ComponentTargetQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.item.SortItemUpdateProcessor;
import com.connectly.partnerAdmin.module.display.service.component.query.item.fetch.ComponentTargetFindService;
import com.connectly.partnerAdmin.module.display.service.component.query.viewExtension.ViewExtensionQueryService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public abstract class ProductRelatedComponentQueryService <T extends SubComponent> extends BaseComponentQueryService<T>{

    private final ComponentTargetMapper componentTargetMapper;
    private final ComponentItemMapper componentItemMapper;
    private final ViewExtensionQueryService viewExtensionQueryService;
    private final ComponentTargetQueryService componentTargetQueryService;
    private final ComponentItemQueryService componentItemQueryService;
    private final SortItemUpdateProcessor sortItemUpdateProcessor;
    private final ComponentTargetFindService componentTargetFindService;

    public ProductRelatedComponentQueryService(ComponentQueryMapper componentQueryMapper, ComponentRepository componentRepository, ComponentJdbcRepository componentJdbcRepository, ComponentTargetMapper componentTargetMapper, ComponentItemMapper componentItemMapper, ViewExtensionQueryService viewExtensionQueryService, ComponentTargetQueryService componentTargetQueryService, ComponentItemQueryService componentItemQueryService, SortItemUpdateProcessor sortItemUpdateProcessor, ComponentTargetFindService componentTargetFindService) {
        super(componentQueryMapper, componentRepository, componentJdbcRepository);
        this.componentTargetMapper = componentTargetMapper;
        this.componentItemMapper = componentItemMapper;
        this.viewExtensionQueryService = viewExtensionQueryService;
        this.componentTargetQueryService = componentTargetQueryService;
        this.componentItemQueryService = componentItemQueryService;
        this.sortItemUpdateProcessor = sortItemUpdateProcessor;
        this.componentTargetFindService = componentTargetFindService;
    }

    protected ComponentTarget toComponentTarget(long componentId, SortType sortType, Long tabId){
        return componentTargetMapper.toEntity(componentId, sortType, tabId);
    }
    protected List<ComponentItem> toComponentItems(ComponentTarget componentTarget, List<DisplayProductGroupThumbnail> productGroupThumbnails){
        return componentItemMapper.toEntities(componentTarget, productGroupThumbnails);
    }

    protected ViewExtension saveViewExtension(ViewExtensionDetails viewExtensionDetails){
        return viewExtensionQueryService.saveViewExtension(viewExtensionDetails);
    }

    protected void createComponentTargetAndItem(Map<Long, List<SortItem>> resultMap){
        resultMap.forEach((componentId, sortItems) -> {
            Map<SortType, SortItem> sortItemsByType = sortItems.stream()
                    .collect(Collectors.toMap(SortItem::getSortType, Function.identity()));

            saveComponentTargetAndItems(componentId, sortItemsByType);
        });
    }

    protected void saveComponentTargetAndItems(Long componentId, Map<SortType, SortItem> sortItemsByType) {
        LinkedHashSet<Long> processedProductGroupIds = new LinkedHashSet<>();

        for (SortType sortType : Arrays.asList(SortType.FIXED, SortType.AUTO)) {
            Optional<SortItem> sortItemOpt = Optional.ofNullable(sortItemsByType.get(sortType));
            sortItemOpt.ifPresent(sortItem -> {
                ComponentTarget componentTarget = saveComponentTarget(componentId, sortType, 0L);

                List<DisplayProductGroupThumbnail> productGroups = sortItem.getProductGroups();

                if (sortType.equals(SortType.AUTO)) {
                    productGroups = productGroups.stream()
                            .filter(pg -> !processedProductGroupIds.contains(pg.getProductGroupId()))
                            .collect(Collectors.toList());
                }

                saveComponentItems(componentTarget, productGroups);
                productGroups.forEach(pg -> processedProductGroupIds.add(pg.getProductGroupId()));
            });
        }
    }


    protected void saveComponentTargetAndItems(Long componentId, Map<SortType, SortItem> sortItemsByType, long tabId) {
        LinkedHashSet<Long> processedProductGroupIds = new LinkedHashSet<>();

        for (SortType sortType : Arrays.asList(SortType.FIXED, SortType.AUTO)) {
            Optional<SortItem> sortItemOpt = Optional.ofNullable(sortItemsByType.get(sortType));
            sortItemOpt.ifPresent(sortItem -> {
                ComponentTarget componentTarget = saveComponentTarget(componentId, sortType, tabId);

                List<DisplayProductGroupThumbnail> productGroups = sortItem.getProductGroups();

                if (sortType.equals(SortType.AUTO)) {
                    productGroups = productGroups.stream()
                            .filter(pg -> !processedProductGroupIds.contains(pg.getProductGroupId()))
                            .collect(Collectors.toList());
                }

                saveComponentItems(componentTarget, productGroups);
                productGroups.forEach(pg -> processedProductGroupIds.add(pg.getProductGroupId()));
            });
        }
    }


    protected ComponentTarget saveComponentTarget(Long componentId, SortType sortType, Long tabId) {
        ComponentTarget componentTarget = toComponentTarget(componentId, sortType, tabId);
        return componentTargetQueryService.saveComponentTarget(componentTarget);
    }

    protected void saveComponentItems(ComponentTarget componentTarget, List<DisplayProductGroupThumbnail> productGroups) {
        List<ComponentItem> componentItems = toComponentItems(componentTarget, productGroups);
        componentItemQueryService.saveComponentItems(componentItems);
    }


    protected void updateSubComponentSortItems(SubComponent existingSubcomponent, SubComponent newComponent) {
        SortItemsUpdateResult sortItemsUpdateResult = processSortItemsUpdates(existingSubcomponent, newComponent);
        if(!sortItemsUpdateResult.getAddedItems().isEmpty()){
            insertComponentTargetAndItems(existingSubcomponent, sortItemsUpdateResult.getAddedItems(), null);
        }

        if(!sortItemsUpdateResult.getUpdatedItems().isEmpty()){
            updateComponentTargetAndItems(existingSubcomponent.getComponentId(), sortItemsUpdateResult.getUpdatedItems(), null);
        }

        if(!sortItemsUpdateResult.getDeletedItems().isEmpty()){
            sortItemsUpdateResult.getDeletedItems().forEach((sortType, productGroupThumbnails) -> {
                List<Long> productGroupIds = productGroupThumbnails.stream().map(DisplayProductGroupThumbnail::getProductGroupId).collect(Collectors.toList());
                deleteComponentTargetsAndItems(existingSubcomponent.getComponentId(), sortType, productGroupIds);
            });
        }


    }

    protected void updateTabComponentSortItems(TabComponentDetail tabComponentDetail, TabDetail newTabDetail, TabDetail existingTabDetail) {
        TabUpdateResult tabUpdateResult = processTabUpdates(newTabDetail, existingTabDetail);
        Map<Long, Map<SortType, List<DisplayProductGroupThumbnail>>> addedTabs = tabUpdateResult.getAddedItems();
        Map<Long, Map<SortType, List<DisplayProductGroupThumbnail>>> updatedTabs = tabUpdateResult.getUpdatedItems();
        Map<Long, Map<SortType, List<DisplayProductGroupThumbnail>>> deletedTabs = tabUpdateResult.getDeletedItems();

        if(!addedTabs.isEmpty()){
            for(Long tabId : addedTabs.keySet()){
                Map<SortType, List<DisplayProductGroupThumbnail>> sortTypeListMap = addedTabs.get(tabId);
                insertComponentTargetAndItems(tabComponentDetail, sortTypeListMap, tabId);
            }
        }

        if(!updatedTabs.isEmpty()){
            for(Long tabId : updatedTabs.keySet()){
                Map<SortType, List<DisplayProductGroupThumbnail>> sortTypeListMap = updatedTabs.get(tabId);
                updateComponentTargetAndItems(tabComponentDetail.getComponentId(), sortTypeListMap, tabId);
            }
        }

        if(!deletedTabs.isEmpty()){
            for(Long tabId : deletedTabs.keySet()){
                Map<SortType, List<DisplayProductGroupThumbnail>> sortTypeListMap = deletedTabs.get(tabId);
                    sortTypeListMap.forEach((sortType, productGroupThumbnails) -> {
                        List<Long> productGroupIds = productGroupThumbnails.stream().map(DisplayProductGroupThumbnail::getProductGroupId).collect(Collectors.toList());
                        deleteComponentTargetsAndItems(tabComponentDetail.getComponentId(), sortType, productGroupIds);
                    });
            }
        }

    }

    private SortItemsUpdateResult processSortItemsUpdates(SubComponent existingSubcomponent, SubComponent newComponent) {
        List<Long> removedProductGroupIds = new ArrayList<>();
        List<Pair<SortType, DisplayProductGroupThumbnail>> addedProductPairs = new ArrayList<>();
        List<Pair<SortType, DisplayProductGroupThumbnail>> updatedProductPairs = new ArrayList<>();
        List<Pair<SortType, DisplayProductGroupThumbnail>> deletedProductPairs = new ArrayList<>();

        Map<SortType, List<DisplayProductGroupThumbnail>> addedComponentTargetAndItems = new HashMap<>();
        Map<SortType, List<DisplayProductGroupThumbnail>> updatedComponentTargetAndItems = new HashMap<>();
        Map<SortType, List<DisplayProductGroupThumbnail>> deletedComponentTargetAndItems = new HashMap<>();


        sortItemUpdateProcessor.processUpdates(
                existingSubcomponent,
                newComponent,
                addedProductPairs::add,
                updatedProductPairs::add,
                deletedProductPairs::add
        );


        addedProductPairs.forEach(pair ->
                addedComponentTargetAndItems.computeIfAbsent(pair.getLeft(), k -> new ArrayList<>()).add(pair.getRight())
        );

        updatedProductPairs.forEach(pair ->
                updatedComponentTargetAndItems.computeIfAbsent(pair.getLeft(), k -> new ArrayList<>()).add(pair.getRight())
        );

        deletedProductPairs.forEach(pair ->
                deletedComponentTargetAndItems.computeIfAbsent(pair.getLeft(), k -> new ArrayList<>()).add(pair.getRight())
        );

        return new SortItemsUpdateResult(addedComponentTargetAndItems, updatedComponentTargetAndItems, deletedComponentTargetAndItems);
    }


    protected TabUpdateResult processTabUpdates(TabDetail newTabDetail, TabDetail existingTabDetail) {
        List<Pair<SortType, DisplayProductGroupThumbnail>> addedProductPairs = new ArrayList<>();
        List<Pair<SortType, DisplayProductGroupThumbnail>> updatedProductPairs = new ArrayList<>();
        List<Pair<SortType, DisplayProductGroupThumbnail>> deletedProductPairs = new ArrayList<>();

        Map<Long, Map<SortType, List<DisplayProductGroupThumbnail>>> addedComponentTargetAndItems = new HashMap<>();
        Map<Long, Map<SortType, List<DisplayProductGroupThumbnail>>> updatedComponentTargetAndItems = new HashMap<>();
        Map<Long, Map<SortType, List<DisplayProductGroupThumbnail>>> deletedComponentTargetAndItems = new HashMap<>();


        sortItemUpdateProcessor.processUpdates(
                existingTabDetail,
                newTabDetail,
                addedProductPairs::add,
                updatedProductPairs::add,
                deletedProductPairs::add
        );


        Long tabId = newTabDetail.getTabId();

        addedProductPairs.forEach(pair -> {
            Map<SortType, List<DisplayProductGroupThumbnail>> sortTypeMap =
                    addedComponentTargetAndItems.computeIfAbsent(tabId, k -> new HashMap<>());
            sortTypeMap.computeIfAbsent(pair.getLeft(), k -> new ArrayList<>()).add(pair.getRight());
        });

        updatedProductPairs.forEach(pair -> {
            Map<SortType, List<DisplayProductGroupThumbnail>> sortTypeMap =
                    updatedComponentTargetAndItems.computeIfAbsent(tabId, k -> new HashMap<>());
            sortTypeMap.computeIfAbsent(pair.getLeft(), k -> new ArrayList<>()).add(pair.getRight());
        });


        deletedProductPairs.forEach(pair -> {
            Map<SortType, List<DisplayProductGroupThumbnail>> sortTypeMap =
                    deletedComponentTargetAndItems.computeIfAbsent(tabId, k -> new HashMap<>());
            sortTypeMap.computeIfAbsent(pair.getLeft(), k -> new ArrayList<>()).add(pair.getRight());
        });

        return new TabUpdateResult(addedComponentTargetAndItems, updatedComponentTargetAndItems, deletedComponentTargetAndItems);
    }



    private void insertComponentTargetAndItems(SubComponent subComponent, Map<SortType, List<DisplayProductGroupThumbnail>> addedComponentTargetAndItems, Long tabId){
        for(SortType sortType: addedComponentTargetAndItems.keySet()){
            Optional<ComponentTarget> componentTarget = componentTargetFindService.fetchComponentTarget(subComponent.getComponentId(), sortType, tabId);
            componentTarget.ifPresentOrElse( findComponentTarget ->{
                saveComponentItems(findComponentTarget, addedComponentTargetAndItems.get(sortType));
            },()->{
                ComponentTarget savedComponentTarget = saveComponentTarget(subComponent.getComponentId(), sortType, tabId);
                saveComponentItems(savedComponentTarget, addedComponentTargetAndItems.get(sortType));
            });
        }
    }


    private void updateComponentTargetAndItems(long componentId, Map<SortType, List<DisplayProductGroupThumbnail>> updatedComponentTargetAndItems, Long tabId){

        for(SortType sortType: updatedComponentTargetAndItems.keySet()){
            Optional<ComponentTarget> componentTarget = componentTargetFindService.fetchComponentTarget(componentId, sortType, tabId);
            componentTarget.ifPresentOrElse( findComponentTarget ->{
                componentItemQueryService.updateComponentItems(componentId, findComponentTarget.getId(), updatedComponentTargetAndItems.get(sortType));
            },()->{
                ComponentTarget savedComponentTarget = saveComponentTarget(componentId, sortType, tabId);
                saveComponentItems(savedComponentTarget, updatedComponentTargetAndItems.get(sortType));
            });
        }
    }

    private void deleteComponentTargetsAndItems(long componentId, SortType sortType, List<Long> productGroupIds){
        componentItemQueryService.deleteComponentItems(componentId, sortType, productGroupIds);
    }

    protected Optional<ViewExtensionDetails> extractViewExtension(ViewExtensionDetails newViewExtensionDetails, ViewExtensionDetails existingViewExtensionDetails){
        if(!newViewExtensionDetails.equals(existingViewExtensionDetails)) return Optional.of(newViewExtensionDetails);
        else return Optional.empty();
    }

    protected void updateViewExtensions(List<ViewExtension> viewExtensions){
        viewExtensionQueryService.updateAll(viewExtensions);
    }

}
