package com.connectly.partnerAdmin.module.display.repository.component.title;


import com.connectly.partnerAdmin.module.display.dto.component.title.TitleComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.sub.TitleComponent;

import java.util.List;

public interface TitleComponentJdbcRepository {
    void saveAll(List<TitleComponent> titleComponents);
    void updateAll(List<TitleComponentDetail> titleComponents);
    void deleteAll(List<Long> titleComponentIds);
}
