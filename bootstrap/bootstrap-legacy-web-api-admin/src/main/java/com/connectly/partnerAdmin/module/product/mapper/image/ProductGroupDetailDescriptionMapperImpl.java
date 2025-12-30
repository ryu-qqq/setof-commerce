package com.connectly.partnerAdmin.module.product.mapper.image;

import com.connectly.partnerAdmin.module.product.entity.image.ProductGroupDetailDescription;
import com.connectly.partnerAdmin.module.product.entity.image.embedded.ImageDetail;
import com.connectly.partnerAdmin.module.product.enums.image.ProductGroupImageType;
import org.springframework.stereotype.Component;

@Component
public class ProductGroupDetailDescriptionMapperImpl implements ProductGroupDetailDescriptionMapper{

    @Override
    public ProductGroupDetailDescription toProductDetailDescription(String detailDescription) {
        return ProductGroupDetailDescription.builder()
                .imageDetail(new ImageDetail(ProductGroupImageType.DESCRIPTION, detailDescription))
                .build();
    }

}
