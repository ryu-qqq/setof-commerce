package com.connectly.partnerAdmin.module.display.service.component.query.brand;

import com.connectly.partnerAdmin.module.brand.core.BaseBrandContext;
import com.connectly.partnerAdmin.module.brand.service.BrandFetchService;
import com.connectly.partnerAdmin.module.category.service.CategoryFetchService;
import com.connectly.partnerAdmin.module.display.dto.component.ComponentUpdatePair;
import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.component.brand.BrandComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.ViewExtension;
import com.connectly.partnerAdmin.module.display.entity.component.item.BrandComponentItem;
import com.connectly.partnerAdmin.module.display.entity.component.sub.product.BrandComponent;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.exception.ContentErrorConstant;
import com.connectly.partnerAdmin.module.display.mapper.ComponentQueryMapper;
import com.connectly.partnerAdmin.module.display.mapper.brand.BrandComponentMapper;
import com.connectly.partnerAdmin.module.display.mapper.item.ComponentItemMapper;
import com.connectly.partnerAdmin.module.display.mapper.item.ComponentTargetMapper;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentRepository;
import com.connectly.partnerAdmin.module.display.repository.component.brand.BrandComponentItemJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.brand.BrandComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.brand.BrandComponentRepository;
import com.connectly.partnerAdmin.module.display.service.component.query.ProductRelatedComponentQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.item.ComponentItemQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.item.ComponentTargetQueryService;
import com.connectly.partnerAdmin.module.display.service.component.query.item.SortItemUpdateProcessor;
import com.connectly.partnerAdmin.module.display.service.component.query.item.fetch.ComponentTargetFindService;
import com.connectly.partnerAdmin.module.display.service.component.query.viewExtension.ViewExtensionQueryService;
import com.connectly.partnerAdmin.module.display.exception.InvalidCategoryOrBrandException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class BrandComponentQueryService extends ProductRelatedComponentQueryService<BrandComponentDetail> {
    private final BrandFetchService brandFetchService;
    private final CategoryFetchService categoryFetchService;
    private final BrandComponentMapper brandComponentMapper;
    private final BrandComponentJdbcRepository brandComponentJdbcRepository;
    private final BrandComponentItemJdbcRepository brandComponentItemJdbcRepository;
    private final BrandComponentRepository brandComponentRepository;

    public BrandComponentQueryService(ComponentQueryMapper componentQueryMapper, ComponentRepository componentRepository, ComponentJdbcRepository componentJdbcRepository, ComponentTargetMapper componentTargetMapper, ComponentItemMapper componentItemMapper, ViewExtensionQueryService viewExtensionQueryService, ComponentTargetQueryService componentTargetQueryService, ComponentItemQueryService componentItemQueryService, SortItemUpdateProcessor sortItemUpdateProcessor, ComponentTargetFindService componentTargetFindService, BrandFetchService brandFetchService, CategoryFetchService categoryFetchService, BrandComponentMapper brandComponentMapper, BrandComponentJdbcRepository brandComponentJdbcRepository, BrandComponentItemJdbcRepository brandComponentItemJdbcRepository, BrandComponentRepository brandComponentRepository) {
        super(componentQueryMapper, componentRepository, componentJdbcRepository, componentTargetMapper, componentItemMapper, viewExtensionQueryService, componentTargetQueryService, componentItemQueryService, sortItemUpdateProcessor, componentTargetFindService);
        this.brandFetchService = brandFetchService;
        this.categoryFetchService = categoryFetchService;
        this.brandComponentMapper = brandComponentMapper;
        this.brandComponentJdbcRepository = brandComponentJdbcRepository;
        this.brandComponentItemJdbcRepository = brandComponentItemJdbcRepository;
        this.brandComponentRepository = brandComponentRepository;
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.BRAND;
    }

    @Override
    public void createComponents(long contentId, List<BrandComponentDetail> components) {
        Map<Long, List<SortItem>> componentIdToSortItemsMap = mapBrandComponentDetailsToSortItems(contentId, components);
        createComponentTargetAndItem(componentIdToSortItemsMap);
    }


    private Map<Long, List<SortItem>> mapBrandComponentDetailsToSortItems(long contentId, List<BrandComponentDetail> components) {
        return components.stream()
                .map(brandComponentDetail -> {
                    validateBrandAndCategory(brandComponentDetail);
                    long componentId = saveBrandComponent(contentId, brandComponentDetail);
                    return new AbstractMap.SimpleEntry<>(componentId, brandComponentDetail.getSortItems());
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private long saveBrandComponent(long contentId, BrandComponentDetail brandComponentDetail) {
        ViewExtension viewExtension = saveViewExtension(brandComponentDetail.getViewExtensionDetails());
        Component component = saveComponent(contentId, viewExtension, brandComponentDetail);
        BrandComponent brandComponent = brandComponentMapper.toEntity(component);
        BrandComponent saveBrandComponent = brandComponentRepository.save(brandComponent);
        List<BrandComponentItem> brandComponentItems = brandComponentMapper.toBrandComponentItems(saveBrandComponent.getId(), brandComponentDetail);
        brandComponentItemJdbcRepository.saveAll(brandComponentItems);

        return component.getId();
    }


    private void validateBrandAndCategory(BrandComponentDetail brandComponentDetail){
        if(brandComponentDetail.getCategoryId() != null && !categoryFetchService.hasCategoryIdExist(brandComponentDetail.getCategoryId())) {
            throw new InvalidCategoryOrBrandException(ContentErrorConstant.INVALID_CATEGORY_ID_MSG);
        }

        for(Long brandId : brandComponentDetail.getBrandIdList()) {
            if(!brandFetchService.hasBrandIdExist(brandId)) {
                throw new InvalidCategoryOrBrandException(ContentErrorConstant.INVALID_BRAND_ID_MSG + " " + ContentErrorConstant.INVALID_CATEGORY_ID_MSG);
            }
        }
    }

    @Override
    public void removeComponents(List<Long> brandComponentIds) {
        brandComponentJdbcRepository.deleteAll(brandComponentIds);
    }

    @Override
    public void updateComponents(List<ComponentUpdatePair<BrandComponentDetail>> components) {
        List<SubComponent> updateBrandComponent = new ArrayList<>();
        List<BrandComponentDetail> toAddBrandComponentItems = new ArrayList<>();
        List<BrandComponentDetail> toDeleteBrandComponentItems = new ArrayList<>();
        List<BrandComponentDetail> toUpdateCategoryIdBrandComponentItems = new ArrayList<>();
        List<ViewExtension> viewExtensions = new ArrayList<>();

        processComponentPairs(components, updateBrandComponent, viewExtensions, toAddBrandComponentItems, toDeleteBrandComponentItems, toUpdateCategoryIdBrandComponentItems);
        applyComponentUpdates(updateBrandComponent, viewExtensions, toAddBrandComponentItems, toDeleteBrandComponentItems, toUpdateCategoryIdBrandComponentItems);

    }

    private void processComponentPairs(List<ComponentUpdatePair<BrandComponentDetail>> components, List<SubComponent> updateBrandComponent,
                                       List<ViewExtension> viewExtensions, List<BrandComponentDetail> toAddBrandComponentItems,
                                       List<BrandComponentDetail> toDeleteBrandComponentItems, List<BrandComponentDetail> toUpdateCategoryIdBrandComponentItems) {

        for (ComponentUpdatePair<BrandComponentDetail> componentUpdatePair : components) {
            BrandComponentDetail existingComponent = componentUpdatePair.getExistingComponent();
            BrandComponentDetail newComponent = componentUpdatePair.getNewComponent();

            updateSubComponentSortItems(existingComponent, newComponent);

            processForUpdate(updateBrandComponent, existingComponent, newComponent);
            processViewExtensions(viewExtensions, existingComponent, newComponent);
            processSubComponentForUpdate(toAddBrandComponentItems, toDeleteBrandComponentItems, toUpdateCategoryIdBrandComponentItems, existingComponent, newComponent);
        }
    }

    private void processForUpdate(List<SubComponent> updateBrandComponent, BrandComponentDetail existingComponent, BrandComponentDetail newComponent) {
        if (!existingComponent.equals(newComponent)) {
            updateBrandComponent.add(newComponent);
        }
    }

    private void processViewExtensions(List<ViewExtension> viewExtensions, BrandComponentDetail existingComponent, BrandComponentDetail newComponent) {
        extractViewExtension(newComponent.getViewExtensionDetails(), existingComponent.getViewExtensionDetails())
                .ifPresent(viewExtensionDetails -> viewExtensions.add(new ViewExtension(newComponent.getViewExtensionId(), viewExtensionDetails)));
    }

    private void processSubComponentForUpdate(List<BrandComponentDetail> toAddBrandComponentItems,
                                           List<BrandComponentDetail> toDeleteBrandComponentItems,
                                           List<BrandComponentDetail> toUpdateCategoryIdBrandComponentItems,
                                           BrandComponentDetail existingComponent, BrandComponentDetail newComponent) {


        deleteBrand(existingComponent, newComponent, toDeleteBrandComponentItems);
        addNewBrand(existingComponent, newComponent, toAddBrandComponentItems);
        updateCategory(existingComponent, newComponent, toUpdateCategoryIdBrandComponentItems);
    }



    private void applyComponentUpdates(List<SubComponent> updateTabComponent, List<ViewExtension> viewExtensions,
                                       List<BrandComponentDetail> toAddBrandComponentItems, List<BrandComponentDetail> toDeleteBrandComponentItems,
                                       List<BrandComponentDetail> toUpdateCategoryIdBrandComponentItems) {

        if(!updateTabComponent.isEmpty()) updateSubComponents(updateTabComponent);

        if(!viewExtensions.isEmpty()) updateViewExtensions(viewExtensions);

        if(!toDeleteBrandComponentItems.isEmpty()) deleteBrandComponentItems(toDeleteBrandComponentItems);

        if(!toAddBrandComponentItems.isEmpty()) addNewBrandComponentItems(toAddBrandComponentItems);

        if(!toUpdateCategoryIdBrandComponentItems.isEmpty()) updateCategoryIdBrandComponentItems(toAddBrandComponentItems);

    }



    private void deleteBrand(BrandComponentDetail existingComponent, BrandComponentDetail newComponent, List<BrandComponentDetail> deleteBrandComponentItems){
        if(newComponent.getBrandComponentId() != null){
            BrandComponentDetail modifiedComponent = new BrandComponentDetail(newComponent);

            Set<Long> existingBrandIds = new HashSet<>(existingComponent.getBrandIdList());
            Set<Long> newBrandIds = new HashSet<>(modifiedComponent.getBrandIdList());

            existingBrandIds.removeAll(newBrandIds);
            Map<Long, BaseBrandContext> brandList = existingComponent.getBrands();

            List<BaseBrandContext> existingBrands = existingBrandIds
                    .stream()
                    .filter(id -> id > 0)
                    .map(brandList::get)
                    .collect(Collectors.toList());


            modifiedComponent.setBrandList(existingBrands);
            deleteBrandComponentItems.add(modifiedComponent);
        }
    }

    private void addNewBrand(BrandComponentDetail existingComponent, BrandComponentDetail newComponent, List<BrandComponentDetail> updateBrandComponentItems){
        if(newComponent.getBrandComponentId() != null){
            BrandComponentDetail modifiedComponent = new BrandComponentDetail(newComponent);

            Set<Long> existingBrandIds = new HashSet<>(existingComponent.getBrandIdList());
            Set<Long> newBrandIds = new HashSet<>(newComponent.getBrandIdList());

            newBrandIds.removeAll(existingBrandIds);
            Map<Long, BaseBrandContext> brandList = existingComponent.getBrands();

            List<BaseBrandContext> newBrands = newBrandIds
                    .stream()
                    .filter(id -> id > 0)
                    .map( aLong -> new BaseBrandContext(aLong, ""))
                    .collect(Collectors.toList());

            modifiedComponent.setBrandList(newBrands);
            updateBrandComponentItems.add(modifiedComponent);
        }
    }

    private void updateCategory(BrandComponentDetail existingComponent, BrandComponentDetail newComponent, List<BrandComponentDetail> updateBrandComponentItems){
        if(newComponent.getBrandComponentId() != null){
            if(!Objects.equals(existingComponent.getCategoryId(), newComponent.getCategoryId()) && newComponent.getCategoryId() != null){
                updateBrandComponentItems.add(newComponent);
            }
        }
    }


    private void deleteBrandComponentItems(List<BrandComponentDetail> deleteBrandComponentItems){
        brandComponentItemJdbcRepository.deleteAll(deleteBrandComponentItems);
    }

    private void addNewBrandComponentItems(List<BrandComponentDetail> toAddBrandComponentItems){
        brandComponentItemJdbcRepository.addAll(toAddBrandComponentItems);
    }

    private void updateCategoryIdBrandComponentItems(List<BrandComponentDetail> toUpdateBrandComponentItems){
        brandComponentItemJdbcRepository.updateCategoryIdAll(toUpdateBrandComponentItems);
    }


}
