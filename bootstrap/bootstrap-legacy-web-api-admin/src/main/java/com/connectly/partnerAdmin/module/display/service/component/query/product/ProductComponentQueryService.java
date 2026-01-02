package com.connectly.partnerAdmin.module.display.service.component.query.product;

import com.connectly.partnerAdmin.module.display.dto.component.ComponentUpdatePair;
import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.component.brand.BrandComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.category.CategoryComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.product.ProductComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.ViewExtension;
import com.connectly.partnerAdmin.module.display.entity.component.sub.product.ProductComponent;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.mapper.ComponentQueryMapper;
import com.connectly.partnerAdmin.module.display.mapper.item.ComponentItemMapper;
import com.connectly.partnerAdmin.module.display.mapper.item.ComponentTargetMapper;
import com.connectly.partnerAdmin.module.display.mapper.product.ProductComponentItemMapper;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentRepository;
import com.connectly.partnerAdmin.module.display.repository.component.product.ProductComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.product.ProductComponentRepository;
import com.connectly.partnerAdmin.module.display.service.component.query.ProductRelatedComponentQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.item.ComponentItemQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.item.ComponentTargetQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.item.SortItemUpdateProcessor;
import com.connectly.partnerAdmin.module.display.service.component.query.item.fetch.ComponentTargetFindService;
import com.connectly.partnerAdmin.module.display.service.component.query.viewExtension.ViewExtensionQueryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class ProductComponentQueryService extends ProductRelatedComponentQueryService<ProductComponentDetail> {

    private final ProductComponentItemMapper productComponentItemMapper;
    private final ProductComponentRepository productComponentRepository;
    private final ProductComponentJdbcRepository productComponentJdbcRepository;

    public ProductComponentQueryService(ComponentQueryMapper componentQueryMapper, ComponentRepository componentRepository, ComponentJdbcRepository componentJdbcRepository, ComponentTargetMapper componentTargetMapper, ComponentItemMapper componentItemMapper, ViewExtensionQueryService viewExtensionQueryService, ComponentTargetQueryService componentTargetQueryService, ComponentItemQueryService componentItemQueryService, SortItemUpdateProcessor sortItemUpdateProcessor, ComponentTargetFindService componentTargetFindService, ProductComponentItemMapper productComponentItemMapper, ProductComponentRepository productComponentRepository, ProductComponentJdbcRepository productComponentJdbcRepository) {
        super(componentQueryMapper, componentRepository, componentJdbcRepository, componentTargetMapper, componentItemMapper, viewExtensionQueryService, componentTargetQueryService, componentItemQueryService, sortItemUpdateProcessor, componentTargetFindService);
        this.productComponentItemMapper = productComponentItemMapper;
        this.productComponentRepository = productComponentRepository;
        this.productComponentJdbcRepository = productComponentJdbcRepository;
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.PRODUCT;
    }

    @Override
    public void createComponents(long contentId, List<ProductComponentDetail> components) {
        Map<Long, List<SortItem>> componentIdToSortItemsMap = mapProductComponentDetailsToSortItems(contentId, components);
        createComponentTargetAndItem(componentIdToSortItemsMap);
    }


    @Override
    public void removeComponents(List<Long> productComponentIds) {
        productComponentJdbcRepository.deleteAll(productComponentIds);
    }

    @Override
    public void updateComponents(List<ComponentUpdatePair<ProductComponentDetail>> components) {
        List<SubComponent> updateProductComponent = new ArrayList<>();
        List<ViewExtension> viewExtensions = new ArrayList<>();

        processComponent(components, updateProductComponent, viewExtensions);

    }

    private void processComponent(List<ComponentUpdatePair<ProductComponentDetail>> components, List<SubComponent> updateProductComponent, List<ViewExtension> viewExtensions){

        for (ComponentUpdatePair<ProductComponentDetail> componentUpdatePair : components) {
            ProductComponentDetail existingComponent = componentUpdatePair.getExistingComponent();
            ProductComponentDetail newComponent = componentUpdatePair.getNewComponent();

            updateSubComponentSortItems(existingComponent, newComponent);

            processForUpdate(updateProductComponent, existingComponent, newComponent);
            processViewExtensions(viewExtensions, existingComponent, newComponent);

            applyComponentUpdates(updateProductComponent, viewExtensions);
        }

    }

    private void processForUpdate(List<SubComponent> updateProductComponent, ProductComponentDetail existingComponent, ProductComponentDetail newComponent){
        if(!existingComponent.equals(newComponent)) updateProductComponent.add(newComponent);
    }

    private void processViewExtensions(List<ViewExtension> viewExtensions, @NotNull ProductComponentDetail existingComponent, ProductComponentDetail newComponent) {
        extractViewExtension(newComponent.getViewExtensionDetails(), existingComponent.getViewExtensionDetails())
                .ifPresent(viewExtensionDetails -> viewExtensions.add(new ViewExtension(newComponent.getViewExtensionId(), viewExtensionDetails)));
    }


    private Map<Long, List<SortItem>> mapProductComponentDetailsToSortItems(long contentId, List<ProductComponentDetail> components) {
        return components.stream()
                .map(productComponentDetail -> {
                    long componentId = saveProductComponent(contentId, productComponentDetail);
                    return new AbstractMap.SimpleEntry<>(componentId, productComponentDetail.getSortItems());

                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    private long saveProductComponent(long contentId, ProductComponentDetail productComponentDetail) {
        ViewExtension viewExtension = saveViewExtension(productComponentDetail.getViewExtensionDetails());
        Component component = saveComponent(contentId, viewExtension, productComponentDetail);
        ProductComponent productComponent = productComponentItemMapper.toEntity(component, productComponentDetail);
        productComponentRepository.save(productComponent);
        return component.getId();
    }

    private void applyComponentUpdates(List<SubComponent> updateProductComponent, List<ViewExtension> viewExtensions) {
        if(!updateProductComponent.isEmpty()) updateSubComponents(updateProductComponent);
        if(!viewExtensions.isEmpty()) updateViewExtensions(viewExtensions);
    }

}
