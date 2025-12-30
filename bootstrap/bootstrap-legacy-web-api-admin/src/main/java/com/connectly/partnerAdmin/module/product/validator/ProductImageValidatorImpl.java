package com.connectly.partnerAdmin.module.product.validator;

import java.util.List;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.product.dto.query.CreateProductImage;

@Component
public class ProductImageValidatorImpl implements ProductImageValidator {

    @Override
    public boolean isValid(List<CreateProductImage> createProductImages) {

        long mainImageCount = createProductImages.stream()
                .filter(c -> c.getProductImageType().isMain())
                .count();

        return  mainImageCount == 1;
    }
}
