package com.setof.connectly.module.display.factory;

import com.setof.connectly.module.display.dto.component.category.CategoryComponentDetail;
import com.setof.connectly.module.display.dto.factory.CategoryComponentFactoryDto;
import com.setof.connectly.module.display.enums.component.ComponentType;
import org.springframework.stereotype.Component;

@Component
public class CategoryComponentFactory
        implements ComponentFactory<CategoryComponentDetail, CategoryComponentFactoryDto> {

    @Override
    public ComponentType getComponentType() {
        return ComponentType.CATEGORY;
    }

    @Override
    public CategoryComponentDetail makeSubComponent(CategoryComponentFactoryDto dto) {
        return dto.toComponentDetail();
    }
}
