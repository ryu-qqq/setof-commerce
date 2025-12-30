package com.connectly.partnerAdmin.module.display.service.component.query.title;

import com.connectly.partnerAdmin.module.display.dto.component.ComponentUpdatePair;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.component.title.TitleComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.sub.TitleComponent;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.mapper.ComponentQueryMapper;
import com.connectly.partnerAdmin.module.display.mapper.title.TitleComponentMapper;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentRepository;
import com.connectly.partnerAdmin.module.display.repository.component.title.TitleComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.service.component.query.BaseComponentQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TitleComponentQueryService extends BaseComponentQueryService<TitleComponentDetail> {

    private final TitleComponentJdbcRepository titleComponentJdbcRepository;
    private final TitleComponentMapper titleMapper;

    public TitleComponentQueryService(ComponentQueryMapper componentQueryMapper, ComponentRepository componentRepository, ComponentJdbcRepository componentJdbcRepository, TitleComponentJdbcRepository titleComponentJdbcRepository, TitleComponentMapper titleMapper) {
        super(componentQueryMapper, componentRepository, componentJdbcRepository);
        this.titleComponentJdbcRepository = titleComponentJdbcRepository;
        this.titleMapper = titleMapper;
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.TITLE;
    }

    @Override
    public void createComponents(long contentId, List<TitleComponentDetail> components) {
        List<TitleComponent> titleComponents = components.stream()
                .map(titleComponentDetail -> {
                    Component component = saveComponent(contentId, null, titleComponentDetail);
                    return titleMapper.toEntity(component, titleComponentDetail);
                })
                .collect(Collectors.toList());

        titleComponentJdbcRepository.saveAll(titleComponents);
    }

    @Override
    public void removeComponents(List<Long> subComponentIds) {
        titleComponentJdbcRepository.deleteAll(subComponentIds);
    }

    @Override
    public void updateComponents(List<ComponentUpdatePair<TitleComponentDetail>> components) {

        List<SubComponent> updateTitleComponent = new ArrayList<>();

        for(ComponentUpdatePair<TitleComponentDetail> componentUpdatePair : components){

            TitleComponentDetail existingComponent = componentUpdatePair.getExistingComponent();
            TitleComponentDetail newComponent = componentUpdatePair.getNewComponent();

            extractUpdateTitleComponent(updateTitleComponent, existingComponent, newComponent);

        }

        updateSubComponents(updateTitleComponent);
        updateTitleComponents(updateTitleComponent);
    }


    private void extractUpdateTitleComponent(List<SubComponent> updateProductComponent, TitleComponentDetail existingComponent, TitleComponentDetail newComponent){
        if(!existingComponent.equals(newComponent)) updateProductComponent.add(newComponent);
    }

    private void updateTitleComponents(List<SubComponent> titleComponentDetails){
        List<TitleComponentDetail> titleComponents = titleComponentDetails.stream()
                .filter(item -> (item instanceof TitleComponentDetail))
                .map(item -> (TitleComponentDetail) item)
                .collect(Collectors.toList());

        if(!titleComponents.isEmpty()){
            titleComponentJdbcRepository.updateAll(titleComponents);
        }
    }

}
