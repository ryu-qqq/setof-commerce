package com.connectly.partnerAdmin.module.display.service.component.query.item.fetch;


import com.connectly.partnerAdmin.module.display.entity.component.ComponentTarget;
import com.connectly.partnerAdmin.module.display.enums.SortType;
import com.connectly.partnerAdmin.module.display.repository.component.item.fetch.ComponentTargetFetchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ComponentTargetFindServiceImpl implements ComponentTargetFindService{

    private final ComponentTargetFetchRepository componentTargetFetchRepository;

    @Override
    public Optional<ComponentTarget> fetchComponentTarget(long componentId, SortType sortType, Long tabId) {
        return componentTargetFetchRepository.fetchComponentTarget(componentId, sortType, tabId);
    }

}
