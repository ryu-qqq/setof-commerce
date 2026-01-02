package com.connectly.partnerAdmin.module.display.service.component.query.blank;

import com.connectly.partnerAdmin.module.display.dto.component.ComponentUpdatePair;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.component.blank.BlankComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.text.TextComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.sub.BlankComponent;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.mapper.ComponentQueryMapper;
import com.connectly.partnerAdmin.module.display.mapper.blank.BlankComponentMapper;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentRepository;
import com.connectly.partnerAdmin.module.display.repository.component.blank.BlankComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.service.component.query.BaseComponentQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Transactional
@Service
public class BlankComponentQueryService extends BaseComponentQueryService<BlankComponentDetail> {

    private final BlankComponentMapper blankMapper;
    private final BlankComponentJdbcRepository blankComponentJdbcRepository;

    public BlankComponentQueryService(ComponentQueryMapper componentQueryMapper, ComponentRepository componentRepository, ComponentJdbcRepository componentJdbcRepository, BlankComponentMapper blankMapper, BlankComponentJdbcRepository blankComponentJdbcRepository) {
        super(componentQueryMapper, componentRepository, componentJdbcRepository);
        this.blankMapper = blankMapper;
        this.blankComponentJdbcRepository = blankComponentJdbcRepository;
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.BLANK;
    }

    @Override
    public void createComponents(long contentId, List<BlankComponentDetail> components) {
        List<BlankComponent> blankComponents = components.stream()
                .map(blankComponentDetail -> {
                    Component component = saveComponent(contentId, null, blankComponentDetail);
                    return blankMapper.toEntity(component, blankComponentDetail);
                })
                .collect(Collectors.toList());

        blankComponentJdbcRepository.saveAll(blankComponents);
    }

    @Override
    public void removeComponents(List<Long> componentIds) {
        blankComponentJdbcRepository.deleteAll(componentIds);
    }


    @Override
    public void updateComponents(List<ComponentUpdatePair<BlankComponentDetail>> components) {
        List<SubComponent> updateBlankComponent = new ArrayList<>();


        for(ComponentUpdatePair<BlankComponentDetail> componentUpdatePair : components){

            BlankComponentDetail existingComponent = componentUpdatePair.getExistingComponent();
            BlankComponentDetail newComponent = componentUpdatePair.getNewComponent();

            extractUpdateBlankComponent(updateBlankComponent, existingComponent, newComponent);

        }

        updateSubComponents(updateBlankComponent);
        updateBlankComponents(updateBlankComponent);

    }

    private void extractUpdateBlankComponent(List<SubComponent> updateProductComponent, BlankComponentDetail existingComponent, BlankComponentDetail newComponent){
        if(!existingComponent.equals(newComponent)) updateProductComponent.add(newComponent);
    }


    private void updateBlankComponents(List<SubComponent> blankComponentDetails){
        List<BlankComponentDetail> blankComponents = blankComponentDetails.stream()
                .filter(item -> (item instanceof BlankComponentDetail))
                .map(item -> (BlankComponentDetail) item)
                .collect(Collectors.toList());

        if(!blankComponents.isEmpty()){
            blankComponentJdbcRepository.updateAll(blankComponents);
        }
    }



}
