package com.connectly.partnerAdmin.module.display.service.component.query.item;


import com.connectly.partnerAdmin.module.display.entity.component.ComponentTarget;
import com.connectly.partnerAdmin.module.display.repository.component.item.ComponentTargetJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.item.ComponentTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Service
@RequiredArgsConstructor
public class ComponentTargetQueryServiceImpl implements ComponentTargetQueryService{

    private final ComponentTargetRepository componentTargetRepository;
    private final ComponentTargetJdbcRepository componentTargetJdbcRepository;

    @Override
    public ComponentTarget saveComponentTarget(ComponentTarget componentTarget){
        return componentTargetRepository.save(componentTarget);
    }

    @Override
    public void deleteComponentTarget(List<Long> ComponentTargetIds) {
        componentTargetJdbcRepository.deleteAll(ComponentTargetIds);
    }

    @Override
    public void deleteComponentTargetWithTabIds(List<Long> tabIds) {
        componentTargetJdbcRepository.deleteAllWithTabIds(tabIds);
    }


}
