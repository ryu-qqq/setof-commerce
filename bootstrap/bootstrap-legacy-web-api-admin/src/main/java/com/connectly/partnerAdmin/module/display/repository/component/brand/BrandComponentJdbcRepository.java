package com.connectly.partnerAdmin.module.display.repository.component.brand;

import com.connectly.partnerAdmin.module.display.entity.component.sub.product.BrandComponent;

import java.util.List;

public interface BrandComponentJdbcRepository {

    void saveAll(List<BrandComponent> brandComponents);

    void deleteAll(List<Long> brandComponentIds);
}
