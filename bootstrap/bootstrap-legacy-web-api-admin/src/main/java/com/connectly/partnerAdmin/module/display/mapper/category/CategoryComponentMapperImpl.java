package com.connectly.partnerAdmin.module.display.mapper.category;


import com.connectly.partnerAdmin.module.display.dto.component.category.CategoryComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.sub.product.CategoryComponent;
import org.springframework.stereotype.Component;

@Component
public class CategoryComponentMapperImpl implements CategoryComponentMapper{


    @Override
    public CategoryComponent toEntity(com.connectly.partnerAdmin.module.display.entity.component.Component component, CategoryComponentDetail categoryComponentDetail) {
        return CategoryComponent.builder()
                .component(component)
                .categoryId(categoryComponentDetail.getCategoryId())
                .build();
    }
}
