package com.setof.connectly.module.display.repository.component.image;

import com.setof.connectly.module.display.entity.component.item.ImageComponentItem;
import java.util.List;

public interface ImageComponentItemJdbcRepository {

    void saveAll(List<ImageComponentItem> imageComponentItems);

    void deleteAll(List<Long> imageComponentIds);

    void updateAll(List<ImageComponentItem> imageComponentItems);
}
