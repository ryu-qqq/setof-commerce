package com.connectly.partnerAdmin.module.display.mapper.tab;

import com.connectly.partnerAdmin.module.display.dto.component.tab.TabComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.tab.TabDetail;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.item.Tab;
import com.connectly.partnerAdmin.module.display.entity.component.sub.product.TabComponent;

import java.util.List;

public interface TabComponentMapper {

    TabComponent toEntity(Component component, TabComponentDetail tabComponentDetail);

    List<Tab> toTabEntities(long tabComponentId, List<TabDetail> tabDetails);
}
