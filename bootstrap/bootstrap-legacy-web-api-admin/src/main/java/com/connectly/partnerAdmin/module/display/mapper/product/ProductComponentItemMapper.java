package com.connectly.partnerAdmin.module.display.mapper.product;

import com.connectly.partnerAdmin.module.display.dto.component.product.ProductComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.component.ComponentTarget;
import com.connectly.partnerAdmin.module.display.entity.component.sub.product.ProductComponent;
import com.connectly.partnerAdmin.module.display.enums.SortType;

public interface ProductComponentItemMapper {

    ProductComponent toEntity(Component component, ProductComponentDetail productComponentDetail);

}
