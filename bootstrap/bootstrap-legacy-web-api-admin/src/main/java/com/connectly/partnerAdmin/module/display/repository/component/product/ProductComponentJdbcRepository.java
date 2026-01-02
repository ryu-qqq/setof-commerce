package com.connectly.partnerAdmin.module.display.repository.component.product;

import java.util.List;

public interface ProductComponentJdbcRepository {

    void deleteAll(List<Long> productComponentIds);
}
