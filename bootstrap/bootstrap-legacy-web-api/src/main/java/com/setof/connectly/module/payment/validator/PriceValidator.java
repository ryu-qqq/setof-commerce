package com.setof.connectly.module.payment.validator;

import com.setof.connectly.module.order.dto.order.OrderSheet;
import com.setof.connectly.module.payment.annotation.ValidPrice;
import com.setof.connectly.module.payment.dto.payment.BasePayment;
import com.setof.connectly.module.product.service.price.PriceService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PriceValidator implements ConstraintValidator<ValidPrice, BasePayment> {

    private final PriceService priceService;

    @Override
    public boolean isValid(BasePayment value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        List<OrderSheet> orders = value.getOrders();
        priceService.checkPrices(orders);

        return true;
    }
}
