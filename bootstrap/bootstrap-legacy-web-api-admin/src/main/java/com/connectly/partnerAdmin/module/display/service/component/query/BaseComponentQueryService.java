package com.connectly.partnerAdmin.module.display.service.component.query;

import com.connectly.partnerAdmin.module.display.dto.component.ComponentUpdatePair;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.ViewExtension;
import com.connectly.partnerAdmin.module.display.mapper.ComponentQueryMapper;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.ComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public abstract class BaseComponentQueryService <T extends SubComponent>  implements SubComponentQueryService<T> {

    private final ComponentQueryMapper componentQueryMapper;
    private final ComponentRepository componentRepository;
    private final ComponentJdbcRepository componentJdbcRepository;


    public abstract void createComponents(long contentId, List<T> components);


    protected Component saveComponent(long contentId, ViewExtension viewExtension, SubComponent subCreateComponent){
        Component component = toComponent(contentId, viewExtension, subCreateComponent);
        return componentRepository.save(component);
    }

    private Component toComponent(long contentId, ViewExtension viewExtension, SubComponent subCreateComponent){
        return componentQueryMapper.toEntity(contentId, viewExtension, subCreateComponent);
    }

    protected void updateSubComponents(List<SubComponent> components){
        componentJdbcRepository.updateAll(components);
    }




}
