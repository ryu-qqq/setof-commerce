package com.connectly.partnerAdmin.module.product.validator;

import com.connectly.partnerAdmin.module.product.dto.query.CreatePrice;

public interface PriceValidator {

    boolean isValid(CreatePrice createPrice);

}
