package com.connectly.partnerAdmin.module.display.service.component.query.tab;

import com.connectly.partnerAdmin.module.display.dto.component.ComponentUpdatePair;
import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.component.tab.TabComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.tab.TabDetail;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.ViewExtension;
import com.connectly.partnerAdmin.module.display.entity.component.item.Tab;
import com.connectly.partnerAdmin.module.display.entity.component.sub.product.TabComponent;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.enums.SortType;
import com.connectly.partnerAdmin.module.display.mapper.ComponentQueryMapper;
import com.connectly.partnerAdmin.module.display.mapper.item.ComponentItemMapper;
import com.connectly.partnerAdmin.module.display.mapper.item.ComponentTargetMapper;
import com.connectly.partnerAdmin.module.display.mapper.tab.TabComponentMapper;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentRepository;
import com.connectly.partnerAdmin.module.display.repository.component.tab.TabComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.tab.TabComponentRepository;
import com.connectly.partnerAdmin.module.display.repository.component.tab.TabJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.tab.TabRepository;
import com.connectly.partnerAdmin.module.display.service.component.query.ProductRelatedComponentQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.item.ComponentItemQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.item.ComponentTargetQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.item.SortItemUpdateProcessor;
import com.connectly.partnerAdmin.module.display.service.component.query.item.fetch.ComponentTargetFindService;
import com.connectly.partnerAdmin.module.display.service.component.query.viewExtension.ViewExtensionQueryService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Transactional
@Service
public class TabComponentQueryService extends ProductRelatedComponentQueryService<TabComponentDetail> {
    private final TabComponentMapper tabComponentMapper;
    private final TabComponentRepository tabComponentRepository;
    private final TabComponentJdbcRepository tabComponentJdbcRepository;
    private final TabJdbcRepository tabJdbcRepository;
    private final TabRepository tabRepository;

    public TabComponentQueryService(ComponentQueryMapper componentQueryMapper, ComponentRepository componentRepository, ComponentJdbcRepository componentJdbcRepository, ComponentTargetMapper componentTargetMapper, ComponentItemMapper componentItemMapper, ViewExtensionQueryService viewExtensionQueryService, ComponentTargetQueryService componentTargetQueryService, ComponentItemQueryService componentItemQueryService, SortItemUpdateProcessor sortItemUpdateProcessor, ComponentTargetFindService componentTargetFindService, TabComponentMapper tabComponentMapper, TabComponentRepository tabComponentRepository, TabComponentJdbcRepository tabComponentJdbcRepository, TabJdbcRepository tabJdbcRepository, TabRepository tabRepository) {
        super(componentQueryMapper, componentRepository, componentJdbcRepository, componentTargetMapper, componentItemMapper, viewExtensionQueryService, componentTargetQueryService, componentItemQueryService, sortItemUpdateProcessor, componentTargetFindService);
        this.tabComponentMapper = tabComponentMapper;
        this.tabComponentRepository = tabComponentRepository;
        this.tabComponentJdbcRepository = tabComponentJdbcRepository;
        this.tabJdbcRepository = tabJdbcRepository;
        this.tabRepository = tabRepository;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TAB;
    }

    @Override
    public void createComponents(long contentId, List<TabComponentDetail> components) {
        List<Map<Long, List<TabDetail>>> componentIdToSortItemsListMap = mapTabComponentDetailsToSortItems(contentId, components);
        createComponentTargetAndItem(componentIdToSortItemsListMap);
    }

    @Override
    public void removeComponents(List<Long> tabComponentIds) {
        deleteTabWitTabComponentIds(tabComponentIds);
        tabComponentJdbcRepository.deleteAll(tabComponentIds);
    }

    private void deleteTabWitTabComponentIds(List<Long> tabComponentIds){
        tabJdbcRepository.deleteAllWithTabComponentIds(tabComponentIds);
    }

    private void deleteTabWitTabIds(List<Long> tabIds){
        tabJdbcRepository.deleteAll(tabIds);
    }

    @Override
    public void updateComponents(List<ComponentUpdatePair<TabComponentDetail>> components) {
        List<SubComponent> updateTabComponent = new ArrayList<>();
        List<ViewExtension> viewExtensions = new ArrayList<>();
        List<Map<TabComponentDetail, Pair<TabDetail, TabDetail>>> toUpdateTabDetails = new ArrayList<>();
        List<Long> removedTabIds = new ArrayList<>();

        processComponentPairs(components, updateTabComponent, viewExtensions, removedTabIds, toUpdateTabDetails);
        applyComponentUpdates(updateTabComponent, removedTabIds, viewExtensions, toUpdateTabDetails);
    }

