package com.setof.connectly.module.display.repository.component.tab;

import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TabComponentFindRepository {
    List<ProductGroupThumbnail> getTabComponentsWhenLesserThanExposedSize(
            long componentId, long tabId, Pageable pageable);
}
