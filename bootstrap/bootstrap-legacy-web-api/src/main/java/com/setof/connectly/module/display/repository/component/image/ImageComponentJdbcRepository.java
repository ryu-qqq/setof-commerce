package com.setof.connectly.module.display.repository.component.image;

import java.util.List;

public interface ImageComponentJdbcRepository {

    void deleteAll(List<Long> imageComponentIds);
}
