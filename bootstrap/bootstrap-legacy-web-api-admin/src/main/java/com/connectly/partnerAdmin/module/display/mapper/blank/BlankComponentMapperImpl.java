package com.connectly.partnerAdmin.module.display.mapper.blank;

import com.connectly.partnerAdmin.module.display.dto.component.blank.BlankComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.sub.BlankComponent;
import org.springframework.stereotype.Component;

@Component
public class BlankComponentMapperImpl implements BlankComponentMapper {

    @Override
    public BlankComponent toEntity(com.connectly.partnerAdmin.module.display.entity.component.Component component, BlankComponentDetail blankComponentDetail) {
        return BlankComponent.builder()
                .component(component)
                .height(blankComponentDetail.getHeight())
                .lineYn(blankComponentDetail.getLineYn())
                .build();
    }
}
