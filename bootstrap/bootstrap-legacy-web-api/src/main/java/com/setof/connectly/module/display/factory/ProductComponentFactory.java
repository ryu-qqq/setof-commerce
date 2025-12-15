package com.setof.connectly.module.display.factory;

import com.setof.connectly.module.display.dto.component.product.ProductComponentDetail;
import com.setof.connectly.module.display.dto.factory.ProductComponentFactoryDto;
import com.setof.connectly.module.display.enums.component.ComponentType;
import org.springframework.stereotype.Component;

@Component
public class ProductComponentFactory
        implements ComponentFactory<ProductComponentDetail, ProductComponentFactoryDto> {
    @Override
    public ComponentType getComponentType() {
        return ComponentType.PRODUCT;
    }

    @Override
    public ProductComponentDetail makeSubComponent(ProductComponentFactoryDto dto) {
        return dto.toComponentDetail();
    }
}
