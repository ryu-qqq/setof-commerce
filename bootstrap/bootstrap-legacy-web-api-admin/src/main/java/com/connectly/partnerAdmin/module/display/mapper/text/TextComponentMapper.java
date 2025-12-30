package com.connectly.partnerAdmin.module.display.mapper.text;

import com.connectly.partnerAdmin.module.display.dto.component.text.TextComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.sub.TextComponent;

public interface TextComponentMapper {

    TextComponent toEntity(Component component, TextComponentDetail textComponentDetail);

}
