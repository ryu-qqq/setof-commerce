package com.connectly.partnerAdmin.module.display.service.component.query.image;

import com.connectly.partnerAdmin.module.display.dto.component.ComponentUpdatePair;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.component.image.ImageComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.image.ImageComponentLink;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.item.ImageComponentItem;
import com.connectly.partnerAdmin.module.display.entity.component.sub.ImageComponent;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.mapper.ComponentQueryMapper;
import com.connectly.partnerAdmin.module.display.mapper.image.ImageComponentMapper;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentRepository;
import com.connectly.partnerAdmin.module.display.repository.component.image.ImageComponentItemJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.image.ImageComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.image.ImageComponentRepository;
import com.connectly.partnerAdmin.module.display.service.component.query.BaseComponentQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Transactional
@Service
public class ImageComponentQueryService extends BaseComponentQueryService<ImageComponentDetail> {

    private final ImageComponentMapper imageComponentMapper;
    private final ImageComponentRepository imageComponentRepository;
    private final ImageComponentJdbcRepository imageComponentJdbcRepository;
    private final ImageComponentItemJdbcRepository imageComponentItemJdbcRepository;

    public ImageComponentQueryService(ComponentQueryMapper componentQueryMapper, ComponentRepository componentRepository, ComponentJdbcRepository componentJdbcRepository, ImageComponentMapper imageComponentMapper, ImageComponentRepository imageComponentRepository, ImageComponentJdbcRepository imageComponentJdbcRepository, ImageComponentItemJdbcRepository imageComponentItemJdbcRepository) {
        super(componentQueryMapper, componentRepository, componentJdbcRepository);
        this.imageComponentMapper = imageComponentMapper;
        this.imageComponentRepository = imageComponentRepository;
        this.imageComponentJdbcRepository = imageComponentJdbcRepository;
        this.imageComponentItemJdbcRepository = imageComponentItemJdbcRepository;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.IMAGE;
    }

    @Override
    public void createComponents(long contentId, List<ImageComponentDetail> components) {

        components.forEach(
                imageComponentDetail -> {
                    Component component = saveComponent(contentId, null, imageComponentDetail);
                    ImageComponent imageComponent = imageComponentMapper.toEntity(component, imageComponentDetail);
                    ImageComponent savedImageComponent = imageComponentRepository.save(imageComponent);
                    saveImageComponentItems(savedImageComponent, imageComponentDetail.getImageComponentLinks());
                });

    }

    @Override
    public void removeComponents(List<Long> imageComponentIds) {
        imageComponentJdbcRepository.deleteAll(imageComponentIds);
        imageComponentItemJdbcRepository.deleteAll(imageComponentIds);
    }

    @Override
    public void updateComponents(List<ComponentUpdatePair<ImageComponentDetail>> components) {
        List<SubComponent> updateImageComponent = new ArrayList<>();
        List<ImageComponentLink> imageComponentLinksToProcess = new ArrayList<>();
        List<ImageComponentLink> imageComponentLinksToDelete = new ArrayList<>();

        for (ComponentUpdatePair<ImageComponentDetail> componentUpdatePair : components) {
            ImageComponentDetail existingComponent = componentUpdatePair.getExistingComponent();
            ImageComponentDetail newComponent = componentUpdatePair.getNewComponent();
            if (!existingComponent.equals(newComponent)) {
                newComponent.setImageComponentId();
                updateImageComponent.add(newComponent);
                imageComponentLinksToDelete.addAll(existingComponent.getImageComponentLinks());
                imageComponentLinksToProcess.addAll(newComponent.getImageComponentLinks());
            }
        }

        updateSubComponents(updateImageComponent);
        processImageComponentLinks(imageComponentLinksToDelete, imageComponentLinksToProcess);
    }

    private void processImageComponentLinks(List<ImageComponentLink> imageComponentLinksToDelete, List<ImageComponentLink> imageComponentLinks) {
        List<ImageComponentItem> imageComponentItemsToUpdate = new ArrayList<>();
        List<ImageComponentItem> imageComponentItemsToAdd = new ArrayList<>();

        Map<Long, ImageComponentLink> existingMap = getImageComponentLinkMap(imageComponentLinksToDelete);

        for (ImageComponentLink link : imageComponentLinks) {
            ImageComponentItem item = imageComponentMapper.toImageComponentItem(link);
            if (link.getImageComponentItemId() == null) {
                imageComponentItemsToAdd.add(item);
            }

            ImageComponentLink imageComponentLink = existingMap.get(link.getImageComponentItemId());
            if(imageComponentLink != null){
                    imageComponentItemsToUpdate.add(item);
                    existingMap.remove(link.getImageComponentItemId());
            }

        }

        if (!imageComponentItemsToAdd.isEmpty()) {
            imageComponentItemJdbcRepository.saveAll(imageComponentItemsToAdd);
        }

        if (!imageComponentItemsToUpdate.isEmpty()) {
            imageComponentItemJdbcRepository.updateAll(imageComponentItemsToUpdate);
        }

        if(!existingMap.isEmpty()){
            ArrayList<Long> imageComponentItemIds = new ArrayList<>(existingMap.keySet());
            imageComponentItemJdbcRepository.deleteAll(imageComponentItemIds);
        }

    }

    private Map<Long, ImageComponentLink> getImageComponentLinkMap(List<ImageComponentLink> links) {
        return links.stream()
                .collect(Collectors.toMap(ImageComponentLink::getImageComponentItemId, Function.identity(), (existing, replacement) -> existing));
    }


    private void saveImageComponentItems(ImageComponent savedImageComponent, List<ImageComponentLink> updateImageComponentLinks){
        List<ImageComponentItem> updateImageComponentItems = imageComponentMapper.toImageComponentItem(savedImageComponent.getId(), updateImageComponentLinks);
        imageComponentItemJdbcRepository.saveAll(updateImageComponentItems);
    }

}
