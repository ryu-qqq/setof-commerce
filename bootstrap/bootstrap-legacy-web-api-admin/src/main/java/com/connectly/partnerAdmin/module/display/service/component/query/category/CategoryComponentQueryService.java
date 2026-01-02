package com.connectly.partnerAdmin.module.display.service.component.query.category;

import com.connectly.partnerAdmin.module.category.service.CategoryFetchService;
import com.connectly.partnerAdmin.module.display.dto.component.ComponentUpdatePair;
import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.component.category.CategoryComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.ViewExtension;
import com.connectly.partnerAdmin.module.display.entity.component.sub.product.CategoryComponent;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.exception.ContentErrorConstant;
import com.connectly.partnerAdmin.module.display.mapper.ComponentQueryMapper;
import com.connectly.partnerAdmin.module.display.mapper.category.CategoryComponentMapper;
import com.connectly.partnerAdmin.module.display.mapper.item.ComponentItemMapper;
import com.connectly.partnerAdmin.module.display.mapper.item.ComponentTargetMapper;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentRepository;
import com.connectly.partnerAdmin.module.display.repository.component.category.CategoryComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.category.CategoryComponentRepository;
import com.connectly.partnerAdmin.module.display.service.component.query.ProductRelatedComponentQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.item.ComponentItemQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.item.ComponentTargetQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.item.SortItemUpdateProcessor;
import com.connectly.partnerAdmin.module.display.service.component.query.item.fetch.ComponentTargetFindService;
import com.connectly.partnerAdmin.module.display.service.component.query.viewExtension.ViewExtensionQueryService;
import com.connectly.partnerAdmin.module.display.exception.InvalidCategoryOrBrandException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Transactional
@Service
public class CategoryComponentQueryService extends ProductRelatedComponentQueryService<CategoryComponentDetail> {

    private final CategoryFetchService categoryFetchService;
    private final CategoryComponentMapper categoryComponentMapper;
    private final CategoryComponentRepository categoryComponentRepository;
    private final CategoryComponentJdbcRepository categoryComponentJdbcRepository;

    public CategoryComponentQueryService(ComponentQueryMapper componentQueryMapper, ComponentRepository componentRepository, ComponentJdbcRepository componentJdbcRepository, ComponentTargetMapper componentTargetMapper, ComponentItemMapper componentItemMapper, ViewExtensionQueryService viewExtensionQueryService, ComponentTargetQueryService componentTargetQueryService, ComponentItemQueryService componentItemQueryService, SortItemUpdateProcessor sortItemUpdateProcessor, ComponentTargetFindService componentTargetFindService, CategoryFetchService categoryFetchService, CategoryComponentMapper categoryComponentMapper, CategoryComponentRepository categoryComponentRepository, CategoryComponentJdbcRepository categoryComponentJdbcRepository) {
        super(componentQueryMapper, componentRepository, componentJdbcRepository, componentTargetMapper, componentItemMapper, viewExtensionQueryService, componentTargetQueryService, componentItemQueryService, sortItemUpdateProcessor, componentTargetFindService);
        this.categoryFetchService = categoryFetchService;
        this.categoryComponentMapper = categoryComponentMapper;
        this.categoryComponentRepository = categoryComponentRepository;
        this.categoryComponentJdbcRepository = categoryComponentJdbcRepository;
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.CATEGORY;
    }

    @Override
    public void createComponents(long contentId, List<CategoryComponentDetail> components) {
        Map<Long, List<SortItem>> componentIdToSortItemsMap = mapCategoryComponentDetailsToSortItems(contentId, components);
        createComponentTargetAndItem(componentIdToSortItemsMap);
    }

    @Override
    public void removeComponents(List<Long> categoryComponentIds) {
        categoryComponentJdbcRepository.deleteAll(categoryComponentIds);
    }

    @Override
    public void updateComponents(List<ComponentUpdatePair<CategoryComponentDetail>> components) {
        List<SubComponent> updateCategoryComponent = new ArrayList<>();
        List<ViewExtension> viewExtensions = new ArrayList<>();
        List<CategoryComponentDetail> toUpdateCategoryComponents = new ArrayList<>();

        processComponent(components, updateCategoryComponent, viewExtensions, toUpdateCategoryComponents);
        applyComponentUpdates(updateCategoryComponent, viewExtensions, toUpdateCategoryComponents);
    }


