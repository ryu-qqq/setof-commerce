package com.setof.connectly.module.display.service.content;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.dto.content.ContentGroupResponse;
import com.setof.connectly.module.display.dto.content.OnDisplayContent;
import com.setof.connectly.module.display.dto.filter.ComponentFilter;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import org.springframework.data.domain.Pageable;

public interface ContentFindService {

    OnDisplayContent fetchOnDisplayContents();

    ContentGroupResponse fetchContent(long contentId, Yn bypass);

    ContentGroupResponse fetchOnlyContent(long contentId);

    CustomSlice<ProductGroupThumbnail> fetchComponentProductGroups(
            long componentId, ComponentFilter filter, Pageable pageable);
}