    private void processComponentPairs(List<ComponentUpdatePair<TabComponentDetail>> components, List<SubComponent> updateTabComponent,
                                       List<ViewExtension> viewExtensions, List<Long> removedTabIds,
                                       List<Map<TabComponentDetail, Pair<TabDetail, TabDetail>>> toUpdateTabDetails) {

        for (ComponentUpdatePair<TabComponentDetail> componentUpdatePair : components) {
            TabComponentDetail existingComponent = componentUpdatePair.getExistingComponent();
            TabComponentDetail newComponent = componentUpdatePair.getNewComponent();

            processTabUpdates(updateTabComponent, existingComponent, newComponent, removedTabIds, toUpdateTabDetails);
            extractAndUpdateViewExtensions(newComponent, existingComponent, viewExtensions);
        }
    }

    private void applyComponentUpdates(List<SubComponent> updateTabComponent, List<Long> removedTabIds, List<ViewExtension> viewExtensions, List<Map<TabComponentDetail, Pair<TabDetail, TabDetail>>> toUpdateTabDetails) {

        if (!updateTabComponent.isEmpty()) updateSubComponents(updateTabComponent);

        if (!removedTabIds.isEmpty()) deleteTabWitTabIds(removedTabIds);

        if (!viewExtensions.isEmpty()) updateViewExtensions(viewExtensions);

        if (!toUpdateTabDetails.isEmpty()) updateTabDetails(toUpdateTabDetails);

    }

    private void extractAndUpdateViewExtensions(TabComponentDetail newComponent, TabComponentDetail existingComponent, List<ViewExtension> viewExtensions) {
        extractViewExtension(newComponent.getViewExtensionDetails(), existingComponent.getViewExtensionDetails())
                .ifPresent(viewExtensionDetails -> viewExtensions.add(new ViewExtension(newComponent.getViewExtensionId(), viewExtensionDetails)));
    }

    private void updateTabDetails(List<Map<TabComponentDetail, Pair<TabDetail, TabDetail>>> toUpdateTabDetails) {
        for (Map<TabComponentDetail, Pair<TabDetail, TabDetail>> tupleTabDetailMap : toUpdateTabDetails) {
            tupleTabDetailMap.forEach((tabComponentDetail, tabDetailPair) ->{
                TabDetail newTab = tabDetailPair.getLeft();
                tabComponentJdbcRepository.update(tabComponentDetail.getTabComponentId(), newTab);
                tabJdbcRepository.update(newTab);
                updateTabComponentSortItems(tabComponentDetail, newTab, tabDetailPair.getRight());
            });
        }
    }



    private void processTabUpdates(List<SubComponent> updateTabComponent, TabComponentDetail existingComponent, TabComponentDetail newComponent,
            List<Long> removedTabIds, List<Map<TabComponentDetail, Pair<TabDetail, TabDetail>>> toUpdateTabDetails) {

        if (!existingComponent.equals(newComponent)) {
            processNewAndExistingTabDetails(
                    existingComponent.getTabDetails(),
                    newComponent.getTabDetails(),
                    newComponent,
                    removedTabIds,
                    toUpdateTabDetails
            );
            updateTabComponent.add(newComponent);
        }
    }

    private void processNewAndExistingTabDetails(List<TabDetail> existingTabDetails, List<TabDetail> newTabDetails, TabComponentDetail newComponent,
                                                 List<Long> removedTabIds, List<Map<TabComponentDetail, Pair<TabDetail, TabDetail>>> toUpdateTabDetails) {

        if (!existingTabDetails.equals(newTabDetails)) {
            addNewTabs(newTabDetails, newComponent);
            removeObsoleteTabs(existingTabDetails, newTabDetails, removedTabIds);
            updateExistingTabs(existingTabDetails, newTabDetails, newComponent, toUpdateTabDetails);
        }
    }

    private void addNewTabs(List<TabDetail> newTabDetails, TabComponentDetail newComponent) {
        List<TabDetail> addedTabs = newTabDetails.stream()
                .filter(tabDetail -> tabDetail.getTabId() == null)
                .collect(Collectors.toList());

        if (!addedTabs.isEmpty()) {
            List<Tab> savedTabs = saveTabs(newComponent.getTabComponentId(), addedTabs);
            updateTabDetailsWithSavedTabs(addedTabs, savedTabs);
            saveComponentTargetAndItems(newComponent.getComponentId(), addedTabs);
        }
    }