    private void processComponent(List<ComponentUpdatePair<CategoryComponentDetail>> components, List<SubComponent> updateCategoryComponent, List<ViewExtension> viewExtensions,
                                  List<CategoryComponentDetail> toUpdateCategoryComponents){

        for (ComponentUpdatePair<CategoryComponentDetail> componentUpdatePair : components) {
            CategoryComponentDetail existingComponent = componentUpdatePair.getExistingComponent();
            CategoryComponentDetail newComponent = componentUpdatePair.getNewComponent();

            updateSubComponentSortItems(existingComponent, newComponent);


            processForUpdate(updateCategoryComponent, existingComponent, newComponent);
            processViewExtensions(viewExtensions, existingComponent, newComponent);
            processSubComponentForUpdate(toUpdateCategoryComponents, existingComponent, newComponent);
        }

    }

    private void processViewExtensions(List<ViewExtension> viewExtensions, @NotNull CategoryComponentDetail existingComponent, CategoryComponentDetail newComponent) {
        extractViewExtension(newComponent.getViewExtensionDetails(), existingComponent.getViewExtensionDetails())
                .ifPresent(viewExtensionDetails -> viewExtensions.add(new ViewExtension(newComponent.getViewExtensionId(), viewExtensionDetails)));
    }

    private void processSubComponentForUpdate(List<CategoryComponentDetail> toUpdateCategoryComponents,
                                           CategoryComponentDetail existingComponent, CategoryComponentDetail newComponent) {
        updateCategory(toUpdateCategoryComponents, existingComponent, newComponent);
    }



    private void applyComponentUpdates(List<SubComponent> updateTabComponent, List<ViewExtension> viewExtensions,
                                       List<CategoryComponentDetail> toUpdateCategoryComponents) {

        if(!updateTabComponent.isEmpty()) updateSubComponents(updateTabComponent);

        if(!viewExtensions.isEmpty()) updateViewExtensions(viewExtensions);

        if(!toUpdateCategoryComponents.isEmpty()) updateCategoryComponents(toUpdateCategoryComponents);
    }


    private void processForUpdate(List<SubComponent> updateCategoryComponent, CategoryComponentDetail existingComponent, CategoryComponentDetail newComponent){
        if(!existingComponent.equals(newComponent)) updateCategoryComponent.add(newComponent);
    }


    private Map<Long, List<SortItem>> mapCategoryComponentDetailsToSortItems(long contentId, List<CategoryComponentDetail> components) {
        return components.stream()
                .map(categoryComponentDetail -> {
                    validateCategory(categoryComponentDetail);
                    long componentId = saveCategoryComponent(contentId, categoryComponentDetail);
                    return new AbstractMap.SimpleEntry<>(componentId, categoryComponentDetail.getSortItems());

                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    private long saveCategoryComponent(long contentId, CategoryComponentDetail categoryComponentDetail) {
        ViewExtension viewExtension = saveViewExtension(categoryComponentDetail.getViewExtensionDetails());
        Component component = saveComponent(contentId, viewExtension, categoryComponentDetail);
        CategoryComponent categoryComponents = categoryComponentMapper.toEntity(component, categoryComponentDetail);
        categoryComponentRepository.save(categoryComponents);
        return component.getId();
    }

    private void validateCategory(CategoryComponentDetail categoryComponentDetail){
        if(!categoryFetchService.hasCategoryIdExist(categoryComponentDetail.getCategoryId())) {
            throw new InvalidCategoryOrBrandException(ContentErrorConstant.INVALID_CATEGORY_ID_MSG);
        }
    }

    private void updateCategory(List<CategoryComponentDetail> toUpdateCategoryComponents, CategoryComponentDetail existingComponent, CategoryComponentDetail newComponent){
        if(existingComponent.getCategoryId() != newComponent.getCategoryId()) toUpdateCategoryComponents.add(newComponent);
    }

    private void updateCategoryComponents(List<CategoryComponentDetail> toUpdateCategoryComponents){
        categoryComponentJdbcRepository.updateAll(toUpdateCategoryComponents);
    }

}
