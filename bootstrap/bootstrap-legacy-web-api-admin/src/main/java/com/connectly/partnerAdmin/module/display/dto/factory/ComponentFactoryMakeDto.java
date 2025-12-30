package com.connectly.partnerAdmin.module.display.dto.factory;

import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.query.ComponentQueryDto;


import java.util.Collections;
import java.util.List;

public interface ComponentFactoryMakeDto {

    SubComponent toComponentDetail();
    long getContentId();
    long getComponentId();
    long getSubComponentId();

    default Long getTargetId(){
        return 0L;
    }

    default List<Long> getTargetIds() {
        return Collections.emptyList();
    }

    default List<SortItem> getSortItems() {
        return Collections.emptyList();
    }



}
