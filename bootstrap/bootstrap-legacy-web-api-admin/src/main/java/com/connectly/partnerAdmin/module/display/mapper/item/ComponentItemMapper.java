package com.connectly.partnerAdmin.module.display.mapper.item;

import com.connectly.partnerAdmin.module.display.dto.component.DisplayProductGroupThumbnail;
import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentItemQueryDto;
import com.connectly.partnerAdmin.module.display.entity.component.ComponentItem;
import com.connectly.partnerAdmin.module.display.entity.component.ComponentTarget;

import java.util.List;

public interface ComponentItemMapper {


    List<ComponentItem> toEntities(ComponentTarget componentTarget, List<DisplayProductGroupThumbnail> productGroupThumbnails);

    List<SortItem> transProductGroupThumbnail(List<ComponentItemQueryDto> componentItemQueries);


}
