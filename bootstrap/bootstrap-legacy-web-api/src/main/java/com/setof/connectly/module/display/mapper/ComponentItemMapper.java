package com.setof.connectly.module.display.mapper;

import com.setof.connectly.module.display.dto.component.SortItem;
import com.setof.connectly.module.display.dto.query.ComponentItemQueryDto;
import java.util.List;

public interface ComponentItemMapper {

    List<SortItem> transProductGroupThumbnail(List<ComponentItemQueryDto> componentItemQueries);
}
