package com.connectly.partnerAdmin.module.display.repository.component.category;

import com.connectly.partnerAdmin.module.display.dto.component.category.CategoryComponentDetail;

import java.util.List;

public interface CategoryComponentJdbcRepository {

    void deleteAll(List<Long> categoryComponentIds);

    void updateAll(List<CategoryComponentDetail> componentDetails);
}
