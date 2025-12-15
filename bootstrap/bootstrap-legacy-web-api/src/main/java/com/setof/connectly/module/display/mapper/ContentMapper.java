package com.setof.connectly.module.display.mapper;

import com.setof.connectly.module.display.dto.component.SubComponent;
import com.setof.connectly.module.display.dto.content.ContentGroupResponse;
import com.setof.connectly.module.display.dto.query.ContentQueryDto;
import java.util.Map;

public interface ContentMapper {
    ContentGroupResponse toContentGroupResponse(
            ContentQueryDto contentQueryDto, Map<Integer, SubComponent> combinedMap);
}
