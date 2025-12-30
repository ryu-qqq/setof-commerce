package com.connectly.partnerAdmin.module.display.mapper.title;

import com.connectly.partnerAdmin.module.display.dto.component.title.TitleComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.sub.TitleComponent;

public interface TitleComponentMapper {

    TitleComponent toEntity(Component component, TitleComponentDetail titleComponentDetail);
}
