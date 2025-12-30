package com.connectly.partnerAdmin.module.display.mapper.blank;

import com.connectly.partnerAdmin.module.display.dto.component.blank.BlankComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.sub.BlankComponent;

public interface BlankComponentMapper {


    BlankComponent toEntity(Component component, BlankComponentDetail blankComponentDetail);
}
