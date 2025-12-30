package com.connectly.partnerAdmin.module.display.repository.component.blank;

import com.connectly.partnerAdmin.module.display.dto.component.blank.BlankComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.sub.BlankComponent;

import java.util.List;

public interface BlankComponentJdbcRepository {

    void saveAll(List<BlankComponent> blankComponents);
    void updateAll(List<BlankComponentDetail> blankComponentDetails);
    void deleteAll(List<Long> blankComponentIds);
}
