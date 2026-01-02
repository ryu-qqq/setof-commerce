package com.connectly.partnerAdmin.module.display.mapper.tab;


import com.connectly.partnerAdmin.module.display.dto.component.tab.TabComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.tab.TabDetail;
import com.connectly.partnerAdmin.module.display.entity.component.item.Tab;
import com.connectly.partnerAdmin.module.display.entity.component.sub.product.TabComponent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TabComponentMapperImpl implements TabComponentMapper{


    @Override
    public TabComponent toEntity(com.connectly.partnerAdmin.module.display.entity.component.Component component, TabComponentDetail tabComponentDetail) {
        return TabComponent.builder()
                .component(component)
                .tabMovingType(tabComponentDetail.getTabDetails().get(0).getTabMovingType())
                .displayOrder(component.getDisplayOrder())
                .stickyYn(tabComponentDetail.getTabDetails().get(0).getStickyYn())
                .build();

    }

    @Override
    public List<Tab> toTabEntities(long tabComponentId, List<TabDetail> tabDetails) {
        return tabDetails
                .stream()
                .map(tabDetail -> {
                    return Tab.builder()
                            .tabName(tabDetail.getTabName())
                            .tabComponentId(tabComponentId)
                            .displayOrder(tabDetail.getDisplayOrder())
                            .build();

                }).collect(Collectors.toList());
    }
}
