package com.connectly.partnerAdmin.module.display.mapper.text;

import com.connectly.partnerAdmin.module.display.dto.component.text.TextComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.sub.TextComponent;
import org.springframework.stereotype.Component;

@Component
public class TextComponentMapperImpl implements TextComponentMapper {

    @Override
    public TextComponent toEntity(com.connectly.partnerAdmin.module.display.entity.component.Component component, TextComponentDetail textComponentDetail) {
        return TextComponent.builder()
                .component(component)
                .content(textComponentDetail.getContent())
                .build();
    }
}
