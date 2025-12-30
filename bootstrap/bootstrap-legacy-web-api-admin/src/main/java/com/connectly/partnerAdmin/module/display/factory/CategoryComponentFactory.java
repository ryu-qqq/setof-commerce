package com.connectly.partnerAdmin.module.display.factory;


import com.connectly.partnerAdmin.module.display.dto.component.category.CategoryComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.factory.CategoryComponentFactoryDto;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import org.springframework.stereotype.Component;

@Component
public class CategoryComponentFactory implements ComponentFactory<CategoryComponentDetail, CategoryComponentFactoryDto>{

    @Override
    public ComponentType getComponentType() {
        return ComponentType.CATEGORY;
    }

    @Override
    public CategoryComponentDetail makeSubComponent(CategoryComponentFactoryDto dto) {
        return dto.toComponentDetail();
    }
}
