package com.connectly.partnerAdmin.module.display.repository.component.image;

import java.util.List;

public interface ImageComponentJdbcRepository {

    void deleteAll(List<Long> imageComponentIds);
}
