package com.connectly.partnerAdmin.module.display.service.component.query.text;

import com.connectly.partnerAdmin.module.display.dto.component.ComponentUpdatePair;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.component.text.TextComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.title.TitleComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.sub.TextComponent;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.mapper.ComponentQueryMapper;
import com.connectly.partnerAdmin.module.display.mapper.text.TextComponentMapper;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentRepository;
import com.connectly.partnerAdmin.module.display.repository.component.text.TextComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.service.component.query.BaseComponentQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Transactional
@Service
public class TextComponentQueryService extends BaseComponentQueryService<TextComponentDetail> {

    private final TextComponentMapper textMapper;
    private final TextComponentJdbcRepository textComponentJdbcRepository;

    public TextComponentQueryService(ComponentQueryMapper componentQueryMapper, ComponentRepository componentRepository, ComponentJdbcRepository componentJdbcRepository, TextComponentMapper textMapper, TextComponentJdbcRepository textComponentJdbcRepository) {
        super(componentQueryMapper, componentRepository, componentJdbcRepository);
        this.textMapper = textMapper;
        this.textComponentJdbcRepository = textComponentJdbcRepository;
    }


    @Override
    public ComponentType getComponentType() {
        return ComponentType.TEXT;
    }

    @Override
    public void createComponents(long contentId, List<TextComponentDetail> components) {

        List<TextComponent> textComponents = components.stream()
                .map(textComponentDetail -> {
                    Component component = saveComponent(contentId, null, textComponentDetail);
                    return textMapper.toEntity(component, textComponentDetail);
                })
                .collect(Collectors.toList());

        textComponentJdbcRepository.saveAll(textComponents);
    }

    @Override
    public void removeComponents(List<Long> componentIds) {
        textComponentJdbcRepository.deleteAll(componentIds);
    }

    @Override
    public void updateComponents(List<ComponentUpdatePair<TextComponentDetail>> components) {

        List<SubComponent> updateTextComponent = new ArrayList<>();

        for(ComponentUpdatePair<TextComponentDetail> componentUpdatePair : components){

            TextComponentDetail existingComponent = componentUpdatePair.getExistingComponent();
            TextComponentDetail newComponent = componentUpdatePair.getNewComponent();

            extractUpdateTextComponent(updateTextComponent, existingComponent, newComponent);

        }

        updateSubComponents(updateTextComponent);
        updateTextComponents(updateTextComponent);
    }


    private void extractUpdateTextComponent(List<SubComponent> updateProductComponent, TextComponentDetail existingComponent, TextComponentDetail newComponent){
        if(!existingComponent.equals(newComponent)) updateProductComponent.add(newComponent);
    }


    private void updateTextComponents(List<SubComponent> textComponentDetails){
        List<TextComponentDetail> textComponents = textComponentDetails.stream()
                .filter(item -> (item instanceof TextComponentDetail))
                .map(item -> (TextComponentDetail) item)
                .collect(Collectors.toList());

        if(!textComponents.isEmpty()){
            textComponentJdbcRepository.updateAll(textComponents);
        }
    }


}
