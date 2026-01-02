package com.connectly.partnerAdmin.module.display.mapper.product;

import com.connectly.partnerAdmin.module.display.dto.component.product.ProductComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.sub.product.ProductComponent;
import org.springframework.stereotype.Component;

@Component
public class ProductComponentItemMapperImpl implements ProductComponentItemMapper{


    @Override
    public ProductComponent toEntity(com.connectly.partnerAdmin.module.display.entity.component.Component component, ProductComponentDetail productComponentDetail) {
        return ProductComponent.builder()
                .component(component)
                .build();
    }
}
