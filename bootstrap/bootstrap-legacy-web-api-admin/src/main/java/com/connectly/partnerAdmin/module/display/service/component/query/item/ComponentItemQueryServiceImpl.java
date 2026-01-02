package com.connectly.partnerAdmin.module.display.service.component.query.item;


import com.connectly.partnerAdmin.module.display.dto.component.DisplayProductGroupThumbnail;
import com.connectly.partnerAdmin.module.display.entity.component.ComponentItem;
import com.connectly.partnerAdmin.module.display.enums.SortType;
import com.connectly.partnerAdmin.module.display.repository.component.item.ComponentItemJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Service
@RequiredArgsConstructor
public class ComponentItemQueryServiceImpl implements ComponentItemQueryService{

    private final ComponentItemJdbcRepository componentItemJdbcRepository;

    @Override
    public void saveComponentItems(List<ComponentItem> componentItem) {
        componentItemJdbcRepository.saveAll(componentItem);
    }

    @Override
    public void deleteComponentItems(List<Long> componentIds) {
        componentItemJdbcRepository.deleteAll(componentIds);
    }

    @Override
    public void deleteComponentItemsWithTabIds(List<Long> tabIds) {
        componentItemJdbcRepository.deleteAllWithTabIds(tabIds);
    }

    @Override
    public void deleteComponentItems(long componentId, SortType sortType,  List<Long> productGroupIds) {
        componentItemJdbcRepository.deleteAll(componentId, sortType, productGroupIds);
    }

    @Override
    public void updateComponentItems(long componentId, long componentTargetId, List<DisplayProductGroupThumbnail> updatedProducts) {
        componentItemJdbcRepository.updateAll(componentId, componentTargetId, updatedProducts);
    }

}
