package com.connectly.partnerAdmin.module.display.repository.component.text;

import com.connectly.partnerAdmin.module.display.dto.component.text.TextComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.sub.TextComponent;

import java.util.List;

public interface TextComponentJdbcRepository {

    void saveAll(List<TextComponent> textComponents);
    void updateAll(List<TextComponentDetail> textComponents);
    void deleteAll(List<Long> textComponentIds);
}
