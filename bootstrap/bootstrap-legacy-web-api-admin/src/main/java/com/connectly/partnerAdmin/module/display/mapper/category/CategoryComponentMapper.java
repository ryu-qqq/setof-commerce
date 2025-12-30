package com.connectly.partnerAdmin.module.display.mapper.category;

import com.connectly.partnerAdmin.module.display.dto.component.category.CategoryComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.sub.product.CategoryComponent;

public interface CategoryComponentMapper {


    CategoryComponent toEntity(Component component, CategoryComponentDetail categoryComponentDetail);
}
