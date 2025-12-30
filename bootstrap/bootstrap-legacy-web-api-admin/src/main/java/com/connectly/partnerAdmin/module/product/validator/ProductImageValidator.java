package com.connectly.partnerAdmin.module.product.validator;


import com.connectly.partnerAdmin.module.product.dto.query.CreateProductImage;

import java.util.List;

public interface ProductImageValidator {
    boolean isValid(List<CreateProductImage> createProductImages);
}

