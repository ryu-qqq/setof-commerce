package com.connectly.partnerAdmin.module.display.factory;


import com.connectly.partnerAdmin.module.display.dto.component.product.ProductComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.factory.ProductComponentFactoryDto;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import org.springframework.stereotype.Component;

@Component
public class ProductComponentFactory implements ComponentFactory<ProductComponentDetail, ProductComponentFactoryDto>{
    @Override
    public ComponentType getComponentType() {
        return ComponentType.PRODUCT;
    }

    @Override
    public ProductComponentDetail makeSubComponent(ProductComponentFactoryDto dto) {
        return dto.toComponentDetail();
    }
}
