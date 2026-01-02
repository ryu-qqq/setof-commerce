package com.connectly.partnerAdmin.module.product.validator;

import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.product.dto.query.CreatePrice;
import org.springframework.stereotype.Component;

@Component
public class PriceValidatorImpl implements PriceValidator{

    @Override
    public boolean isValid(CreatePrice createPrice) {
        if(createPrice.getCurrentPrice().isLessThan(Money.ZERO) || createPrice.getRegularPrice().isLessThan(Money.ZERO)) {
            return false;
        }
        return createPrice.getRegularPrice().isGreaterThanOrEqual(createPrice.getCurrentPrice());
    }
}