    private void removeObsoleteTabs(List<TabDetail> existingTabDetails, List<TabDetail> newTabDetails, List<Long> removedTabIds) {

        Set<Long> existingTabIds = existingTabDetails.stream()
                .map(TabDetail::getTabId)
                .collect(Collectors.toSet());

        Set<Long> newTabIds = newTabDetails.stream()
                .map(TabDetail::getTabId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingTabIds.removeAll(newTabIds);
        removedTabIds.addAll(existingTabIds);
    }

    private void updateExistingTabs(List<TabDetail> existingTabDetails, List<TabDetail> newTabDetails, TabComponentDetail newComponent, List<Map<TabComponentDetail, Pair<TabDetail, TabDetail>>> toUpdateTabDetails) {

        Map<Long, TabDetail> existingTabMap = existingTabDetails.stream()
                .collect(Collectors.toMap(TabDetail::getTabId, Function.identity(), (v1, v2) -> v1));

        Map<Long, TabDetail> newTabMap = newTabDetails.stream()
                .filter(tabDetail -> tabDetail.getTabId() != null)
                .collect(Collectors.toMap(TabDetail::getTabId, Function.identity(), (v1, v2) -> v1));

        newTabMap.forEach((tabId, newTabDetail) -> {
            if (existingTabMap.containsKey(tabId) && !newTabDetail.equals(existingTabMap.get(tabId))) {
                Map<TabComponentDetail, Pair<TabDetail, TabDetail>> tabUpdates = new HashMap<>();
                tabUpdates.put(newComponent, Pair.of(newTabDetail, existingTabMap.get(tabId)));
                toUpdateTabDetails.add(tabUpdates);
            }
        });
    }


    private List<Map<Long, List<TabDetail>>> mapTabComponentDetailsToSortItems(long contentId, List<TabComponentDetail> components) {
        return components.stream()
                .map(component -> saveTabComponent(contentId, component))
                .collect(Collectors.toList());
    }

    private Map<Long, List<TabDetail>> saveTabComponent(long contentId, TabComponentDetail detail) {
        ViewExtension viewExtension = saveViewExtension(detail.getViewExtensionDetails());
        Component component = saveComponent(contentId, viewExtension, detail);
        TabComponent tabComponent = tabComponentMapper.toEntity(component, detail);

        Map<Long, List<TabDetail>> result = new HashMap<>();

        TabComponent savedTabComponent = tabComponentRepository.save(tabComponent);
        List<Tab> savedTabs = saveTabs(savedTabComponent.getId(), detail.getTabDetails());
        updateTabDetailsWithSavedTabs(detail.getTabDetails(), savedTabs);
        result.put(component.getId(), detail.getTabDetails());

        return result;
    }

    private List<Tab> saveTabs(long tabComponentId, List<TabDetail> tabDetails) {
        List<Tab> tabs = tabComponentMapper.toTabEntities(tabComponentId, tabDetails);
        return tabRepository.saveAll(tabs);
    }

    private void updateTabDetailsWithSavedTabs(List<TabDetail> tabDetails, List<Tab> savedTabs) {
        savedTabs.sort(Comparator.comparingInt(Tab::getDisplayOrder));
        tabDetails.sort(Comparator.comparingInt(TabDetail::getDisplayOrder));

        IntStream.range(0, savedTabs.size())
                .filter(i -> i < tabDetails.size())
                .forEach(i -> tabDetails.get(i).setTabId(savedTabs.get(i).getId()));
    }

    public void createComponentTargetAndItem(List<Map<Long, List<TabDetail>>> listMap) {
        listMap.forEach(map -> map.forEach(this::saveComponentTargetAndItems));
    }

    private void saveComponentTargetAndItems(Long componentId, List<TabDetail> tabDetails) {
        tabDetails.forEach(tabDetail -> processTabDetail(componentId, tabDetail));
    }

    private void processTabDetail(Long componentId, TabDetail tabDetail) {
        Map<SortType, SortItem> sortItemsByType = tabDetail.getSortItems().stream()
                .collect(Collectors.toMap(SortItem::getSortType, Function.identity()));

        saveComponentTargetAndItems(componentId, sortItemsByType, tabDetail.getTabId());
    }


}
